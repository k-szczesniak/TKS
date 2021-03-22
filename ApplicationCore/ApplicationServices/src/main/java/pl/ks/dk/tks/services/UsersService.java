package pl.ks.dk.tks.services;

import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.infrastructure.users.AddUserPort;
import pl.ks.dk.tks.infrastructure.users.GetUserPort;
import pl.ks.dk.tks.infrastructure.users.UpdateUserPort;
import pl.ks.dk.tks.services.exceptions.ServiceException;
import pl.ks.dk.tks.userinterface.UserUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UsersService implements UserUseCase {

    @Inject
    private AddUserPort addUserPort;

    @Inject
    private GetUserPort getUserPort;

    @Inject
    private UpdateUserPort updateUserPort;

    @Override
    public User getUserByLogin(String login) throws ServiceException {
        User user;
        try {
            user = getUserPort.getUserByLogin(login);
        } catch (AdapterException adapterException) {
            throw new ServiceException(adapterException.getMessage(), adapterException);
        }
        return user;
    }

    @Override
    public User getUserByKey(String uuid) throws ServiceException {
        User user;
        try {
            user = getUserPort.getUserByKey(uuid);
        } catch (AdapterException adapterException) {
            throw new ServiceException(adapterException.getMessage(), adapterException);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return getUserPort.getAllUsers();
    }

    @Override
    public void addUser(User user) throws ServiceException {
        try {
            addUserPort.addUser(user);
        } catch (AdapterException adapterException) {
            throw new ServiceException(adapterException.getMessage(), adapterException);
        }
    }

    @Override
    public void updateUser(User user, String uuid) {
        try {
            updateUserPort.updateUser(user, uuid);
        } catch (AdapterException adapterException) {
            throw new ServiceException(adapterException.getMessage(), adapterException);
        }
    }

    @Override
    public boolean checkIfUserIsActive(String login) {
        return getUserByLogin(login).isActive();
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) throws ServiceException {
        User user = null;
        try {
            User tmpUser = getUserByLogin(login);
            if (tmpUser.getPassword().equals(password)) {
                user = tmpUser;
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return user;
    }
}
