package pl.ks.dk.tks.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.infrastructure.employments.AddEmploymentPort;
import pl.ks.dk.tks.infrastructure.employments.GetEmploymentPort;
import pl.ks.dk.tks.services.exceptions.ServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmploymentsServiceTest {

    @InjectMocks
    EmploymentsService employmentsService;

    @Mock
    private AddEmploymentPort addEmploymentPort;

    @Mock
    private GetEmploymentPort getEmploymentPort;

    @Test
    void getActualEmploymentsForClient() {
        given(getEmploymentPort.getActualEmploymentsForClient("12345678")).willReturn(prepareMockData());
        assertEquals(employmentsService.getActualEmploymentsForClient("12345678").size(), 1);
        verify(getEmploymentPort).getActualEmploymentsForClient("12345678");
    }

    @Test
    void employPositiveTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 3, 6);
        Client client = new Client("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);

        employmentsService.employ(client, babysitter);
        verify(addEmploymentPort)
                .addEmployment(ArgumentMatchers.any(Client.class), ArgumentMatchers.any(Babysitter.class));
    }

    @Test
    void employNegativeCase1Test() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 3, 6);
        Client client = new Client("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);

        doThrow(AdapterException.class).when(addEmploymentPort)
                .addEmployment(ArgumentMatchers.any(Client.class), ArgumentMatchers.any(Babysitter.class));
        assertThrows(ServiceException.class, () -> employmentsService.employ(client, babysitter));
    }

    @Test
    void employNegativeCase2Test() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        Client client = new Client("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);

        assertThrows(ServiceException.class, () -> employmentsService.employ(client, babysitter));
    }

    private List<Employment> prepareMockData() {
        List<Employment> employmentsEntList = new ArrayList<>();
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 3, 6);
        Client client = new Client("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);
        client.setUuid("12345678");

        Employment employment = new Employment(babysitter, client);
        employmentsEntList.add(employment);
        return employmentsEntList;
    }
}