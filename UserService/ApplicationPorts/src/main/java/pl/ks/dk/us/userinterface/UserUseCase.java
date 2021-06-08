package pl.ks.dk.us.userinterface;


import pl.ks.dk.us.users.User;

import javax.xml.rpc.ServiceException;
import java.util.List;

public interface UserUseCase {

    User getUserByLogin(String login);

    User getUserByKey(String uuid);

    List<User> getAllUsers();

    void addUser(User user) throws ServiceException;

    void updateUser(User user, String uuid);

    void updateUserByLogin(User user, String login) throws ServiceException;

    boolean checkIfUserIsActive(String login);

    User getUserByLoginAndPassword(String login, String password);

    int getUsersCount();
}
