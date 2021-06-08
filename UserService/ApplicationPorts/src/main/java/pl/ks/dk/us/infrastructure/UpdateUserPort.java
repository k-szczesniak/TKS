package pl.ks.dk.us.infrastructure;


import pl.ks.dk.us.users.User;

public interface UpdateUserPort {
    void updateUser(User user, String key);

    void updateUserByLogin(User user, String login);
}
