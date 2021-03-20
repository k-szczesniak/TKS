package pl.ks.dk.tks.services;

import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.infrastructure.users.AddUserPort;
import pl.ks.dk.tks.infrastructure.users.GetUserPort;
import pl.ks.dk.tks.userinterface.UserUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

//TODO: USYSTEMATYZOWAC NAZWY PAKIETOW I KLAS

@ApplicationScoped
public class UsersService implements UserUseCase {

    @Inject
    private AddUserPort addUserPort;

    @Inject
    private GetUserPort getUserPort;

    @Override
    public User getUserByLogin(String login) {
        return getUserPort.getUserByLogin(login);
    }

    @Override
    public User getUserByKey(String uuid) {
        return getUserPort.getUserByKey(uuid);
    }

    @Override
    public List<User> getAllUsers() {
        return getUserPort.getAllUsers();
    }

    @Override
    public void addUser(User user) {
        addUserPort.addUser(user);
    }

    @Override
    public boolean checkIfUserIsActive(String login) {
        return getUserPort.getUserByLogin(login).isActive();
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        User user = null;
        try {
            User tmpUser = getUserByLogin(login);
            if (tmpUser.getPassword().equals(password)) {
                user = tmpUser;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


//    @Inject
//    private UsersRepository usersRepository;
//
//    public UsersService() {
//    }
//
//    public UsersRepository getUsersRepository() {
//        return usersRepository;
//    }
//
//    public UsersService(UsersRepository usersRepository) {
//        this.usersRepository = usersRepository;
//    }
//
//    public List<User> getUsersList() {
//        return usersRepository.getUsersList();
//    }
//
//    public User[] getAllUsersArray() {
//        return usersRepository.getUsersList().toArray(new User[0]);
//    }
//
//    public List<Client> getClientList() {
//        return usersRepository.getClientList();
//    }
//
//    public void addUser(User user) {
//        usersRepository.addElement(user);
//    }
//
//    public void deleteUser(User user) {
//        usersRepository.deleteElement(user);
//    }
//
//    public void changeActive(User user) {
//        usersRepository.changeActiveForUser(user);
//    }
//
//    public boolean checkIfActive(String login) {
//        return findByLogin(login).isActive();
//    }
//
//    public int getNumberOfClients() {
//        return usersRepository.getNumberOfElements();
//    }
//
//    public User findByKey(String uuid) {
//        return usersRepository.findUserByUuid(uuid);
//    }
//
//    public User findByLogin(String login) {
//        return usersRepository.findUserByLogin(login);
//    }
//
//    public User findByLoginPasswordActive(String login, String password) {
//        User user = null;
//        try {
//           User tmpUser = findByLogin(login);
//           if (tmpUser.getPassword().equals(password) && tmpUser.isActive()) {
//               user = tmpUser;
//           }
//        } catch (UserException e) {
//            e.printStackTrace();
//        }
//        return user;
//    }
}
