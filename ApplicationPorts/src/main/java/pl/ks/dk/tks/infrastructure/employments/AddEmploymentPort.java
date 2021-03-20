package pl.ks.dk.tks.infrastructure.employments;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.users.Client;

public interface AddEmploymentPort {

    void addEmployment(Client client, Babysitter babysitter);
}
