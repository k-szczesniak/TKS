package pl.ks.dk.tks.infrastructure.users;

import pl.ks.dk.tks.domainmodel.users.User;

import java.util.List;

public interface GetUserPort {

    User getUserByLogin(String login);

    User getUserByKey(String key);

    List<User> getAllUsers();
}
