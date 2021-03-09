package pl.ks.dk.tks.repositories;

import org.apache.commons.lang3.RandomStringUtils;
import pl.ks.dk.tks.model.exceptions.UserExceptionEnt;
import pl.ks.dk.tks.model.users.AdminEnt;
import pl.ks.dk.tks.model.users.ClientEnt;
import pl.ks.dk.tks.model.users.SuperUserEnt;
import pl.ks.dk.tks.model.users.UserEnt;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsersRepositoryEnt extends RepositoryEnt<UserEnt> {

    final int SHORT_ID_LENGTH = 8;

    public List<UserEnt> getUsersList() {
        return getElements();
    }

    public List<ClientEnt> getClientList() {
        List<ClientEnt> clientList = new ArrayList<>();
        List<UserEnt> elements = getUsersList();

        for (UserEnt element : elements) {
            if (element instanceof ClientEnt) {
                clientList.add((ClientEnt) element);
            }
        }
        return clientList;
    }

    @Override
    public void addElement(UserEnt user) {
        if (isLoginUnique(user.getLogin())) {
            user.setUuid(RandomStringUtils.randomNumeric(SHORT_ID_LENGTH));
            super.addElement(user);
        } else {
            throw new UserExceptionEnt("Login is not unique");
        }
    }

    public boolean isLoginUnique(String login) {
        for (UserEnt user : getElements()) {
            if (user.getLogin().equals(login)) {
                return false;
            }
        }
        return true;
    }

    public boolean isLoginUnique(String login, UserEnt user1) {
        for (UserEnt user : getElements()) {
            if (user.getLogin().equals(login) && user != user1) {
                return false;
            }
        }
        return true;
    }

    public List<UserEnt> showSelectedUser(String key, boolean checkById) {
        List<UserEnt> temporaryUsersList = new ArrayList<>();
        if (checkById) {
            temporaryUsersList.add(findUserByUuid(key));
        } else {
            temporaryUsersList.add(findUserByLogin(key));
        }
        return temporaryUsersList;
    }

    public UserEnt findUserByUuid(String uuid) {
        for (UserEnt user : getElements()) {
            if (user.getUuid().equals(uuid)) {
                return user;
            }
        }
        throw new UserExceptionEnt("Element not found");
    }

    public UserEnt findUserByLogin(String login) {
        for (UserEnt user : getElements()) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        throw new UserExceptionEnt("Element not found");
    }

    public void changeActiveForUser(UserEnt user) {
        findUserByUuid(user.getUuid()).changeActive();
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
