package pl.ks.dk.tks.model.babysitters;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.model.exceptions.BabysitterExceptionEnt;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TidingSitterTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void constructor() {
        assertThrows(BabysitterExceptionEnt.class, () -> new TidingSitterEnt("Ola", "Nowak", -3, 3, 4, -5));
    }

    @Test
    void getValueOfCleaningEquipment() {
        TidingSitterEnt babysitter = new TidingSitterEnt("Ola", "Nowak", 20, 3, 4, 450);

        assertEquals(450, babysitter.getValueOfCleaningEquipment());
    }

    @Test
    void priceForHour() {
        TidingSitterEnt babysitter = new TidingSitterEnt("Ola", "Nowak", 20, 3, 4, 450);
        double priceForHour = babysitter.getBasePriceForHour() * (1 + babysitter.getValueOfCleaningEquipment() / 500.0);

        assertEquals(priceForHour, babysitter.priceForHour());
    }

    @Test
    void testToString() {
        BabysitterEnt babysitter = new TidingSitterEnt("Ola", "Nowak", 20, 3, 4, 450);
        log.config(babysitter.toString());
    }
}