package pl.ks.dk.us.infrastructure;



import pl.ks.dk.us.users.User;

import java.util.List;

public interface GetUserPort {

    User getUserByLogin(String login);

    User getUserByKey(String key);

    List<User> getAllUsers();

    int getUsersCount();
}
