package pl.ks.dk.tks.aggregates;

import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.infrastructure.users.AddUserPort;
import pl.ks.dk.tks.infrastructure.users.GetUserPort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserAdapter implements AddUserPort, GetUserPort {

    @Override
    public void addUser(User user) {

    }

    @Override
    public User getUserByLogin(String login) {
        return null;
    }

    @Override
    public User getUserByKey(String key) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}
