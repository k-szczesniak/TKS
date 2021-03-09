package com.mycompany.firstapplication.Babysitters;

import com.mycompany.firstapplication.Exceptions.BabysitterException;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BabysitterTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void constructor() {
        assertThrows(BabysitterException.class, () -> new Babysitter("Ola", "Nowak", -3, 3, 4));
        assertThrows(BabysitterException.class, () -> new Babysitter("Ola", "Nowak", 10, -3, 4));
        assertThrows(BabysitterException.class, () -> new Babysitter("Ola", "Nowak", 10, 3, -4));
    }

    @Test
    void price() {
        Babysitter babysitter = new Babysitter("Ola", "Nowak", 20, 3, 4);
        assertEquals(20, babysitter.priceForHour());
    }

    @Test
    void getName() {
        Babysitter babysitter = new Babysitter("Ola", "Nowak", 20, 3, 4);
        assertEquals("Ola", babysitter.getName());
    }

    @Test
    void getSurname() {
        Babysitter babysitter = new Babysitter("Ola", "Nowak", 20, 3, 4);
        assertEquals("Nowak", babysitter.getSurname());
    }

    @Test
    void getBasePrice() {
        Babysitter babysitter = new Babysitter("Ola", "Nowak", 20, 3, 4);
        assertEquals(20, babysitter.getBasePriceForHour());
    }

    @Test
    void getMinChildAge() {
        Babysitter babysitter = new Babysitter("Ola", "Nowak", 20, 3, 4);
        assertEquals(3, babysitter.getMinChildAge());
    }

    @Test
    void getMaxNumberOfChildrenInTheFamily() {
        Babysitter babysitter = new Babysitter("Ola", "Nowak", 20, 3, 4);
        assertEquals(4, babysitter.getMaxNumberOfChildrenInTheFamily());
    }

    @Test
    void testToString() {
        Babysitter babysitter = new Babysitter("Ola", "Nowak", 20, 3, 4);
        log.config(babysitter.toString());
    }
}