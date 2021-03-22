package pl.ks.dk.tks.repositories;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import pl.ks.dk.tks.model.users.AdminEnt;
import pl.ks.dk.tks.model.users.ClientEnt;
import pl.ks.dk.tks.model.users.SuperUserEnt;
import pl.ks.dk.tks.model.users.UserEnt;
import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@ApplicationScoped
public class UsersRepositoryEnt extends RepositoryEnt<UserEnt> {

    final int SHORT_ID_LENGTH = 8;

    public List<UserEnt> getUsersList() {
        return getElements();
    }

    @Override
    public void addElement(UserEnt user) throws RepositoryExceptionEnt {
        if (isLoginUnique(user.getLogin())) {
            user.setUuid(RandomStringUtils.randomNumeric(SHORT_ID_LENGTH));
            super.addElement(user);
        } else {
            throw new RepositoryExceptionEnt("Login is not unique");
        }
    }

    public void updateElement(UserEnt user, String uuid) throws RepositoryExceptionEnt {
        try {
            BeanUtils.copyProperties(findUserByUuid(uuid), user);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RepositoryExceptionEnt("Error during update");
        }
    }

    private boolean isLoginUnique(String login) {
        for (UserEnt user : getElements()) {
            if (user.getLogin().equals(login)) {
                return false;
            }
        }
        return true;
    }

    public UserEnt findUserByUuid(String uuid) throws RepositoryExceptionEnt {
        for (UserEnt user : getElements()) {
            if (user.getUuid().equals(uuid)) {
                return user;
            }
        }
        throw new RepositoryExceptionEnt("User not found");
    }

    public UserEnt findUserByLogin(String login) throws RepositoryExceptionEnt {
        for (UserEnt user : getElements()) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        throw new RepositoryExceptionEnt("User not found");
    }

    @PostConstruct
    private void initUsersList() {
        addElement(new AdminEnt("aAdamski", "Adam", "Adamski", "adamski", "Admin"));
        addElement(new SuperUserEnt("tTomkowski", "Tomek", "Tomkowski", "tomkowski", "SuperUser"));
        addElement(new ClientEnt("tHajto", "Tomasz", "Hajto", "hajto", "Client", 3, 4));
        addElement(new ClientEnt("jUrban", "Jan", "Urban", "urban", "Client", 2, 7));
        addElement(
                new ClientEnt("jKwiatkowska", "Janina", "Kwiatkowska", "kwiatkowska", "Client", 2, 5));
        addElement(new ClientEnt("aWiadro", "Agata", "Wiadro", "wiadro", "Client", 1, 13));
    }
}
