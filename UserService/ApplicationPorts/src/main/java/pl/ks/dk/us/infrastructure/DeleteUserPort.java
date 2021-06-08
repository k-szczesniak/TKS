package pl.ks.dk.us.infrastructure;

import pl.ks.dk.us.exceptions.AdapterException;

public interface DeleteUserPort {

    void deleteUser(String login) throws AdapterException;
}
