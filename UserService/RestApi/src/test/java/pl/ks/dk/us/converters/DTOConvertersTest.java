package pl.ks.dk.us.converters;

import org.junit.jupiter.api.Test;
import pl.ks.dk.us.dtomodel.users.UserDTO;
import pl.ks.dk.us.users.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DTOConvertersTest {

    @Test
    void convertUserToUserDTOTest() {
        User user = new User("Login2", "Jan2", "Kowalski", "kowalski", "Admin");

        UserDTO userDTO = UserDTOConverter.convertUserToUserDTO(user);

        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getLogin(), user.getLogin());
    }

    @Test
    void convertUserDTOToUserTest() {
        UserDTO userDTO = new UserDTO("Login2", "Jan2", "Kowalski", "kowalski", "Admin");

        User user = UserDTOConverter.convertUserDTOToUser(userDTO);

        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getLogin(), user.getLogin());
    }

}