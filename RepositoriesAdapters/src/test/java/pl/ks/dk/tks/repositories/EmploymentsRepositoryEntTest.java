package pl.ks.dk.tks.repositories;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.employments.EmploymentEnt;
import pl.ks.dk.tks.model.users.ClientEnt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmploymentsRepositoryEntTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void getNumberOfElements() {
        EmploymentsRepositoryEnt employmentsRepository = new EmploymentsRepositoryEnt();

        assertEquals(0, employmentsRepository.getElements().size());
        assertEquals(employmentsRepository.getElements().size(),
                employmentsRepository.getNumberOfElements());

        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        employmentsRepository.addElement(employment);

        assertEquals(1, employmentsRepository.getElements().size());
        assertEquals(employmentsRepository.getElements().size(),
                employmentsRepository.getNumberOfElements());
    }

    @Test
    void checkIfTheElementIsPresent() {
        EmploymentsRepositoryEnt employmentsRepository = new EmploymentsRepositoryEnt();

        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        employmentsRepository.addElement(employment);

        assertTrue(employmentsRepository.checkIfTheElementIsPresent(employment));
    }

    @Test
    void addElement() {
        EmploymentsRepositoryEnt employmentsRepository = new EmploymentsRepositoryEnt();

        assertEquals(0, employmentsRepository.getNumberOfElements());

        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        employmentsRepository.addElement(employment);

        assertEquals(1, employmentsRepository.getNumberOfElements());
    }

    @Test
    void deleteElement() {
        EmploymentsRepositoryEnt employmentsRepository = new EmploymentsRepositoryEnt();

        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        employmentsRepository.addElement(employment);

        assertEquals(1, employmentsRepository.getNumberOfElements());

        employmentsRepository.deleteElement(employment);

        assertEquals(0, employmentsRepository.getNumberOfElements());
    }

    @Test
    void getElements() {
        EmploymentsRepositoryEnt employmentsRepository = new EmploymentsRepositoryEnt();

        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        employmentsRepository.addElement(employment);

        List<EmploymentEnt> employmentList = new ArrayList<>();
        employmentList.add(employment);

        assertEquals(employmentList, employmentsRepository.getElements());
    }

    @Test
    void testToString() {
        EmploymentsRepositoryEnt employmentsRepository = new EmploymentsRepositoryEnt();

        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        employmentsRepository.addElement(employment);

        log.config(employmentsRepository.toString());
    }
}