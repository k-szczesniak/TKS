package pl.ks.dk.tks.aggregates;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.infrastructure.employments.AddEmploymentPort;
import pl.ks.dk.tks.infrastructure.employments.GetEmploymentPort;
import pl.ks.dk.tks.model.employments.EmploymentEnt;
import pl.ks.dk.tks.repositories.EmploymentsRepositoryEnt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmploymentAdapter implements AddEmploymentPort, GetEmploymentPort {

    //TODO:printStackTrace na jakiś wyjątek

    @Inject
    private EmploymentsRepositoryEnt employmentsRepositoryEnt;

    @Override
    public void addEmployment(Client client, Babysitter babysitter) {
        babysitter.setEmployed(true);
        Employment employment = new Employment(babysitter, client);
        employmentsRepositoryEnt.addElement(convertEmploymentToEmploymentEnt(employment));
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
        try {
            BeanUtils.copyProperties(employmentEnt, employment);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return employmentEnt;
    }

    public static Employment convertEmploymentEntToEmployment(EmploymentEnt employmentEnt) {
        Employment employment = new Employment();
        try {
            BeanUtils.copyProperties(employment, employmentEnt);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return employment;
    }
}
