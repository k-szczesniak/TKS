package pl.ks.dk.us.soapmodel.users;

import org.junit.jupiter.api.Test;
import pl.ks.dk.us.users.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserSOAPConverterTest {

    @Test
    void convertUserToUserSOAPTest() {
        User user = new User("Login1", "Jan1", "Kowalski", "kowalski", "Client");


        UserSOAP userSOAP = UserSOAPConverter.convertUserToUserSOAP(user);

        assertEquals(userSOAP.getName(), user.getName());
        assertEquals(userSOAP.getLogin(), user.getLogin());
    }

    @Test
    void convertUserSOAPToUserTest() {
        UserSOAP userSOAP = new UserSOAP("Login1", "Jan1", "Kowalski", "kowalski", "Client");

        User user = UserSOAPConverter.convertUserSOAPToUser(userSOAP);

        assertEquals(userSOAP.getName(), user.getName());
        assertEquals(userSOAP.getLogin(), user.getLogin());
    }
}