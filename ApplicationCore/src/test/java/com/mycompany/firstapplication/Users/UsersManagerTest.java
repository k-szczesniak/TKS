package com.mycompany.firstapplication.Users;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersManagerTest {

    @Test
    void addClient() {
        UsersManager usersManager = new UsersManager(new UsersRepository());

        assertEquals(0, usersManager.getNumberOfClients());

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        usersManager.addUser(client);

        assertEquals(1, usersManager.getNumberOfClients());
    }

    @Test
    void deleteClient() {
        UsersManager usersManager = new UsersManager(new UsersRepository());

        assertEquals(0, usersManager.getNumberOfClients());

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        usersManager.addUser(client);

        assertEquals(1, usersManager.getNumberOfClients());

        usersManager.deleteUser(client);

        assertEquals(0, usersManager.getNumberOfClients());
    }
}
