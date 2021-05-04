package pl.ks.dk.us.soapmodel.users;

import pl.ks.dk.us.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserSOAPConverter {

    public static User convertUserSOAPToUser(UserSOAP userSOAP) {
        return copyUserSOAPToUser(userSOAP);
    }

    public static UserSOAP convertUserToUserSOAP(User user) {
        return copyUserToUserSOAP(user);

    }

    //TODO: usunac wyjatek od convertowania

    private static User copyUserSOAPToUser(UserSOAP userSOAP) {
        User user = new User(userSOAP.getLogin(), userSOAP.getName(), userSOAP.getSurname(), userSOAP.getPassword(),
                userSOAP.getRole());
        if (userSOAP.getUuid() != null) {
            user.setUuid(userSOAP.getUuid());
        }
        user.setActive(userSOAP.isActive());
        return user;
    }

    private static UserSOAP copyUserToUserSOAP(User user) {
        UserSOAP userSOAP =
                new UserSOAP(user.getLogin(), user.getName(), user.getSurname(), user.getPassword(), user.getRole());
        if (user.getUuid() != null) {
            userSOAP.setUuid(user.getUuid());
        }
        userSOAP.setActive(user.isActive());
        return userSOAP;
    }

    public static List<UserSOAP> convertUserListToUserSOAPList(List<User> userList) {
        return userList.stream()
                .map(UserSOAPConverter::convertUserToUserSOAP)
                .collect(Collectors.toList());
    }
}
