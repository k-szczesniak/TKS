package pl.ks.dk.tks.userinterface.rest;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Client;

import java.util.List;

public interface EmploymentRestUseCase {

    List<Employment> getActualEmploymentsForClient(String key);

    void employ(Client client, Babysitter babysitter);
}
