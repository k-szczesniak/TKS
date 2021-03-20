package pl.ks.dk.tks.infrastructure.employments;

import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Client;

import java.util.List;

public interface GetEmploymentPort {

    List<Employment> getActualEmploymentsForClient(Client client);
}
