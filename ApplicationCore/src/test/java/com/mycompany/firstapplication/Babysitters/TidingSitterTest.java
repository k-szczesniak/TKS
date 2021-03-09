package com.mycompany.firstapplication.Babysitters;

import com.mycompany.firstapplication.Exceptions.BabysitterException;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TidingSitterTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void constructor() {
        assertThrows(BabysitterException.class, () -> new TidingSitter("Ola", "Nowak", -3, 3, 4, -5));
    }

    @Test
    void getValueOfCleaningEquipment() {
        TidingSitter babysitter = new TidingSitter("Ola", "Nowak", 20, 3, 4, 450);

        assertEquals(450, babysitter.getValueOfCleaningEquipment());
    }

    @Test
    void priceForHour() {
        TidingSitter babysitter = new TidingSitter("Ola", "Nowak", 20, 3, 4, 450);
        double priceForHour = babysitter.getBasePriceForHour() * (1 + babysitter.getValueOfCleaningEquipment() / 500.0);

        assertEquals(priceForHour, babysitter.priceForHour());
    }

    @Test
    void testToString() {
        Babysitter babysitter = new TidingSitter("Ola", "Nowak", 20, 3, 4, 450);
        log.config(babysitter.toString());
    }
}