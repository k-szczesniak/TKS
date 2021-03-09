package com.mycompany.firstapplication.Employment;


import com.mycompany.firstapplication.Babysitters.Babysitter;
import com.mycompany.firstapplication.Exceptions.EmploymentException;
import com.mycompany.firstapplication.Users.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmploymentsManagerTest {

    @Test
    void employBabysitter_CorrectCase() {
        EmploymentsManager employmentsManager = new EmploymentsManager(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertEquals(0, employmentsManager.numberOfCurrentClientEmployment(client));

        employmentsManager.employBabysitter(client, babysitter);
        assertEquals(1, employmentsManager.numberOfCurrentClientEmployment(client));
    }

    @Test
    void employBabysitter_IncorrectCase_BabysitterEmployed() {
        EmploymentsManager employmentsManager = new EmploymentsManager(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client1 = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        employmentsManager.employBabysitter(client1, babysitter);

        Client client2 = new Client("Login", "InnySzymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertThrows(EmploymentException.class,
                () -> employmentsManager.employBabysitter(client2, babysitter));
    }

    @Test
    void employBabysitter_IncorrectCase_BabysitterDoesNotMeetRequires() {
        EmploymentsManager employmentsManager = new EmploymentsManager(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 3);

        assertThrows(EmploymentException.class,
                () -> employmentsManager.employBabysitter(client, babysitter));
    }

    @Test
    void endEmployment_CorrectCase() {
        EmploymentsManager employmentsManager = new EmploymentsManager(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertEquals(0, employmentsManager.numberOfCurrentClientEmployment(client));

        employmentsManager.employBabysitter(client, babysitter);

        assertEquals(1, employmentsManager.numberOfCurrentClientEmployment(client));

        employmentsManager
                .endEmployment(employmentsManager.getActualEmploymentForBabysitter(babysitter));

        assertEquals(0, employmentsManager.numberOfCurrentClientEmployment(client));
    }

    @Test
    void endEmployment_IncorrectCase_TryingToEndEndedEmployment() {
        EmploymentsManager employmentsManager = new EmploymentsManager(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertEquals(0, employmentsManager.numberOfCurrentClientEmployment(client));

        employmentsManager.employBabysitter(client, babysitter);

        assertEquals(1, employmentsManager.numberOfCurrentClientEmployment(client));

        employmentsManager
                .endEmployment(employmentsManager.getActualEmploymentForBabysitter(babysitter));

        assertThrows(EmploymentException.class, () -> employmentsManager
                .endEmployment(employmentsManager.getActualEmploymentForBabysitter(babysitter)));
    }

    @Test
    void numberOfCurrentAndEndedFamilyEmployment() {
        EmploymentsManager employmentsManager = new EmploymentsManager(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertEquals(0, employmentsManager.numberOfCurrentClientEmployment(client));
        assertEquals(0, employmentsManager.numberOfEndedClientEmployment(client));

        employmentsManager.employBabysitter(client, babysitter);

        assertEquals(1, employmentsManager.numberOfCurrentClientEmployment(client));
        assertEquals(0, employmentsManager.numberOfEndedClientEmployment(client));

        employmentsManager
                .endEmployment(employmentsManager.getActualEmploymentForBabysitter(babysitter));

        assertEquals(0, employmentsManager.numberOfCurrentClientEmployment(client));
        assertEquals(1, employmentsManager.numberOfEndedClientEmployment(client));
    }
}