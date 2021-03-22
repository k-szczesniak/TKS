package pl.ks.dk.tks.aggregates;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.infrastructure.employments.AddEmploymentPort;
import pl.ks.dk.tks.infrastructure.employments.GetEmploymentPort;
import pl.ks.dk.tks.model.employments.EmploymentEnt;
import pl.ks.dk.tks.model.users.ClientEnt;
import pl.ks.dk.tks.repositories.EmploymentsRepositoryEnt;
import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmploymentAdapter implements AddEmploymentPort, GetEmploymentPort {

    @Inject
    private EmploymentsRepositoryEnt employmentsRepositoryEnt;

    @Override
    public void addEmployment(Client client, Babysitter babysitter) throws AdapterException {
        babysitter.setEmployed(true);
        Employment employment = new Employment(babysitter, client);
        try {
            employmentsRepositoryEnt.addElement(convertEmploymentToEmploymentEnt(employment));
        } catch (RepositoryExceptionEnt repositoryExceptionEnt) {
            throw new AdapterException(repositoryExceptionEnt.getMessage(), repositoryExceptionEnt);
        }
    }

    @Override
    public List<Employment> getActualEmploymentsForClient(Client client) {
        List<Employment> actualEmploymentList = new ArrayList<>();
        //TODO:Tu może nie pyknąć
        List<Employment> allEmploymentsList = employmentsRepositoryEnt.getElements().stream()
                .map(EmploymentAdapter::convertEmploymentEntToEmployment)
                .collect(Collectors.toList());
        for (Employment employment : allEmploymentsList) {
            if (employment.getClient() == client && employment.getEndOfEmployment() == null)
                actualEmploymentList.add(employment);
        }
        return actualEmploymentList;
    }

    public static EmploymentEnt convertEmploymentToEmploymentEnt(Employment employment) {
        EmploymentEnt employmentEnt = new EmploymentEnt();
        employmentEnt.setBabysitter(BabysitterAdapter.convertBabysitterToBabysitterEnt(employment.getBabysitter()));
        employmentEnt.setClient((ClientEnt) UserAdapter.convertUserToUserEnt(employment.getClient()));
        employmentEnt.setEndOfEmployment(employment.getEndOfEmployment());
        employmentEnt.setBeginningOfEmployment(employment.getBeginningOfEmployment());
        employmentEnt.setUuid(employment.getUuid());

        return employmentEnt;
    }

    public static Employment convertEmploymentEntToEmployment(EmploymentEnt employmentEnt) {
        Employment employment = new Employment();
        employment.setBabysitter(BabysitterAdapter.convertBabysitterEntToBabysitter(employmentEnt.getBabysitter()));
        employment.setClient((Client) UserAdapter.convertUserEntToUser(employmentEnt.getClient()));
        employment.setEndOfEmployment(employmentEnt.getEndOfEmployment());
        employment.setBeginningOfEmployment(employmentEnt.getBeginningOfEmployment());
        employment.setUuid(employmentEnt.getUuid());

        return employment;
    }
}
