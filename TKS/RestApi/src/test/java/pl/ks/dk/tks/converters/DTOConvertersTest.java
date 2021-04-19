package pl.ks.dk.tks.converters;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.babysitters.TeachingSitter;
import pl.ks.dk.tks.domainmodel.babysitters.TidingSitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.dtomodel.babysitters.BabysitterDTO;
import pl.ks.dk.tks.dtomodel.babysitters.TeachingSitterDTO;
import pl.ks.dk.tks.dtomodel.babysitters.TidingSitterDTO;
import pl.ks.dk.tks.dtomodel.employments.EmploymentDTO;
import pl.ks.dk.tks.dtomodel.users.AdminDTO;
import pl.ks.dk.tks.dtomodel.users.ClientDTO;
import pl.ks.dk.tks.dtomodel.users.SuperUserDTO;
import pl.ks.dk.tks.dtomodel.users.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DTOConvertersTest {

    @Test
    void convertBabysitterToBabysitterDTOTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        TeachingSitter teachingSitter = new TeachingSitter("Anna2", "Kowalska2", 20.0, 4, 5, 5);
        TidingSitter tidingSitter = new TidingSitter("Anna3", "Kowalska3", 20.0, 4, 5, 500.0);

        BabysitterDTO babysitterDTO = BabysitterDTOConverter.convertBabysitterToBabysitterDTO(babysitter);
        BabysitterDTO babysitterDTO1 = BabysitterDTOConverter.convertBabysitterToBabysitterDTO(teachingSitter);
        BabysitterDTO babysitterDTO2 = BabysitterDTOConverter.convertBabysitterToBabysitterDTO(tidingSitter);

        assertEquals(babysitterDTO.getName(), babysitter.getName());
        assertEquals(babysitterDTO.getSurname(), babysitter.getSurname());

        assertEquals(babysitterDTO1.getName(), teachingSitter.getName());
        assertEquals(babysitterDTO1.getSurname(), teachingSitter.getSurname());

        assertEquals(babysitterDTO2.getName(), tidingSitter.getName());
        assertEquals(babysitterDTO2.getSurname(), tidingSitter.getSurname());
    }

    @Test
    void convertBabysitterDTOToBabysitterTest() {
        BabysitterDTO babysitterDTO = new BabysitterDTO("Anna", "Kowalska", 20.0, 5, 6);
        TeachingSitterDTO teachingSitterDTO = new TeachingSitterDTO("Anna2", "Kowalska2", 20.0, 4, 5, 5);
        TidingSitterDTO tidingSitterDTO = new TidingSitterDTO("Anna3", "Kowalska3", 20.0, 4, 5, 500.0);

        Babysitter babysitter = BabysitterDTOConverter.convertBabysitterDTOToBabysitter(babysitterDTO);
        Babysitter teachingSitter = BabysitterDTOConverter.convertBabysitterDTOToBabysitter(teachingSitterDTO);
        Babysitter tidingSitter = BabysitterDTOConverter.convertBabysitterDTOToBabysitter(tidingSitterDTO);

        assertEquals(babysitterDTO.getName(), babysitter.getName());
        assertEquals(babysitterDTO.getSurname(), babysitter.getSurname());

        assertEquals(teachingSitterDTO.getName(), teachingSitter.getName());
        assertEquals(teachingSitterDTO.getSurname(), teachingSitter.getSurname());

        assertEquals(tidingSitterDTO.getName(), tidingSitter.getName());
        assertEquals(tidingSitterDTO.getSurname(), tidingSitter.getSurname());
    }

    @Test
    void convertEmploymentToEmploymentDTOTest() {
        Babysitter babysitter = new Babysitter("Anna", "Kowalska", 20.0, 5, 6);
        Client client = new Client("Login1", "Jan1", "Kowalski", "kowalski", "CliDTO", 3, 4);
        Employment employment = new Employment(babysitter, client);

        EmploymentDTO employmentDTO = EmploymentDTOConverter.convertEmploymentToEmploymentDTO(employment);

        assertEquals(employmentDTO.getBabysitter().getName(), babysitter.getName());
        assertEquals(employmentDTO.getBabysitter().getSurname(), babysitter.getSurname());
        assertEquals(employmentDTO.getClient().getLogin(), client.getLogin());
    }

    @Test
    void convertUserToUserDTOTest() {
        User user = new Client("Login1", "Jan1", "Kowalski", "kowalski", "CliDTO", 3, 4);
        User admin = new Admin("Login2", "Jan2", "Kowalski", "kowalski", "Admin");
        User superUser = new SuperUser("Login3", "Jan3", "Kowalski", "kowalski", "SuperUser");

        UserDTO userDTO = UserDTOConverter.convertUserToUserDTO(user);
        UserDTO userDTO1 = UserDTOConverter.convertUserToUserDTO(admin);
        UserDTO userDTO2 = UserDTOConverter.convertUserToUserDTO(superUser);

        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getLogin(), user.getLogin());

        assertEquals(userDTO1.getName(), admin.getName());
        assertEquals(userDTO1.getLogin(), admin.getLogin());

        assertEquals(userDTO2.getName(), superUser.getName());
        assertEquals(userDTO2.getLogin(), superUser.getLogin());
    }

    @Test
    void convertUserDTOToUserTest() {
        UserDTO userDTO = new ClientDTO("Login1", "Jan1", "Kowalski", "kowalski", "CliDTO", 3, 4);
        UserDTO userDTO1 = new AdminDTO("Login2", "Jan2", "Kowalski", "kowalski", "Admin");
        UserDTO userDTO2 = new SuperUserDTO("Login3", "Jan3", "Kowalski", "kowalski", "SuperUser");

        User user = UserDTOConverter.convertUserDTOToUser(userDTO);
        User admin = UserDTOConverter.convertUserDTOToUser(userDTO1);
        User superUser = UserDTOConverter.convertUserDTOToUser(userDTO2);

        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getLogin(), user.getLogin());

        assertEquals(userDTO1.getName(), admin.getName());
        assertEquals(userDTO1.getLogin(), admin.getLogin());

        assertEquals(userDTO2.getName(), superUser.getName());
        assertEquals(userDTO2.getLogin(), superUser.getLogin());
    }

}