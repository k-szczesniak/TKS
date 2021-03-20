package pl.ks.dk.tks.userinterface;

import pl.ks.dk.tks.domainmodel.users.User;

import java.util.List;

public interface UserUseCase {

    User getUserByLogin(String login);

    User getUserByKey(String uuid);

    List<User> getAllUsers();

    void addUser(User user);

    boolean checkIfUserIsActive(String login);

    User getUserByLoginAndPassword(String login, String password);
}
