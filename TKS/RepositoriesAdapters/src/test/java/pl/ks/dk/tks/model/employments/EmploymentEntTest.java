package pl.ks.dk.tks.model.employments;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.babysitters.TeachingSitterEnt;
import pl.ks.dk.tks.model.babysitters.TidingSitterEnt;
import pl.ks.dk.tks.model.exceptions.EmploymentExceptionEnt;
import pl.ks.dk.tks.model.users.ClientEnt;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class EmploymentEntTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void endEmployment() {
        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20.0, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        assertNull(employment.getEndOfEmployment());

        employment.endEmployment();

        assertNotNull(employment.getEndOfEmployment());
    }

    @Test
    void isEnded() {
        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20.0, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        assertFalse(employment.isEnded());

        employment.endEmployment();

        assertTrue(employment.isEnded());
    }

    @Test
    void employmentDurationInHours() {
        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20.0, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);
        employment.endEmployment();
        employment.setEndOfEmployment(employment.getEndOfEmployment().plusMinutes(66));

        assertEquals(2, employment.employmentDurationInHours());
    }

    @Test
    void employmentDurationInHours_NotEndedEmployment() {
        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 20.0, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        assertThrows(EmploymentExceptionEnt.class, employment::employmentDurationInHours);
    }

    @Test
    void employmentCost_Case1() {
        BabysitterEnt babysitter = new BabysitterEnt("Anna", "Kowalska", 15.0, 4, 5);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        assertThrows(EmploymentExceptionEnt.class, employment::employmentCost);

        employment.endEmployment();

        employment.setEndOfEmployment(employment.getEndOfEmployment().plusMinutes(66));

        assertEquals(30, employment.employmentCost());
    }

    @Test
    void employmentCost_Case2() {
        BabysitterEnt babysitter = new TeachingSitterEnt("Anna", "Kowalska", 10.0, 4, 5, 10);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        assertThrows(EmploymentExceptionEnt.class, employment::employmentCost);

        employment.endEmployment();

        employment.setEndOfEmployment(employment.getEndOfEmployment().plusMinutes(66));

        assertEquals(40, employment.employmentCost());
    }

    @Test
    void employmentCost_Case3() {
        BabysitterEnt babysitter = new TidingSitterEnt("Anna", "Kowalska", 20.0, 4, 5, 500);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);

        assertThrows(EmploymentExceptionEnt.class, employment::employmentCost);

        employment.endEmployment();

        employment.setEndOfEmployment(employment.getEndOfEmployment().plusMinutes(66));

        assertEquals(80, employment.employmentCost());
    }

    @Test
    void testToString() {

        BabysitterEnt babysitter = new TidingSitterEnt("Anna", "Kowalska", 20.0, 4, 5, 500);

        ClientEnt client = new ClientEnt("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        EmploymentEnt employment = new EmploymentEnt(babysitter, client);
        employment.endEmployment();

        log.config(employment.toString());
    }
}