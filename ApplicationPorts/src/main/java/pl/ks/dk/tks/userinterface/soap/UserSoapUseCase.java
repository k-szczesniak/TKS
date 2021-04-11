package pl.ks.dk.tks.userinterface.soap;

import pl.ks.dk.tks.domainmodel.users.User;

import java.util.List;

public interface UserSoapUseCase {

    User getUserByKey(String uuid);

    List<User> getAllUsers();

    void addUser(User user);

    int getUsersCount();

}
