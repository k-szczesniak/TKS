package pl.ks.dk.us.userinterface;


import pl.ks.dk.us.users.User;

import java.util.List;

public interface UserUseCase {

    User getUserByLogin(String login);

    User getUserByKey(String uuid);

    List<User> getAllUsers();

    void addUser(User user);

    void updateUser(User user, String uuid);

    boolean checkIfUserIsActive(String login);

    User getUserByLoginAndPassword(String login, String password);

    int getUsersCount();
}
