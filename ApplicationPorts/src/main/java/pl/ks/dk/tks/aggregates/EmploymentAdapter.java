package pl.ks.dk.tks.aggregates;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.infrastructure.employments.AddEmploymentPort;
import pl.ks.dk.tks.infrastructure.employments.GetEmploymentPort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmploymentAdapter implements AddEmploymentPort, GetEmploymentPort {

    //TODO:convertery dorobic

    @Inject
    private EmploymentsRepositoryEnt employmentsRepositoryEnt;

    @Override
    public void addEmployment(Client client, Babysitter babysitter) {

    }

    @Override
    public List<Employment> getActualEmploymentsForClient(Client client) {
        return null;
    }
}
