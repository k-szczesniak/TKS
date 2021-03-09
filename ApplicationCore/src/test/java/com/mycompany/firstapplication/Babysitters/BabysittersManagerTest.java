package com.mycompany.firstapplication.Babysitters;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BabysittersManagerTest {

    @Test
    void addBabysitter() {
        BabysittersManager babysittersManager = new BabysittersManager(new BabysittersRepository());

        assertEquals(0, babysittersManager.getNumberOfBabysitters());

        Babysitter babysitter = new TeachingSitter("Ola", "Nowak", 20, 3, 4, 12);
        babysittersManager.addBabysitter(babysitter);

        assertEquals(1, babysittersManager.getNumberOfBabysitters());
    }

    @Test
    void deleteBabysitter() {
        BabysittersManager babysittersManager = new BabysittersManager(new BabysittersRepository());

        Babysitter babysitter = new TidingSitter("Ola", "Nowak", 20, 3, 4, 500);
        babysittersManager.addBabysitter(babysitter);

        assertEquals(1, babysittersManager.getNumberOfBabysitters());

        babysittersManager.deleteBabysitter(babysitter);

        assertEquals(0, babysittersManager.getNumberOfBabysitters());
    }

    @Test
    void getNumberOfBabysitters() {
        BabysittersManager babysittersManager = new BabysittersManager(new BabysittersRepository());

        Babysitter babysitter = new TeachingSitter("Ola", "Nowak", 20, 3, 4, 11);
        babysittersManager.addBabysitter(babysitter);

        assertEquals(1, babysittersManager.getNumberOfBabysitters());
    }

    @Test
    void getListWithAppropriateBabysitters_AgeCase() {
        BabysittersManager babysittersManager = new BabysittersManager(new BabysittersRepository());

        Babysitter babysitter1 = new TeachingSitter("Ola", "Nowak", 20, 5, 3, 12);
        babysittersManager.addBabysitter(babysitter1);

        Babysitter babysitter2 = new TidingSitter("Ania", "Nowak", 20, 6, 4, 450);
        babysittersManager.addBabysitter(babysitter2);

        Babysitter babysitter3 = new Babysitter("Kasia", "Nowak", 20, 9, 4);
        babysittersManager.addBabysitter(babysitter3);

        List<Babysitter> babysitterList = new ArrayList<>();
        babysitterList.add(babysitter1);
        babysitterList.add(babysitter2);

        assertEquals(babysitterList, babysittersManager.getListWithAppropriateBabysitters(6, 3));
    }

    @Test
    void getListWithAppropriateBabysitters_NumberOFChildrenCase() {
        BabysittersManager babysittersManager = new BabysittersManager(new BabysittersRepository());

        Babysitter babysitter1 = new TeachingSitter("Ola", "Nowak", 20, 5, 3, 12);
        babysittersManager.addBabysitter(babysitter1);

        Babysitter babysitter2 = new TidingSitter("Ania", "Nowak", 20, 6, 4, 450);
        babysittersManager.addBabysitter(babysitter2);

        Babysitter babysitter3 = new Babysitter("Kasia", "Nowak", 20, 7, 5);
        babysittersManager.addBabysitter(babysitter3);

        List<Babysitter> babysitterList = new ArrayList<>();
        babysitterList.add(babysitter3);

        assertEquals(babysitterList, babysittersManager.getListWithAppropriateBabysitters(7, 5));
    }

    @Test
    void getListWithAppropriateBabysitters_BothParametersCase() {
        BabysittersManager babysittersManager = new BabysittersManager(new BabysittersRepository());

        Babysitter babysitter1 = new TeachingSitter("Ola", "Nowak", 20, 4, 3, 12);
        babysittersManager.addBabysitter(babysitter1);

        Babysitter babysitter2 = new TidingSitter("Ania", "Nowak", 20, 4, 5, 450);
        babysittersManager.addBabysitter(babysitter2);

        Babysitter babysitter3 = new Babysitter("Kasia", "Nowak", 20, 7, 4);
        babysittersManager.addBabysitter(babysitter3);

        List<Babysitter> babysitterList = new ArrayList<>();
        babysitterList.add(babysitter2);

        assertEquals(babysitterList, babysittersManager.getListWithAppropriateBabysitters(4, 5));
    }
}