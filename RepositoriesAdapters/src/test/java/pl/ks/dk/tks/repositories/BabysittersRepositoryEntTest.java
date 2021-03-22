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
    void getNumberOfElementsTest() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        assertEquals(0, babysittersRepository.getElements().size());
        assertEquals(babysittersRepository.getElements().size(),
                babysittersRepository.getNumberOfElements());

        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 13);
        babysittersRepository.addElement(babysitter);

        assertEquals(1, babysittersRepository.getElements().size());
        assertEquals(babysittersRepository.getElements().size(),
                babysittersRepository.getNumberOfElements());
    }

    @Test
    void checkIfTheElementIsPresentTest() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new TidingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 560);
        babysittersRepository.addElement(babysitter);

        assertTrue(babysittersRepository.checkIfTheElementIsPresent(babysitter));
    }

    @Test
    void addElementTest() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        assertEquals(0, babysittersRepository.getNumberOfElements());

        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20.0, 3, 4);
        babysittersRepository.addElement(babysitter);

        assertEquals(1, babysittersRepository.getNumberOfElements());
    }

    @Test
    void deleteElementTest() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 13);
        babysittersRepository.addElement(babysitter);

        assertEquals(1, babysittersRepository.getNumberOfElements());

        babysittersRepository.deleteElement(babysitter);

        assertEquals(0, babysittersRepository.getNumberOfElements());
    }

    @Test
    void getBabysittersListTest() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20.0, 3, 4);
        babysittersRepository.addElement(babysitter);

        List<BabysitterEnt> babysitterList = new ArrayList<>();
        babysitterList.add(babysitter);

        assertEquals(babysitterList, babysittersRepository.getBabysittersList());
    }

    @Test
    void tryingToAddAddedElementTest() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 13);
        babysittersRepository.addElement(babysitter);

        assertThrows(RepositoryExceptionEnt.class, () -> babysittersRepository.addElement(babysitter));
    }

    @Test
    void tryingToDeleteDeletedElementTest() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();

        BabysitterEnt babysitter = new TeachingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 13);
        babysittersRepository.addElement(babysitter);
        babysittersRepository.deleteElement(babysitter);

        assertThrows(RepositoryExceptionEnt.class, () -> babysittersRepository.deleteElement(babysitter));
    }

    @Test
    void updateElementTest() {
        BabysittersRepositoryEnt babysittersRepositoryEnt = new BabysittersRepositoryEnt();
        BabysitterEnt babysitter = new BabysitterEnt("Ola", "Nowak", 20.0, 3, 4);

        babysittersRepositoryEnt.addElement(babysitter);
        assertEquals("Ola", babysittersRepositoryEnt.getElements().get(0).getName());

        BabysitterEnt updatedBabysitter = new BabysitterEnt("Ewa", "Nowak", 20.0, 3, 4);
        babysittersRepositoryEnt.updateElement(updatedBabysitter, babysittersRepositoryEnt.getElements().get(0).getUuid());
        assertEquals("Ewa", babysittersRepositoryEnt.getElements().get(0).getName());

    }

    @Test
    void findByKeyTest() {
        BabysittersRepositoryEnt babysittersRepository = new BabysittersRepositoryEnt();
        BabysitterEnt babysitter = new TidingSitterEnt("Ola", "Nowak", 20.0, 3, 4, 560);
        babysittersRepository.addElement(babysitter);

        assertEquals(babysitter, babysittersRepository.findByKey(babysitter.getUuid()));
        assertThrows(RepositoryExceptionEnt.class, () -> babysittersRepository.findByKey("zleUuid"));
    }


}