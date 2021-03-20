package pl.ks.dk.tks.aggregates;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.infrastructure.employments.AddEmploymentPort;
import pl.ks.dk.tks.infrastructure.employments.GetEmploymentPort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EmploymentAdapter implements AddEmploymentPort, GetEmploymentPort {
    @Override
    public void addEmployment(Client client, Babysitter babysitter) {

    }

    @Override
    public List<Employment> getActualEmploymentsForClient(Client client) {
        return null;
    }
}
