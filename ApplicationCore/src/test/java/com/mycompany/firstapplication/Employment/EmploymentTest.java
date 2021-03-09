package com.mycompany.firstapplication.Employment;


import com.mycompany.firstapplication.Babysitters.Babysitter;
import com.mycompany.firstapplication.Babysitters.TeachingSitter;
import com.mycompany.firstapplication.Babysitters.TidingSitter;
import com.mycompany.firstapplication.Exceptions.EmploymentException;
import com.mycompany.firstapplication.Users.Client;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class EmploymentTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void endEmployment() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        assertNull(employment.getEndOfEmployment());

        employment.endEmployment();

        assertNotNull(employment.getEndOfEmployment());
    }

    @Test
    void isEnded() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        assertFalse(employment.isEnded());

        employment.endEmployment();

        assertTrue(employment.isEnded());
    }

    @Test
    void employmentDurationInHours() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);
        employment.endEmployment();
        employment.setEndOfEmployment(employment.getEndOfEmployment().plusMinutes(66));

        assertEquals(2, employment.employmentDurationInHours());
    }

    @Test
    void employmentDurationInHours_NotEndedEmployment() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        assertThrows(EmploymentException.class, employment::employmentDurationInHours);
    }

    @Test
    void employmentCost_Case1() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 15, 4, 5);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        assertThrows(EmploymentException.class, employment::employmentCost);

        employment.endEmployment();

        employment.setEndOfEmployment(employment.getEndOfEmployment().plusMinutes(66));

        assertEquals(30, employment.employmentCost());
    }

    @Test
    void employmentCost_Case2() {
        Babysitter babysitter = new TeachingSitter("Anna", "Kowalska", 10, 4, 5, 10);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        assertThrows(EmploymentException.class, employment::employmentCost);

        employment.endEmployment();

        employment.setEndOfEmployment(employment.getEndOfEmployment().plusMinutes(66));

        assertEquals(40, employment.employmentCost());
    }

    @Test
    void employmentCost_Case3() {
        Babysitter babysitter = new TidingSitter("Anna", "Kowalska", 20, 4, 5, 500);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);

        assertThrows(EmploymentException.class, employment::employmentCost);

        employment.endEmployment();

        employment.setEndOfEmployment(employment.getEndOfEmployment().plusMinutes(66));

        assertEquals(80, employment.employmentCost());
    }

    @Test
    void testToString() {

        Babysitter babysitter = new TidingSitter("Anna", "Kowalska", 20, 4, 5, 500);

        Client client = new Client("Login", "Szymon", "Dubowski", "dubowski", "Client", 5, 10);

        Employment employment = new Employment(babysitter, client);
        employment.endEmployment();

        log.config(employment.toString());
    }
}