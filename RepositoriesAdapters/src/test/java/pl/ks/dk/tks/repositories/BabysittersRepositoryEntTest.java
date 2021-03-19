package pl.ks.dk.tks.repositories;


import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.babysitters.TeachingSitterEnt;
import pl.ks.dk.tks.model.babysitters.TidingSitterEnt;
import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class BabysittersRepositoryEntTest {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Test
    void getNumberOfElements() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        assertEquals(0, babysittersRepository.getElements().size());
        assertEquals(babysittersRepository.getElements().size(),
                babysittersRepository.getNumberOfElements());

        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20, 3, 4, 13);
        babysittersRepository.addElement(babysitter);

        assertEquals(1, babysittersRepository.getElements().size());
        assertEquals(babysittersRepository.getElements().size(),
                babysittersRepository.getNumberOfElements());
    }

    @Test
    void checkIfTheElementIsPresent() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new TidingSitterEnt("Ola", "Nowak", 20, 3, 4, 560);
        babysittersRepository.addElement(babysitter);

        assertTrue(babysittersRepository.checkIfTheElementIsPresent(babysitter));
    }

    @Test
    void addElement() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        assertEquals(0, babysittersRepository.getNumberOfElements());

        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        babysittersRepository.addElement(babysitter);

        assertEquals(1, babysittersRepository.getNumberOfElements());
    }

    @Test
    void deleteElement() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20, 3, 4, 13);
        babysittersRepository.addElement(babysitter);

        assertEquals(1, babysittersRepository.getNumberOfElements());

        babysittersRepository.deleteElement(babysitter);

        assertEquals(0, babysittersRepository.getNumberOfElements());
    }

    @Test
    void getElements() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        babysittersRepository.addElement(babysitter);

        List<BabysitterEnt> babysitterList = new ArrayList<>();
        babysitterList.add(babysitter);

        assertEquals(babysitterList, babysittersRepository.getElements());
    }

    @Test
    void testToString() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter1 = new BabysitterEnt("Ola", "Nowak", 20, 3, 4);
        babysittersRepository.addElement(babysitter1);

        BabysitterEnt babysitter2 = new TeachingSitterEnt("Anna", "Kowalska", 15, 5, 2, 5);
        babysittersRepository.addElement(babysitter2);

        BabysitterEnt babysitter3 = new TidingSitterEnt("Kasia", "Parkowska", 11, 10, 2, 560);
        babysittersRepository.addElement(babysitter3);

        log.config(babysittersRepository.toString());
    }

    @Test
    void tryingToAddAddedElement() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20, 3, 4, 13);
        babysittersRepository.addElement(babysitter);

        assertThrows(RepositoryExceptionEnt.class, () -> babysittersRepository.addElement(babysitter));
    }

    @Test
    void tryingToDeleteDeletedElement() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20, 3, 4, 13);
        babysittersRepository.addElement(babysitter);
        babysittersRepository.deleteElement(babysitter);

        assertThrows(RepositoryExceptionEnt.class, () -> babysittersRepository.deleteElement(babysitter));
    }
}