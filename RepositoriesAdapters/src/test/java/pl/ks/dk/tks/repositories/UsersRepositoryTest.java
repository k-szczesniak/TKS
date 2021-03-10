package pl.ks.dk.tks.repositories;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.model.exceptions.UserExceptionEnt;
import pl.ks.dk.tks.model.users.ClientEnt;
import pl.ks.dk.tks.model.users.UserEnt;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsersRepositoryTest {

    @Test
    void UserRepositoryTest() {

        UsersRepositoryEnt usersRepository = new UsersRepositoryEnt();

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertEquals(0, usersRepository.getNumberOfElements());

        //correct add
        usersRepository.addElement(client);
        assertEquals(1, usersRepository.getNumberOfElements());

        //non-unique login
        assertThrows(UserExceptionEnt.class, () -> usersRepository.addElement(client));

        //find by uuid
        assertEquals(client, usersRepository.findUserByUuid(client.getUuid()));

        //find by login
        assertEquals(client, usersRepository.findUserByLogin(client.getLogin()));

        //change active
        assertTrue(client.isActive());
        usersRepository.changeActiveForUser(client);
        assertFalse(client.isActive());
        usersRepository.changeActiveForUser(client);
        assertTrue(client.isActive());

        //check if present
        assertTrue(usersRepository.checkIfTheElementIsPresent(client));

        //list
        List<UserEnt> users = new ArrayList<>();
        users.add(client);
        assertEquals(users, usersRepository.getElements());
    }
}