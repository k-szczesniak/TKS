package com.mycompany.firstapplication.Employment;

import com.mycompany.firstapplication.Babysitters.Babysitter;
import com.mycompany.firstapplication.Users.Client;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmploymentsRepositoryTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void getNumberOfElements() {
        EmploymentsRepository employmentsRepository = new EmploymentsRepository();

        assertEquals(0, employmentsRepository.getElements().size());
        assertEquals(employmentsRepository.getElements().size(),
                employmentsRepository.getNumberOfElements());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        employmentsRepository.addElement(employment);

        assertEquals(1, employmentsRepository.getElements().size());
        assertEquals(employmentsRepository.getElements().size(),
                employmentsRepository.getNumberOfElements());
    }

    @Test
    void checkIfTheElementIsPresent() {
        EmploymentsRepository employmentsRepository = new EmploymentsRepository();

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        employmentsRepository.addElement(employment);

        assertTrue(employmentsRepository.checkIfTheElementIsPresent(employment));
    }

    @Test
    void addElement() {
        EmploymentsRepository employmentsRepository = new EmploymentsRepository();

        assertEquals(0, employmentsRepository.getNumberOfElements());

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        employmentsRepository.addElement(employment);

        assertEquals(1, employmentsRepository.getNumberOfElements());
    }

    @Test
    void deleteElement() {
        EmploymentsRepository employmentsRepository = new EmploymentsRepository();

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        employmentsRepository.addElement(employment);

        assertEquals(1, employmentsRepository.getNumberOfElements());

        employmentsRepository.deleteElement(employment);

        assertEquals(0, employmentsRepository.getNumberOfElements());
    }

    @Test
    void getElements() {
        EmploymentsRepository employmentsRepository = new EmploymentsRepository();

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        employmentsRepository.addElement(employment);

        List<Employment> employmentList = new ArrayList<>();
        employmentList.add(employment);

        assertEquals(employmentList, employmentsRepository.getElements());
    }

    @Test
    void testToString() {
        EmploymentsRepository employmentsRepository = new EmploymentsRepository();

        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        employmentsRepository.addElement(employment);

        log.config(employmentsRepository.toString());
    }
}