package pl.ks.dk.tks.aggregates;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.infrastructure.users.AddUserPort;
import pl.ks.dk.tks.infrastructure.users.GetUserPort;
import pl.ks.dk.tks.infrastructure.users.UpdateUserPort;
import pl.ks.dk.tks.model.users.AdminEnt;
import pl.ks.dk.tks.model.users.ClientEnt;
import pl.ks.dk.tks.model.users.SuperUserEnt;
import pl.ks.dk.tks.model.users.UserEnt;
import pl.ks.dk.tks.repositories.UsersRepositoryEnt;
import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserAdapter implements AddUserPort, GetUserPort, UpdateUserPort {

    @Inject
    private UsersRepositoryEnt usersRepositoryEnt;

    @Override
    public void addUser(User user) throws AdapterException {
        try {
            usersRepositoryEnt.addElement(convertUserToUserEnt(user));
        } catch (RepositoryExceptionEnt repositoryExceptionEnt) {
            throw new AdapterException(repositoryExceptionEnt.getMessage(), repositoryExceptionEnt);
        }
    }

    @Override
    public User getUserByLogin(String login) throws AdapterException {
        User user = null;
        try {
            user = convertUserEntToUser(usersRepositoryEnt.findUserByLogin(login));
        } catch (RepositoryExceptionEnt repositoryExceptionEnt) {
            throw new AdapterException(repositoryExceptionEnt.getMessage(), repositoryExceptionEnt);
        }
        return user;
    }

    @Override
    public User getUserByKey(String key) throws AdapterException {
        User user = null;
        try {
            user = convertUserEntToUser(usersRepositoryEnt.findUserByUuid(key));
        } catch (RepositoryExceptionEnt repositoryExceptionEnt) {
            throw new AdapterException(repositoryExceptionEnt.getMessage(), repositoryExceptionEnt);
        }
        return user;
    }

    @Override
    public void updateUser(User user, String key) throws AdapterException {
        try {
            usersRepositoryEnt.updateElement(convertUserToUserEnt(user), key);
        } catch (RepositoryExceptionEnt repositoryExceptionEnt) {
            throw new AdapterException(repositoryExceptionEnt.getMessage(), repositoryExceptionEnt);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return usersRepositoryEnt.getUsersList().stream()
                .map(UserAdapter::convertUserEntToUser)
                .collect(Collectors.toList());
    }

    public static UserEnt convertUserToUserEnt(User user) {
        if (user instanceof Client) {
            UserEnt clientEnt = new ClientEnt();
            return copyUserToUserEnt(clientEnt, user);
        }
        if (user instanceof SuperUser) {
            UserEnt superUserEnt = new SuperUserEnt();
            return copyUserToUserEnt(superUserEnt, user);
        } else {
            UserEnt adminEnt = new AdminEnt();
            return copyUserToUserEnt(adminEnt, user);
        }
    }

    public static User convertUserEntToUser(UserEnt userEnt) {
        if (userEnt instanceof ClientEnt) {
            User client = new Client();
            return copyUserEntToUser(client, userEnt);
        }
        if (userEnt instanceof SuperUserEnt) {
            User superUser = new SuperUser();
            return copyUserEntToUser(superUser, userEnt);
        } else {
            User admin = new Admin();
            return copyUserEntToUser(admin, userEnt);
        }
    }

    private static UserEnt copyUserToUserEnt(UserEnt userEnt, User user) throws AdapterException {
        try {
            BeanUtils.copyProperties(userEnt, user);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AdapterException("Convert User to UserEnt error", e);
        }
        return userEnt;
    }

    private static User copyUserEntToUser(User user, UserEnt userEnt) throws AdapterException {
        try {
            BeanUtils.copyProperties(user, userEnt);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AdapterException("Convert UserEnt to User error", e);
        }
        return user;
    }
}
