package pl.ks.dk.tks.model.babysitters;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TidingSitterEntTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void getValueOfCleaningEquipment() {
        TidingSitterEnt babysitter = new TidingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 450);

        assertEquals(450, babysitter.getValueOfCleaningEquipment());
    }

    @Test
    void priceForHour() {
        TidingSitterEnt babysitter = new TidingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 450);

        Double priceForHour = 20 * (1 + babysitter.getValueOfCleaningEquipment() / 500.0);
        assertEquals(priceForHour, babysitter.getBasePriceForHour());
    }

    @Test
    void testToString() {
        BabysitterEnt babysitter = new TidingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 450);
        log.config(babysitter.toString());
    }
}