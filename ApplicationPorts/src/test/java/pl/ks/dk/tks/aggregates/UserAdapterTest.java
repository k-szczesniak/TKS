package pl.ks.dk.tks.aggregates;

import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.model.users.UserEnt;

public class UserAdapterTest {
    @Test
    void convertUserToUserEntTest() {
        User user = new Client("jKowalski", "Jan", "Kowalski", "kowalski", "Client", 3, 4);
        User admin = new Admin("jKowalski", "Jan", "Kowalski", "kowalski", "Admin");
        User superUser = new SuperUser("jKowalski", "Jan", "Kowalski", "kowalski", "SuperUser");

        UserEnt userEnt = UserAdapter.convertUserToUserEnt(user);
        UserEnt userEnt1 = UserAdapter.convertUserToUserEnt(admin);
        UserEnt userEnt2 = UserAdapter.convertUserToUserEnt(superUser);
        int i = 5;
    }
}
