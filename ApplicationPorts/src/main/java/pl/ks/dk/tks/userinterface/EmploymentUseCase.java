package pl.ks.dk.tks.userinterface;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Client;

import java.util.List;

public interface EmploymentUseCase {

    List<Employment> getActualEmploymentsForClient(Client client);

    void employ(Client client, Babysitter babysitter);
}
