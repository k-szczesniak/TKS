package pl.ks.dk.us.aggregates;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.us.exceptions.AdapterException;
import pl.ks.dk.us.infrastructure.AddUserPort;
import pl.ks.dk.us.infrastructure.GetUserPort;
import pl.ks.dk.us.infrastructure.UpdateUserPort;
import pl.ks.dk.us.model.UserEnt;
import pl.ks.dk.us.repositories.UsersRepositoryEnt;
import pl.ks.dk.us.repositories.exceptions.RepositoryExceptionEnt;
import pl.ks.dk.us.users.User;

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

    @Override
    public int getUsersCount() {
        return usersRepositoryEnt.getNumberOfElements();
    }

    public static UserEnt convertUserToUserEnt(User user) {
            UserEnt userEnt = new UserEnt();
            return copyUserToUserEnt(userEnt, user);
    }

    public static User convertUserEntToUser(UserEnt userEnt) {
            User user = new User();
            return copyUserEntToUser(user, userEnt);
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
