package pl.ks.dk.tks.soapmodel.users;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.soapmodel.exceptions.UserSoapConverterException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class UserSOAPConverter {

    public static User convertUserSOAPToUser(UserSOAP userSOAP) {
        if (userSOAP instanceof ClientSOAP) {
            User client = new Client();
            return copyUserSOAPToUser(client, userSOAP);
        }
        if (userSOAP instanceof SuperUserSOAP) {
            User superUser = new SuperUser();
            return copyUserSOAPToUser(superUser, userSOAP);
        } else {
            User admin = new Admin();
            return copyUserSOAPToUser(admin, userSOAP);
        }
    }

    private static User copyUserSOAPToUser(User user, UserSOAP userSOAP) throws UserSoapConverterException {
        try {
            BeanUtils.copyProperties(user, userSOAP);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new UserSoapConverterException("Convert UserSOAP to User error", e);
        }
        return user;
    }

    public static UserSOAP convertUserToUserSOAP(User user) {
        if (user instanceof Client) {
            UserSOAP clientSOAP = new ClientSOAP();
            return copyUserToUserSOAP(clientSOAP, user);
        }
        if (user instanceof SuperUser) {
            UserSOAP superUserSOAP = new SuperUserSOAP();
            return copyUserToUserSOAP(superUserSOAP, user);
        } else {
            UserSOAP adminSOAP = new AdminSOAP();
            return copyUserToUserSOAP(adminSOAP, user);
        }
    }

    private static UserSOAP copyUserToUserSOAP(UserSOAP userSOAP, User user) throws UserSoapConverterException {
        try {
            BeanUtils.copyProperties(userSOAP, user);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new UserSoapConverterException("Convert User to UserSOAP error", e);
        }
        return userSOAP;
    }

    public static List<UserSOAP> convertUserListToUserSOAPList(List<User> userList) {
        return userList.stream()
                .map(UserSOAPConverter::convertUserToUserSOAP)
                .collect(Collectors.toList());
    }
}
