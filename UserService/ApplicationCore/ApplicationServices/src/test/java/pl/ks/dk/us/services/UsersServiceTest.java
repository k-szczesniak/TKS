package pl.ks.dk.us.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ks.dk.us.exceptions.AdapterException;
import pl.ks.dk.us.infrastructure.AddUserPort;
import pl.ks.dk.us.infrastructure.GetUserPort;
import pl.ks.dk.us.infrastructure.UpdateUserPort;
import pl.ks.dk.us.services.exceptions.ServiceException;
import pl.ks.dk.us.users.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @InjectMocks
    UsersService usersService;

    @Mock
    private AddUserPort addUserPort;

    @Mock
    private GetUserPort getUserPort;

    @Mock
    private UpdateUserPort updateUserPort;

    @Test
    void getUserByLoginTest() {
        given(getUserPort.getUserByLogin(ArgumentMatchers.anyString())).willThrow(
                AdapterException.class);
        given(getUserPort.getUserByLogin(ArgumentMatchers.eq("Login2")))
                .willReturn(new User("Login2", "Jan", "Kowalski", "kowalski"));

        assertEquals("Jan", usersService.getUserByLogin("Login2").getName());
        assertThrows(ServiceException.class, () -> usersService.getUserByLogin("OtherLogin"));
        verify(getUserPort, times(2)).getUserByLogin(ArgumentMatchers.anyString());
    }

    @Test
    void getUserByKeyTest() {
        given(getUserPort.getUserByKey(ArgumentMatchers.anyString())).willThrow(
                AdapterException.class);
        given(getUserPort.getUserByKey(ArgumentMatchers.eq("12345678")))
                .willReturn(new User("Login2", "Jan", "Kowalski", "kowalski"));

        assertEquals("Jan", usersService.getUserByKey("12345678").getName());
        assertThrows(ServiceException.class, () -> usersService.getUserByKey("OtherUuid"));
        verify(getUserPort, times(2)).getUserByKey(ArgumentMatchers.anyString());
    }

    @Test
    void getAllUsersTest() {
        given(getUserPort.getAllUsers()).willReturn(prepareMockData());

        List<User> users = usersService.getAllUsers();
        assertEquals(1, users.size());
        verify(getUserPort, times(1)).getAllUsers();
    }

    @Test
    void getUsersCountTest() {
        given(getUserPort.getUsersCount()).willReturn(prepareMockData().size());

        int count = usersService.getUsersCount();
        assertEquals(1, count);
        verify(getUserPort, times(1)).getUsersCount();
    }

    @Test
    void addUserPositiveTest() {
        usersService.addUser(new User("Login2", "Jan", "Kowalski", "kowalski"));
        verify(addUserPort).addUser(ArgumentMatchers.any(User.class));
    }

    @Test
    void addUserNegativeTest() {
        doThrow(AdapterException.class).when(addUserPort).addUser(ArgumentMatchers.any(User.class));
        assertThrows(ServiceException.class,
                () -> usersService.addUser(new User("Login2", "Jan", "Kowalski", "kowalski")));
    }

    @Test
    void updateUserPositiveTest() {
        usersService.updateUser(new User("Login2", "Jan", "Kowalski", "kowalski"), "12345678");
        verify(updateUserPort)
                .updateUser(ArgumentMatchers.any(User.class), ArgumentMatchers.anyString());
    }

    @Test
    void updateUserNegativeTest() {
        doThrow(AdapterException.class).when(updateUserPort)
                .updateUser(ArgumentMatchers.any(User.class), ArgumentMatchers.anyString());
        assertThrows(ServiceException.class,
                () -> usersService.updateUser(new User("Login2", "Jan", "Kowalski", "kowalski"), "12345678"));
    }

    @Test
    void checkIfUserIsActiveTest() {
        given(getUserPort.getUserByLogin(ArgumentMatchers.eq("Login2")))
                .willReturn(new User("Login2", "Jan", "Kowalski", "kowalski"));
        assertTrue(usersService.checkIfUserIsActive("Login2"));
    }

    @Test
    void getUserByLoginAndPasswordTest() {
        User user = new User("Login2", "Jan", "Kowalski", "kowalski");
        given(getUserPort.getUserByLogin(ArgumentMatchers.eq("Login2")))
                .willReturn(user);

        assertEquals(user, usersService.getUserByLoginAndPassword("Login2", "kowalski"));
        assertThrows(ServiceException.class, () -> usersService.getUserByLoginAndPassword("Login", "BadPassword"));
    }

    private List<User> prepareMockData() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("Login2", "Jan", "Kowalski", "kowalski"));
        return userList;
    }
}