package com.mycompany.firstapplication.Employment;


import com.mycompany.firstapplication.Babysitters.Babysitter;
import com.mycompany.firstapplication.Exceptions.EmploymentException;
import com.mycompany.firstapplication.Users.Client;
import com.mycompany.firstapplication.services.EmploymentsService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmploymentsServiceTest {

    @Test
    void employBabysitter_CorrectCase() {
        EmploymentsService employmentsService = new EmploymentsService(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertEquals(0, employmentsService.numberOfCurrentClientEmployment(client));

        employmentsService.employBabysitter(client, babysitter);
        assertEquals(1, employmentsService.numberOfCurrentClientEmployment(client));
    }

    @Test
    void employBabysitter_IncorrectCase_BabysitterEmployed() {
        EmploymentsService employmentsService = new EmploymentsService(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client1 = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        employmentsService.employBabysitter(client1, babysitter);

        Client client2 = new Client("Login", "InnySzymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertThrows(EmploymentException.class,
                () -> employmentsService.employBabysitter(client2, babysitter));
    }

    @Test
    void employBabysitter_IncorrectCase_BabysitterDoesNotMeetRequires() {
        EmploymentsService employmentsService = new EmploymentsService(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 3);

        assertThrows(EmploymentException.class,
                () -> employmentsService.employBabysitter(client, babysitter));
    }

    @Test
    void endEmployment_CorrectCase() {
        EmploymentsService employmentsService = new EmploymentsService(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertEquals(0, employmentsService.numberOfCurrentClientEmployment(client));

        employmentsService.employBabysitter(client, babysitter);

        assertEquals(1, employmentsService.numberOfCurrentClientEmployment(client));

        employmentsService
                .endEmployment(employmentsService.getActualEmploymentForBabysitter(babysitter));

        assertEquals(0, employmentsService.numberOfCurrentClientEmployment(client));
    }

    @Test
    void endEmployment_IncorrectCase_TryingToEndEndedEmployment() {
        EmploymentsService employmentsService = new EmploymentsService(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertEquals(0, employmentsService.numberOfCurrentClientEmployment(client));

        employmentsService.employBabysitter(client, babysitter);

        assertEquals(1, employmentsService.numberOfCurrentClientEmployment(client));

        employmentsService
                .endEmployment(employmentsService.getActualEmploymentForBabysitter(babysitter));

        assertThrows(EmploymentException.class, () -> employmentsService
                .endEmployment(employmentsService.getActualEmploymentForBabysitter(babysitter)));
    }

    @Test
    void numberOfCurrentAndEndedFamilyEmployment() {
        EmploymentsService employmentsService = new EmploymentsService(new EmploymentsRepository());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        assertEquals(0, employmentsService.numberOfCurrentClientEmployment(client));
        assertEquals(0, employmentsService.numberOfEndedClientEmployment(client));

        employmentsService.employBabysitter(client, babysitter);

        assertEquals(1, employmentsService.numberOfCurrentClientEmployment(client));
        assertEquals(0, employmentsService.numberOfEndedClientEmployment(client));

        employmentsService
                .endEmployment(employmentsService.getActualEmploymentForBabysitter(babysitter));

        assertEquals(0, employmentsService.numberOfCurrentClientEmployment(client));
        assertEquals(1, employmentsService.numberOfEndedClientEmployment(client));
    }
}