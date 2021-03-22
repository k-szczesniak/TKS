package pl.ks.dk.tks.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.employments.EmploymentEnt;
import pl.ks.dk.tks.model.users.ClientEnt;
import pl.ks.dk.tks.model.users.UserEnt;
import pl.ks.dk.tks.repositories.EmploymentsRepositoryEnt;
import pl.ks.dk.tks.repositories.UsersRepositoryEnt;
import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmploymentAdapterTest {

    @Mock
    EmploymentsRepositoryEnt employmentsRepositoryEnt;

    @InjectMocks
    EmploymentAdapter employmentAdapter;


    @Test
    void addEmploymentPositiveTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        Client client = new Client("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);

        employmentAdapter.addEmployment(client, babysitter);
        verify(employmentsRepositoryEnt).addElement(ArgumentMatchers.any(EmploymentEnt.class));
    }

    @Test
    void addEmploymentNegativeTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        Client client = new Client("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);

        doThrow(RepositoryExceptionEnt.class).when(employmentsRepositoryEnt).addElement(ArgumentMatchers.any(EmploymentEnt.class));
        assertThrows(AdapterException.class, () -> employmentAdapter.addEmployment(client, babysitter));
    }

    @Test
    void getActualEmploymentsForClientTest() {
        given(employmentsRepositoryEnt.getElements()).willReturn(prepareMockData());
        assertEquals(employmentAdapter.getActualEmploymentsForClient("12345678").size(), 1);
        verify(employmentsRepositoryEnt).getElements();
    }

    @Test
    void convertEmploymentToEmploymentEntTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        Client client = new Client("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);
        Employment employment = new Employment(babysitter, client);

        EmploymentEnt employmentEnt = EmploymentAdapter.convertEmploymentToEmploymentEnt(employment);

        assertEquals(employmentEnt.getBabysitter().getName(), babysitter.getName());
        assertEquals(employmentEnt.getBabysitter().getSurname(), babysitter.getSurname());
        assertEquals(employmentEnt.getClient().getLogin(), client.getLogin());
    }

    @Test
    void convertEmploymentEntToEmploymentTest() {
        BabysitterEnt babysitterEnt = new BabysitterEnt("Anna", "Kowalska", 20.0, 5, 6);
        ClientEnt clientEnt = new ClientEnt("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);
        EmploymentEnt employmentEnt = new EmploymentEnt(babysitterEnt, clientEnt);

        Employment employment = EmploymentAdapter.convertEmploymentEntToEmployment(employmentEnt);

        assertEquals(employmentEnt.getBabysitter().getName(), babysitterEnt.getName());
        assertEquals(employmentEnt.getBabysitter().getSurname(), babysitterEnt.getSurname());
        assertEquals(employmentEnt.getClient().getLogin(), clientEnt.getLogin());
    }

    private List<EmploymentEnt> prepareMockData() {
        List<EmploymentEnt> employmentsEntList = new ArrayList<>();
        BabysitterEnt babysitterEnt = new BabysitterEnt("Anna", "Kowalska", 20.0, 5, 6);
        ClientEnt clientEnt = new ClientEnt("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);
        clientEnt.setUuid("12345678");

        EmploymentEnt employment = new EmploymentEnt(babysitterEnt, clientEnt);
        employmentsEntList.add(employment);
        return employmentsEntList;
    }
}