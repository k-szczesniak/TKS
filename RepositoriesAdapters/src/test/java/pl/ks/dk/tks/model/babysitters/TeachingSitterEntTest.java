package pl.ks.dk.tks.model.babysitters;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.model.exceptions.BabysitterExceptionEnt;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeachingSitterEntTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void constructor() {
        assertThrows(BabysitterExceptionEnt.class, () -> new TeachingSitterEnt("Ola", "Nowak", -3, 3, 4, -5));
    }

    @Test
    void getYearsOfExperienceInTeaching() {
        TeachingSitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20, 3, 4, 13);
        assertEquals(13, babysitter.getYearsOfExperienceInTeaching());
    }

    @Test
    void priceForHour() {
        TeachingSitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20, 3, 4, 13);
        double priceForHour = babysitter.getBasePriceForHour() *
                (1 + babysitter.getYearsOfExperienceInTeaching() / 10.0);

        assertEquals(priceForHour, babysitter.priceForHour());
    }

    @Test
    void testToString() {
        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20, 3, 4, 13);
        log.config(babysitter.toString());
    }
}