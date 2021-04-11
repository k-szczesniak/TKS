package pl.ks.dk.tks.userinterface.rest;

import pl.ks.dk.tks.domainmodel.users.User;

import java.util.List;

public interface UserRestUseCase {

    User getUserByLogin(String login);

    User getUserByKey(String uuid);

    List<User> getAllUsers();

    void addUser(User user);

    void updateUser(User user, String uuid);

    boolean checkIfUserIsActive(String login);

    User getUserByLoginAndPassword(String login, String password);
}
