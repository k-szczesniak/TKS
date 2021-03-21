package pl.ks.dk.tks.aggregates;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.model.users.UserEnt;

import static org.junit.jupiter.api.Assertions.*;

public class UserAdapterTest {
    @Test
    void convertUserToUserEntTest() {
        User user = new Client("jKowalski", "Jan", "Kowalski", "kowalski", "Client", 3, 4);
        User admin = new Admin("jKowalski", "Jan", "Kowalski", "kowalski", "Admin");
        User superUser = new SuperUser("jKowalski", "Jan", "Kowalski", "kowalski", "SuperUser");

        UserEnt userEnt = UserAdapter.convertUserToUserEnt(user);
        UserEnt userEnt1 = UserAdapter.convertUserToUserEnt(admin);
        UserEnt userEnt2 = UserAdapter.convertUserToUserEnt(superUser);

        assertEquals(userEnt.getName(), user.getName());
        assertEquals(userEnt1.getName(), admin.getName());
        assertEquals(userEnt2.getName(), superUser.getName());

        int i = 5;


    }
}
