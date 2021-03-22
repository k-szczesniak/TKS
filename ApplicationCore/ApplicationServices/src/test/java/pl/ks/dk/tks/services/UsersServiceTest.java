package pl.ks.dk.tks.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.infrastructure.users.AddUserPort;
import pl.ks.dk.tks.infrastructure.users.GetUserPort;
import pl.ks.dk.tks.infrastructure.users.UpdateUserPort;
import pl.ks.dk.tks.services.exceptions.ServiceException;

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
    void getUserByLogin() {
        given(getUserPort.getUserByLogin(ArgumentMatchers.anyString())).willThrow(
                AdapterException.class);
        given(getUserPort.getUserByLogin(ArgumentMatchers.eq("Login2")))
                .willReturn(new Admin("Login2", "Jan", "Kowalski", "kowalski", "Admin"));

        assertEquals("Jan", usersService.getUserByLogin("Login2").getName());
        assertThrows(ServiceException.class, () -> usersService.getUserByLogin("OtherLogin"));
        verify(getUserPort, times(2)).getUserByLogin(ArgumentMatchers.anyString());
    }

    @Test
    void getUserByKey() {
        given(getUserPort.getUserByKey(ArgumentMatchers.anyString())).willThrow(
                AdapterException.class);
        given(getUserPort.getUserByKey(ArgumentMatchers.eq("12345678")))
                .willReturn(new Admin("Login2", "Jan", "Kowalski", "kowalski", "Admin"));

        assertEquals("Jan", usersService.getUserByKey("12345678").getName());
        assertThrows(ServiceException.class, () -> usersService.getUserByKey("OtherUuid"));
        verify(getUserPort, times(2)).getUserByKey(ArgumentMatchers.anyString());
    }

    @Test
    void getAllUsers() {
        given(getUserPort.getAllUsers()).willReturn(prepareMockData());

        List<User> users = usersService.getAllUsers();
        assertEquals(1, users.size());
        verify(getUserPort, times(1)).getAllUsers();
    }

    @Test
    void addUserPositiveTest() {
        usersService.addUser(new Admin("Login2", "Jan", "Kowalski", "kowalski", "Admin"));
        verify(addUserPort).addUser(ArgumentMatchers.any(User.class));
    }

    @Test
    void addUserNegativeTest() {
        doThrow(AdapterException.class).when(addUserPort).addUser(ArgumentMatchers.any(User.class));
        assertThrows(ServiceException.class,
                () -> usersService.addUser(new Admin("Login2", "Jan", "Kowalski", "kowalski", "Admin")));
    }

    @Test
    void updateUserPositiveTest() {
        usersService.updateUser(new Admin("Login2", "Jan", "Kowalski", "kowalski", "Admin"), "12345678");
        verify(updateUserPort)
                .updateUser(ArgumentMatchers.any(User.class), ArgumentMatchers.anyString());
    }

    @Test
    void updateUserNegativeTest() {
        doThrow(AdapterException.class).when(updateUserPort)
                .updateUser(ArgumentMatchers.any(User.class), ArgumentMatchers.anyString());
        assertThrows(ServiceException.class,
                () -> usersService.updateUser(new Admin("Login2", "Jan", "Kowalski", "kowalski", "Admin"), "12345678"));
    }

    @Test
    void checkIfUserIsActive() {
        given(getUserPort.getUserByLogin(ArgumentMatchers.eq("Login2")))
                .willReturn(new Admin("Login2", "Jan", "Kowalski", "kowalski", "Admin"));
        assertTrue(usersService.checkIfUserIsActive("Login2"));
    }

    @Test
    void getUserByLoginAndPassword() {
        Admin admin = new Admin("Login2", "Jan", "Kowalski", "kowalski", "Admin");
        given(getUserPort.getUserByLogin(ArgumentMatchers.eq("Login2")))
                .willReturn(admin);

        assertEquals(admin, usersService.getUserByLoginAndPassword("Login2", "kowalski"));
        assertThrows(ServiceException.class, () -> usersService.getUserByLoginAndPassword("Login", "BadPassword"));
    }

    private List<User> prepareMockData() {
        List<User> userList = new ArrayList<>();
        userList.add(new Admin("Login2", "Jan", "Kowalski", "kowalski", "Admin"));
        return userList;
    }
}