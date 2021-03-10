package com.mycompany.firstapplication.Users;
import com.mycompany.firstapplication.services.UsersService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersServiceTest {

    @Test
    void addClient() {
        UsersService usersService = new UsersService(new UsersRepository());

        assertEquals(0, usersService.getNumberOfClients());

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        usersService.addUser(client);

        assertEquals(1, usersService.getNumberOfClients());
    }

    @Test
    void deleteClient() {
        UsersService usersService = new UsersService(new UsersRepository());

        assertEquals(0, usersService.getNumberOfClients());

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        usersService.addUser(client);

        assertEquals(1, usersService.getNumberOfClients());

        usersService.deleteUser(client);

        assertEquals(0, usersService.getNumberOfClients());
    }
}
