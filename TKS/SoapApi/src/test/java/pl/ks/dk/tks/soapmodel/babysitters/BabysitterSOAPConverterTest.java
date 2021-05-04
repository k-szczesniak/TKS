package pl.ks.dk.tks.soapmodel.babysitters;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.babysitters.TeachingSitter;
import pl.ks.dk.tks.domainmodel.babysitters.TidingSitter;

import static org.junit.jupiter.api.Assertions.*;

class BabysitterSOAPConverterTest {
    @Test
    void convertBabysitterToBabysitterSOAPTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        TeachingSitter teachingSitter = new TeachingSitter("Anna2", "Kowalska2", 20.0, 4, 5, 5);
        TidingSitter tidingSitter = new TidingSitter("Anna3", "Kowalska3", 20.0, 4, 5, 500.0);

        BabysitterSOAP babysitterSOAP = BabysitterSOAPConverter.convertBabysitterToBabysitterSOAP(babysitter);
        BabysitterSOAP babysitterSOAP1 = BabysitterSOAPConverter.convertBabysitterToBabysitterSOAP(teachingSitter);
        BabysitterSOAP babysitterSOAP2 = BabysitterSOAPConverter.convertBabysitterToBabysitterSOAP(tidingSitter);

        assertEquals(babysitterSOAP.getName(), babysitter.getName());
        assertEquals(babysitterSOAP.getSurname(), babysitter.getSurname());

        assertEquals(babysitterSOAP1.getName(), teachingSitter.getName());
        assertEquals(babysitterSOAP1.getSurname(), teachingSitter.getSurname());

        assertEquals(babysitterSOAP2.getName(), tidingSitter.getName());
        assertEquals(babysitterSOAP2.getSurname(), tidingSitter.getSurname());
    }

    @Test
    void convertBabysitterSOAPToBabysitterTest() {
        BabysitterSOAP babysitterSOAP = new BabysitterSOAP("Anna", "Kowalska", 20.0, 5, 6);
        TeachingSitterSOAP teachingSitterSOAP = new TeachingSitterSOAP("Anna2", "Kowalska2", 20.0, 4, 5, 5);
        TidingSitterSOAP tidingSitterSOAP = new TidingSitterSOAP("Anna3", "Kowalska3", 20.0, 4, 5, 500.0);

        Babysitter babysitter = BabysitterSOAPConverter.convertBabysitterSOAPToBabysitter(babysitterSOAP);
        Babysitter teachingSitter = BabysitterSOAPConverter.convertBabysitterSOAPToBabysitter(teachingSitterSOAP);
        Babysitter tidingSitter = BabysitterSOAPConverter.convertBabysitterSOAPToBabysitter(tidingSitterSOAP);

        assertEquals(babysitterSOAP.getName(), babysitter.getName());
        assertEquals(babysitterSOAP.getSurname(), babysitter.getSurname());

        assertEquals(teachingSitterSOAP.getName(), teachingSitter.getName());
        assertEquals(teachingSitterSOAP.getSurname(), teachingSitter.getSurname());

        assertEquals(tidingSitterSOAP.getName(), tidingSitter.getName());
        assertEquals(tidingSitterSOAP.getSurname(), tidingSitter.getSurname());
    }
}