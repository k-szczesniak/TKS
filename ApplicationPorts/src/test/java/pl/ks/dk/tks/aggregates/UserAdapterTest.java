package pl.ks.dk.tks.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.model.users.AdminEnt;
import pl.ks.dk.tks.model.users.ClientEnt;
import pl.ks.dk.tks.model.users.SuperUserEnt;
import pl.ks.dk.tks.model.users.UserEnt;
import pl.ks.dk.tks.repositories.UsersRepositoryEnt;
import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAdapterTest {

    @Mock
    UsersRepositoryEnt usersRepositoryEnt;

    @InjectMocks
    UserAdapter userAdapter;

    @Test
    void addUserPositiveTest() {
        userAdapter.addUser(new Admin("jKowalski", "Jan", "Kowalski", "kowalski", "Admin"));
        verify(usersRepositoryEnt).addElement(ArgumentMatchers.any(UserEnt.class));
    }

    @Test
    void addUserNegativeTest() {
        doThrow(RepositoryExceptionEnt.class).when(usersRepositoryEnt).addElement(ArgumentMatchers.any(UserEnt.class));
        assertThrows(AdapterException.class, () -> userAdapter.addUser(new Admin("jKowalski", "Jan", "Kowalski", "kowalski", "Admin")));
    }

    @Test
    void getUserByLoginTest() {
        given(usersRepositoryEnt.findUserByLogin(ArgumentMatchers.anyString())).willThrow(RepositoryExceptionEnt.class);
        given(usersRepositoryEnt.findUserByLogin(ArgumentMatchers.eq("aAdamski"))).willReturn(new AdminEnt("aAdamski", "Adam", "Adamski", "adamski", "Admin"));

        assertEquals("Adam", userAdapter.getUserByLogin("aAdamski").getName());
        assertThrows(AdapterException.class, () -> userAdapter.getUserByLogin("OtherLogin"));
        verify(usersRepositoryEnt, times(2)).findUserByLogin(ArgumentMatchers.anyString());
    }

    @Test
    void getUserByKeyTest() {
        given(usersRepositoryEnt.findUserByUuid(ArgumentMatchers.anyString())).willThrow(RepositoryExceptionEnt.class);
        given(usersRepositoryEnt.findUserByUuid(ArgumentMatchers.eq("12345678"))).willReturn(new AdminEnt("aAdamski", "Adam", "Adamski", "adamski", "Admin"));

        assertEquals("Adam", userAdapter.getUserByKey("12345678").getName());
        assertThrows(AdapterException.class, () -> userAdapter.getUserByKey("OtherUuid"));
        verify(usersRepositoryEnt, times(2)).findUserByUuid(ArgumentMatchers.anyString());
    }

    @Test
    void updateUserPositiveTest() {
        userAdapter.updateUser(new Admin("jKowalski", "Jan", "Kowalski", "kowalski", "Admin"), "12345678");
        verify(usersRepositoryEnt).updateElement(ArgumentMatchers.any(UserEnt.class), ArgumentMatchers.anyString());
    }

    @Test
    void updateUserNegativeTest() {
        doThrow(RepositoryExceptionEnt.class).when(usersRepositoryEnt).updateElement(ArgumentMatchers.any(UserEnt.class), ArgumentMatchers.anyString());
        assertThrows(AdapterException.class, () -> userAdapter.updateUser(new Admin("jKowalski", "Jan", "Kowalski", "kowalski", "Admin"), "12345678"));
    }

    @Test
    void getAllUsersTest() {
        given(usersRepositoryEnt.getUsersList()).willReturn(prepareMockData());

        List<User> users = userAdapter.getAllUsers();
        assertEquals(1, users.size());
        verify(usersRepositoryEnt, times(1)).getUsersList();
    }

    @Test
    void getUsersCountTest() {
        given(usersRepositoryEnt.getNumberOfElements()).willReturn(prepareMockData().size());

        int count = userAdapter.getUsersCount();
        assertEquals(1, count);
        verify(usersRepositoryEnt, times(1)).getNumberOfElements();
    }

    @Test
    void convertUserToUserEntTest() {
        User user = new Client("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);
        User admin = new Admin("Login2", "Jan2", "Kowalski", "kowalski", "Admin");
        User superUser = new SuperUser("Login3", "Jan3", "Kowalski", "kowalski", "SuperUser");

        UserEnt userEnt = UserAdapter.convertUserToUserEnt(user);
        UserEnt userEnt1 = UserAdapter.convertUserToUserEnt(admin);
        UserEnt userEnt2 = UserAdapter.convertUserToUserEnt(superUser);

        assertEquals(userEnt.getName(), user.getName());
        assertEquals(userEnt.getLogin(), user.getLogin());

        assertEquals(userEnt1.getName(), admin.getName());
        assertEquals(userEnt1.getLogin(), admin.getLogin());

        assertEquals(userEnt2.getName(), superUser.getName());
        assertEquals(userEnt2.getLogin(), superUser.getLogin());
    }

    @Test
    void convertUserEntToUserTest() {
        UserEnt userEnt = new ClientEnt("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);
        UserEnt userEnt1 = new AdminEnt("Login2", "Jan2", "Kowalski", "kowalski", "Admin");
        UserEnt userEnt2 = new SuperUserEnt("Login3", "Jan3", "Kowalski", "kowalski", "SuperUser");

        User user = UserAdapter.convertUserEntToUser(userEnt);
        User admin = UserAdapter.convertUserEntToUser(userEnt1);
        User superUser = UserAdapter.convertUserEntToUser(userEnt2);

        assertEquals(userEnt.getName(), user.getName());
        assertEquals(userEnt.getLogin(), user.getLogin());

        assertEquals(userEnt1.getName(), admin.getName());
        assertEquals(userEnt1.getLogin(), admin.getLogin());

        assertEquals(userEnt2.getName(), superUser.getName());
        assertEquals(userEnt2.getLogin(), superUser.getLogin());
    }

    private List<UserEnt> prepareMockData() {
        List<UserEnt> users = new ArrayList<>();
        users.add(new AdminEnt("aAdamski", "Adam", "Adamski", "adamski", "Admin"));
        return users;
    }
}
