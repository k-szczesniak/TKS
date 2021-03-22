package pl.ks.dk.tks.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.infrastructure.babysitters.AddBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.DeleteBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.GetBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.UpdateBabysitterPort;
import pl.ks.dk.tks.services.exceptions.ServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BabysittersServiceTest {

    @InjectMocks
    BabysittersService babysittersService;

    @Mock
    private GetBabysitterPort getBabysitterPort;

    @Mock
    private AddBabysitterPort addBabysitterPort;

    @Mock
    private UpdateBabysitterPort updateBabysitterPort;

    @Mock
    private DeleteBabysitterPort deleteBabysitterPort;


    @Test
    void getBabysitterByKey() {
        given(getBabysitterPort.getBabysitter(ArgumentMatchers.anyString())).willThrow(
                AdapterException.class);
        given(getBabysitterPort.getBabysitter(ArgumentMatchers.eq("12345678")))
                .willReturn(new Babysitter("Anna", "Kowalska", 20.0, 5, 6));

        assertEquals("Anna", babysittersService.getBabysitterByKey("12345678").getName());
        assertThrows(ServiceException.class, () -> babysittersService.getBabysitterByKey("OtherUuid"));
        verify(getBabysitterPort, times(2)).getBabysitter(ArgumentMatchers.anyString());
    }

    @Test
    void getAllBabysitters() {
        given(getBabysitterPort.getAllBabysitters()).willReturn(prepareMockData());

        List<Babysitter> babysitters = babysittersService.getAllBabysitters();
        assertEquals(1, babysitters.size());
        verify(getBabysitterPort, times(1)).getAllBabysitters();
    }

    @Test
    void addBabysitterPositiveTest() {
        babysittersService.addBabysitter(new Babysitter("Anna", "Kowalska", 20.0, 5, 6));
        verify(addBabysitterPort).addBabysitter(ArgumentMatchers.any(Babysitter.class));
    }

    @Test
    void addBabysitterNegativeTest() {
        doThrow(AdapterException.class).when(addBabysitterPort).addBabysitter(ArgumentMatchers.any(Babysitter.class));
        assertThrows(ServiceException.class,
                () -> babysittersService.addBabysitter(new Babysitter("Anna", "Kowalska", 20.0, 5, 6)));
    }

    @Test
    void updateBabysitterPositiveTest() {
        babysittersService.updateBabysitter(new Babysitter("Anna", "Kowalska", 20.0, 5, 6), "12345678");
        verify(updateBabysitterPort)
                .updateBabysitter(ArgumentMatchers.any(Babysitter.class), ArgumentMatchers.anyString());
    }

    @Test
    void updateBabysitterNegativeTest() {
        doThrow(AdapterException.class).when(updateBabysitterPort)
                .updateBabysitter(ArgumentMatchers.any(Babysitter.class), ArgumentMatchers.anyString());
        assertThrows(ServiceException.class,
                () -> babysittersService.updateBabysitter(new Babysitter("Anna", "Kowalska", 20.0, 5, 6), "12345678"));
    }

    @Test
    void deleteBabysitterPositiveTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        babysitter.setEmployed(false);
        babysitter.setUuid("12345678");
        given(babysittersService.getBabysitterByKey(ArgumentMatchers.anyString())).willReturn(babysitter);

        babysittersService.deleteBabysitter("12345678");
        verify(deleteBabysitterPort).deleteBabysitter(ArgumentMatchers.anyString());
    }

    @Test
    void deleteBabysitterNegativeTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        babysitter.setEmployed(false);
        babysitter.setUuid("12345678");
        given(babysittersService.getBabysitterByKey(ArgumentMatchers.anyString())).willReturn(babysitter);

        doThrow(AdapterException.class).when(deleteBabysitterPort).deleteBabysitter(ArgumentMatchers.anyString());
        assertThrows(ServiceException.class, () -> babysittersService.deleteBabysitter("12345678"));
    }

    private List<Babysitter> prepareMockData() {
        List<Babysitter> babysitterList = new ArrayList<>();
        babysitterList.add(new Babysitter("Anna", "Kowalska", 20.0, 5, 6));
        return babysitterList;
    }
}