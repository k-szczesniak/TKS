package pl.ks.dk.tks.model.babysitters;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.model.exceptions.BabysitterExceptionEnt;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BabysitterEntTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void constructor() {
        assertThrows(BabysitterExceptionEnt.class, () -> new BabysitterEnt("Ola", "Nowak", -3, 3, 4));
        assertThrows(BabysitterExceptionEnt.class, () -> new BabysitterEnt("Ola", "Nowak", 10, -3, 4));
        assertThrows(BabysitterExceptionEnt.class, () -> new BabysitterEnt("Ola", "Nowak", 10, 3, -4));
    }

    @Test
    void price() {
        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        assertEquals(20, babysitter.priceForHour());
    }

    @Test
    void getName() {
        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        assertEquals("Ola", babysitter.getName());
    }

    @Test
    void getSurname() {
        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        assertEquals("Nowak", babysitter.getSurname());
    }

    @Test
    void getBasePrice() {
        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        assertEquals(20, babysitter.getBasePriceForHour());
    }

    @Test
    void getMinChildAge() {
        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        assertEquals(3, babysitter.getMinChildAge());
    }

    @Test
    void getMaxNumberOfChildrenInTheFamily() {
        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        assertEquals(4, babysitter.getMaxNumberOfChildrenInTheFamily());
    }

    @Test
    void testToString() {
        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        log.config(babysitter.toString());
    }
}