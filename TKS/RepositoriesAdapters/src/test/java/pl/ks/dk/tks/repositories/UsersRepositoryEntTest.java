package pl.ks.dk.tks.repositories;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.model.users.AdminEnt;
import pl.ks.dk.tks.model.users.UserEnt;
import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsersRepositoryEntTest {

    @Test
    void getNumberOfElementsTest() {
        UsersRepositoryEnt usersRepositoryEnt = new UsersRepositoryEnt();

        assertEquals(0, usersRepositoryEnt.getElements().size());
        assertEquals(usersRepositoryEnt.getElements().size(),
                usersRepositoryEnt.getNumberOfElements());

        AdminEnt adminEnt = new AdminEnt("Ola", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        usersRepositoryEnt.addElement(adminEnt);

        assertEquals(1, usersRepositoryEnt.getElements().size());
        assertEquals(usersRepositoryEnt.getElements().size(),
                usersRepositoryEnt.getNumberOfElements());
    }

    @Test
    void getUsersListTest() {
        UsersRepositoryEnt usersRepositoryEnt = new UsersRepositoryEnt();

        AdminEnt adminEnt = new AdminEnt("Ola", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        usersRepositoryEnt.addElement(adminEnt);

        List<UserEnt> userEntList = new ArrayList<>();
        userEntList.add(adminEnt);

        assertEquals(userEntList, usersRepositoryEnt.getUsersList());
    }

    @Test
    void addElementTest() {
        UsersRepositoryEnt usersRepositoryEnt = new UsersRepositoryEnt();

        assertEquals(0, usersRepositoryEnt.getNumberOfElements());

        AdminEnt adminEnt = new AdminEnt("Ola", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        usersRepositoryEnt.addElement(adminEnt);

        assertEquals(1, usersRepositoryEnt.getNumberOfElements());
    }

    @Test
    void addElementNotUniqueLoginTest() {
        UsersRepositoryEnt usersRepositoryEnt = new UsersRepositoryEnt();

        assertEquals(0, usersRepositoryEnt.getNumberOfElements());

        AdminEnt adminEnt1 = new AdminEnt("Ola", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        usersRepositoryEnt.addElement(adminEnt1);

        assertEquals(1, usersRepositoryEnt.getNumberOfElements());

        AdminEnt adminEnt2 = new AdminEnt("Ola", "Tomkowski", "RazDwaTrzy", "Haslo", "Admin");
        assertThrows(RepositoryExceptionEnt.class, () -> usersRepositoryEnt.addElement(adminEnt2));
    }

    @Test
    void tryingToAddAddedElementTest() {
        UsersRepositoryEnt usersRepositoryEnt = new UsersRepositoryEnt();

        assertEquals(0, usersRepositoryEnt.getNumberOfElements());

        AdminEnt adminEnt = new AdminEnt("Ola", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        usersRepositoryEnt.addElement(adminEnt);

        assertEquals(1, usersRepositoryEnt.getNumberOfElements());

        assertThrows(RepositoryExceptionEnt.class, () -> usersRepositoryEnt.addElement(adminEnt));
    }

    @Test
    void checkIfTheElementIsPresentTest() {
        UsersRepositoryEnt usersRepositoryEnt = new UsersRepositoryEnt();

        AdminEnt adminEnt = new AdminEnt("Ola", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        usersRepositoryEnt.addElement(adminEnt);

        assertTrue(usersRepositoryEnt.checkIfTheElementIsPresent(adminEnt));
    }

    @Test
    void findUserByLoginTest() {
        UsersRepositoryEnt usersRepositoryEnt = new UsersRepositoryEnt();

        AdminEnt adminEnt = new AdminEnt("Ola", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        usersRepositoryEnt.addElement(adminEnt);

        assertEquals(adminEnt, usersRepositoryEnt.findUserByLogin("Ola"));
        assertThrows(RepositoryExceptionEnt.class, () -> usersRepositoryEnt.findUserByLogin("Login"));
    }

    @Test
    void findUserByUuidTest() {
        UsersRepositoryEnt usersRepositoryEnt = new UsersRepositoryEnt();

        AdminEnt adminEnt = new AdminEnt("Ola", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        usersRepositoryEnt.addElement(adminEnt);

        String uuid = usersRepositoryEnt.findUserByLogin("Ola").getUuid();

        assertEquals(adminEnt, usersRepositoryEnt.findUserByUuid(uuid));
        assertThrows(RepositoryExceptionEnt.class, () -> usersRepositoryEnt.findUserByUuid("uuid"));
    }

    @Test
    void updateElementTest() {
        UsersRepositoryEnt usersRepositoryEnt = new UsersRepositoryEnt();

        AdminEnt adminEnt = new AdminEnt("Ola", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        usersRepositoryEnt.addElement(adminEnt);

        String uuid = usersRepositoryEnt.findUserByLogin("Ola").getUuid();

        assertEquals(usersRepositoryEnt.findUserByUuid(uuid).getLogin(), "Ola");

        AdminEnt adminEnt2 = new AdminEnt("RazRazRaz", "Nowak", "RazDwaTrzy", "Haslo", "Admin");
        adminEnt2.setUuid(uuid);
        usersRepositoryEnt.updateElement(adminEnt2, uuid);

        assertEquals(usersRepositoryEnt.findUserByUuid(uuid).getLogin(), "RazRazRaz");
        assertNotEquals(usersRepositoryEnt.findUserByUuid(uuid).getLogin(), "Ola");
    }
}