package com.mycompany.firstapplication.Babysitters;

import com.mycompany.firstapplication.services.BabysittersService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BabysittersServiceTest {

    @Test
    void addBabysitter() {
        BabysittersService babysittersService = new BabysittersService(new BabysittersRepository());

        assertEquals(0, babysittersService.getNumberOfBabysitters());

        Babysitter babysitter = new TeachingSitter("Ola", "Nowak", 20, 3, 4, 12);
        babysittersService.addBabysitter(babysitter);

        assertEquals(1, babysittersService.getNumberOfBabysitters());
    }

    @Test
    void deleteBabysitter() {
        BabysittersService babysittersService = new BabysittersService(new BabysittersRepository());

        Babysitter babysitter = new TidingSitter("Ola", "Nowak", 20, 3, 4, 500);
        babysittersService.addBabysitter(babysitter);

        assertEquals(1, babysittersService.getNumberOfBabysitters());

        babysittersService.deleteBabysitter(babysitter);

        assertEquals(0, babysittersService.getNumberOfBabysitters());
    }

    @Test
    void getNumberOfBabysitters() {
        BabysittersService babysittersService = new BabysittersService(new BabysittersRepository());

        Babysitter babysitter = new TeachingSitter("Ola", "Nowak", 20, 3, 4, 11);
        babysittersService.addBabysitter(babysitter);

        assertEquals(1, babysittersService.getNumberOfBabysitters());
    }

    @Test
    void getListWithAppropriateBabysitters_AgeCase() {
        BabysittersService babysittersService = new BabysittersService(new BabysittersRepository());

        Babysitter babysitter1 = new TeachingSitter("Ola", "Nowak", 20, 5, 3, 12);
        babysittersService.addBabysitter(babysitter1);

        Babysitter babysitter2 = new TidingSitter("Ania", "Nowak", 20, 6, 4, 450);
        babysittersService.addBabysitter(babysitter2);

        Babysitter babysitter3 = new Babysitter("Kasia", "Nowak", 20, 9, 4);
        babysittersService.addBabysitter(babysitter3);

        List<Babysitter> babysitterList = new ArrayList<>();
        babysitterList.add(babysitter1);
        babysitterList.add(babysitter2);

        assertEquals(babysitterList, babysittersService.getListWithAppropriateBabysitters(6, 3));
    }

    @Test
    void getListWithAppropriateBabysitters_NumberOFChildrenCase() {
        BabysittersService babysittersService = new BabysittersService(new BabysittersRepository());

        Babysitter babysitter1 = new TeachingSitter("Ola", "Nowak", 20, 5, 3, 12);
        babysittersService.addBabysitter(babysitter1);

        Babysitter babysitter2 = new TidingSitter("Ania", "Nowak", 20, 6, 4, 450);
        babysittersService.addBabysitter(babysitter2);

        Babysitter babysitter3 = new Babysitter("Kasia", "Nowak", 20, 7, 5);
        babysittersService.addBabysitter(babysitter3);

        List<Babysitter> babysitterList = new ArrayList<>();
        babysitterList.add(babysitter3);

        assertEquals(babysitterList, babysittersService.getListWithAppropriateBabysitters(7, 5));
    }

    @Test
    void getListWithAppropriateBabysitters_BothParametersCase() {
        BabysittersService babysittersService = new BabysittersService(new BabysittersRepository());

        Babysitter babysitter1 = new TeachingSitter("Ola", "Nowak", 20, 4, 3, 12);
        babysittersService.addBabysitter(babysitter1);

        Babysitter babysitter2 = new TidingSitter("Ania", "Nowak", 20, 4, 5, 450);
        babysittersService.addBabysitter(babysitter2);

        Babysitter babysitter3 = new Babysitter("Kasia", "Nowak", 20, 7, 4);
        babysittersService.addBabysitter(babysitter3);

        List<Babysitter> babysitterList = new ArrayList<>();
        babysitterList.add(babysitter2);

        assertEquals(babysitterList, babysittersService.getListWithAppropriateBabysitters(4, 5));
    }
}