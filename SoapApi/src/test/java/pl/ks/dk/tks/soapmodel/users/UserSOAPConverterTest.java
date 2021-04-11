package pl.ks.dk.tks.soapmodel.users;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserSOAPConverterTest {

    @Test
    void convertUserToUserSOAPTest() {
        User user = new Client("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);
        User admin = new Admin("Login2", "Jan2", "Kowalski", "kowalski", "Admin");
        User superUser = new SuperUser("Login3", "Jan3", "Kowalski", "kowalski", "SuperUser");

        UserSOAP userSOAP = UserSOAPConverter.convertUserToUserSOAP(user);
        UserSOAP userSOAP1 = UserSOAPConverter.convertUserToUserSOAP(admin);
        UserSOAP userSOAP2 = UserSOAPConverter.convertUserToUserSOAP(superUser);

        assertEquals(userSOAP.getName(), user.getName());
        assertEquals(userSOAP.getLogin(), user.getLogin());

        assertEquals(userSOAP1.getName(), admin.getName());
        assertEquals(userSOAP1.getLogin(), admin.getLogin());

        assertEquals(userSOAP2.getName(), superUser.getName());
        assertEquals(userSOAP2.getLogin(), superUser.getLogin());
    }

    @Test
    void convertUserSOAPToUserTest() {
        UserSOAP userSOAP = new ClientSOAP("Login1", "Jan1", "Kowalski", "kowalski", "Client", 3, 4);
        UserSOAP userSOAP1 = new AdminSOAP("Login2", "Jan2", "Kowalski", "kowalski", "Admin");
        UserSOAP userSOAP2 = new SuperUserSOAP("Login3", "Jan3", "Kowalski", "kowalski", "SuperUser");

        User user = UserSOAPConverter.convertUserSOAPToUser(userSOAP);
        User admin = UserSOAPConverter.convertUserSOAPToUser(userSOAP1);
        User superUser = UserSOAPConverter.convertUserSOAPToUser(userSOAP2);

        assertEquals(userSOAP.getName(), user.getName());
        assertEquals(userSOAP.getLogin(), user.getLogin());

        assertEquals(userSOAP1.getName(), admin.getName());
        assertEquals(userSOAP1.getLogin(), admin.getLogin());

        assertEquals(userSOAP2.getName(), superUser.getName());
        assertEquals(userSOAP2.getLogin(), superUser.getLogin());
    }
}