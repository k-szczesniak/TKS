package pl.ks.dk.us.aggregates;

import pl.ks.dk.us.exceptions.AdapterException;
import pl.ks.dk.us.infrastructure.AddUserPort;
import pl.ks.dk.us.infrastructure.DeleteUserPort;
import pl.ks.dk.us.infrastructure.GetUserPort;
import pl.ks.dk.us.infrastructure.UpdateUserPort;
import pl.ks.dk.us.model.UserEnt;
import pl.ks.dk.us.repositories.UsersRepositoryEnt;
import pl.ks.dk.us.repositories.exceptions.RepositoryExceptionEnt;
import pl.ks.dk.us.users.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserAdapter implements AddUserPort, GetUserPort, UpdateUserPort, DeleteUserPort {

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
    public void deleteUser(String login) throws AdapterException {
        try {
            usersRepositoryEnt.deleteElementByLogin(login);
        } catch (RepositoryExceptionEnt repositoryExceptionEnt) {
            throw new AdapterException(repositoryExceptionEnt.getMessage(), repositoryExceptionEnt);
        }
    }

    @Override
    public void updateUserByLogin(User user, String login) throws AdapterException {
        try {
            usersRepositoryEnt.updateElementByLogin(convertUserToUserEnt(user), login);
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
        return copyUserToUserEnt(user);
    }

    public static User convertUserEntToUser(UserEnt userEnt) {
        return copyUserEntToUser(userEnt);
    }

    private static UserEnt copyUserToUserEnt(User user) throws AdapterException {
        UserEnt userEnt =
                new UserEnt(user.getLogin(), user.getName(), user.getSurname(), user.getPassword(), user.getRole());
        if (user.getUuid() != null) {
            userEnt.setUuid(user.getUuid());
        }
        userEnt.setActive(user.isActive());
        return userEnt;
    }

    private static User copyUserEntToUser(UserEnt userEnt) throws AdapterException {
        User user = new User(userEnt.getLogin(), userEnt.getName(), userEnt.getSurname(), userEnt.getPassword(),
                userEnt.getRole());
        if (userEnt.getUuid() != null) {
            user.setUuid(userEnt.getUuid());
        }
        user.setActive(userEnt.isActive());
        return user;
    }
}
