package com.mycompany.firstapplication.Babysitters;

import com.mycompany.firstapplication.Exceptions.BabysitterException;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeachingSitterTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void constructor() {
        assertThrows(BabysitterException.class, () -> new TeachingSitter("Ola", "Nowak", -3, 3, 4, -5));
    }

    @Test
    void getYearsOfExperienceInTeaching() {
        TeachingSitter babysitter = new TeachingSitter("Ola", "Nowak", 20, 3, 4, 13);
        assertEquals(13, babysitter.getYearsOfExperienceInTeaching());
    }

    @Test
    void priceForHour() {
        TeachingSitter babysitter = new TeachingSitter("Ola", "Nowak", 20, 3, 4, 13);
        double priceForHour = babysitter.getBasePriceForHour() *
                (1 + babysitter.getYearsOfExperienceInTeaching() / 10.0);

        assertEquals(priceForHour, babysitter.priceForHour());
    }

    @Test
    void testToString() {
        Babysitter babysitter = new TeachingSitter("Ola", "Nowak", 20, 3, 4, 13);
        log.config(babysitter.toString());
    }
}