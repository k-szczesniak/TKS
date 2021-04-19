package pl.ks.dk.tks.infrastructure.users;

import pl.ks.dk.tks.domainmodel.users.User;

public interface UpdateUserPort {
    void updateUser(User user, String key);
}
