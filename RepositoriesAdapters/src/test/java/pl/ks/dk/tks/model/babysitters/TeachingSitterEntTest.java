package pl.ks.dk.tks.model.babysitters;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeachingSitterEntTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void getYearsOfExperienceInTeaching() {
        TeachingSitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 13);
        assertEquals(13, babysitter.getYearsOfExperienceInTeaching());
    }

    @Test
    void priceForHour() {
        TeachingSitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 13);
        double priceForHour = 20 * (1 + babysitter.getYearsOfExperienceInTeaching() / 10.0);

        assertEquals(priceForHour, babysitter.getBasePriceForHour());
    }

    @Test
    void testToString() {
        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 13);
        log.config(babysitter.toString());
    }
}