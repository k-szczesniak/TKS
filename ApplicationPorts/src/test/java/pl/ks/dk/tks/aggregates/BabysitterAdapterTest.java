package pl.ks.dk.tks.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.babysitters.TeachingSitter;
import pl.ks.dk.tks.domainmodel.babysitters.TidingSitter;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.babysitters.TeachingSitterEnt;
import pl.ks.dk.tks.model.babysitters.TidingSitterEnt;
import pl.ks.dk.tks.repositories.BabysittersRepositoryEnt;
import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BabysitterAdapterTest {

    @Mock
    BabysittersRepositoryEnt babysittersRepositoryEnt;

    @InjectMocks
    BabysitterAdapter babysitterAdapter;

    @Test
    void addBabysitterPositiveTest() {
        babysitterAdapter.addBabysitter(new Babysitter("Anna", "Kowalska", 20.0, 5, 6));
        verify(babysittersRepositoryEnt).addElement(ArgumentMatchers.any(BabysitterEnt.class));
    }

    @Test
    void addBabysitterNegativeTest() {
        doThrow(RepositoryExceptionEnt.class).when(babysittersRepositoryEnt).addElement(ArgumentMatchers.any(BabysitterEnt.class));
        assertThrows(AdapterException.class, () -> babysitterAdapter.addBabysitter(new Babysitter("Anna", "Kowalska", 20.0, 5, 6)));
    }

    @Test
    void getBabysitterTest() {
        given(babysittersRepositoryEnt.findByKey(ArgumentMatchers.anyString())).willThrow(RepositoryExceptionEnt.class);
        given(babysittersRepositoryEnt.findByKey(ArgumentMatchers.eq("12345678"))).willReturn(new BabysitterEnt("Anna", "Kowalska", 20.0, 5, 6));

        assertEquals("Anna", babysitterAdapter.getBabysitter("12345678").getName());
        assertThrows(AdapterException.class, () -> babysitterAdapter.getBabysitter("OtherUuid"));
        verify(babysittersRepositoryEnt, times(2)).findByKey(ArgumentMatchers.anyString());
    }

    @Test
    void updateBabysitterPositiveTest() {
        babysitterAdapter.updateBabysitter(new Babysitter("Anna", "Kowalska", 20.0, 5, 6), "12345678");
        verify(babysittersRepositoryEnt).updateElement(ArgumentMatchers.any(BabysitterEnt.class), ArgumentMatchers.anyString());
    }

    @Test
    void updateBabysitterNegativeTest() {
        doThrow(RepositoryExceptionEnt.class).when(babysittersRepositoryEnt).updateElement(ArgumentMatchers.any(BabysitterEnt.class), ArgumentMatchers.anyString());
        assertThrows(AdapterException.class, () -> babysitterAdapter.updateBabysitter(new Babysitter("Anna", "Kowalska", 20.0, 5, 6), "12345678"));
    }

    @Test
    void deleteBabysitterPositiveTest() {
        babysitterAdapter.deleteBabysitter("12345678");
        verify(babysittersRepositoryEnt).deleteElement(ArgumentMatchers.anyString());
    }

    @Test
    void deleteBabysitterNegativeTest() {
        doThrow(RepositoryExceptionEnt.class).when(babysittersRepositoryEnt).deleteElement(ArgumentMatchers.anyString());
        assertThrows(AdapterException.class, () -> babysitterAdapter.deleteBabysitter("12345678"));
    }

    @Test
    void getAllBabysittersTest() {
        given(babysittersRepositoryEnt.getBabysittersList()).willReturn(prepareMockData());

        List<Babysitter> babysitters = babysitterAdapter.getAllBabysitters();
        assertEquals(1, babysitters.size());
        verify(babysittersRepositoryEnt, times(1)).getBabysittersList();
    }

    @Test
    void convertBabysitterToBabysitterEntTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        TeachingSitter teachingSitter = new TeachingSitter("Anna2", "Kowalska2", 20.0, 4, 5, 5);
        TidingSitter tidingSitter = new TidingSitter("Anna3", "Kowalska3", 20.0, 4, 5, 500.0);

        BabysitterEnt babysitterEnt = BabysitterAdapter.convertBabysitterToBabysitterEnt(babysitter);
        BabysitterEnt babysitterEnt1 = BabysitterAdapter.convertBabysitterToBabysitterEnt(teachingSitter);
        BabysitterEnt babysitterEnt2 = BabysitterAdapter.convertBabysitterToBabysitterEnt(tidingSitter);

        assertEquals(babysitterEnt.getName(), babysitter.getName());
        assertEquals(babysitterEnt.getSurname(), babysitter.getSurname());

        assertEquals(babysitterEnt1.getName(), teachingSitter.getName());
        assertEquals(babysitterEnt1.getSurname(), teachingSitter.getSurname());

        assertEquals(babysitterEnt2.getName(), tidingSitter.getName());
        assertEquals(babysitterEnt2.getSurname(), tidingSitter.getSurname());
    }

    @Test
    void convertBabysitterEntToBabysitterTest() {
        BabysitterEnt babysitterEnt = new BabysitterEnt("Anna", "Kowalska", 20.0, 5, 6);
        TeachingSitterEnt teachingSitterEnt = new TeachingSitterEnt("Anna2", "Kowalska2", 20.0, 4, 5, 5);
        TidingSitterEnt tidingSitterEnt = new TidingSitterEnt("Anna3", "Kowalska3", 20.0, 4, 5, 500.0);

        Babysitter babysitter = BabysitterAdapter.convertBabysitterEntToBabysitter(babysitterEnt);
        Babysitter teachingSitter = BabysitterAdapter.convertBabysitterEntToBabysitter(teachingSitterEnt);
        Babysitter tidingSitter = BabysitterAdapter.convertBabysitterEntToBabysitter(tidingSitterEnt);

        assertEquals(babysitterEnt.getName(), babysitter.getName());
        assertEquals(babysitterEnt.getSurname(), babysitter.getSurname());

        assertEquals(teachingSitterEnt.getName(), teachingSitter.getName());
        assertEquals(teachingSitterEnt.getSurname(), teachingSitter.getSurname());

        assertEquals(tidingSitterEnt.getName(), tidingSitter.getName());
        assertEquals(tidingSitterEnt.getSurname(), tidingSitter.getSurname());
    }

    private List<BabysitterEnt> prepareMockData() {
        List<BabysitterEnt> babysitterEntList = new ArrayList<>();
        babysitterEntList.add(new BabysitterEnt("Anna", "Kowalska", 20.0, 5, 6));
        return babysitterEntList;
    }
}