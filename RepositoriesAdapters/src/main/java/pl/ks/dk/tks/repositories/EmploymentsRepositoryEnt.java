package pl.ks.dk.tks.repositories;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.ks.dk.tks.model.employments.EmploymentEnt;
import pl.ks.dk.tks.model.users.ClientEnt;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

//TODO: W WOLNEJ CHWILI: POSTCONSTRUCT

@ApplicationScoped
public class EmploymentsRepositoryEnt extends RepositoryEnt<EmploymentEnt> {

    final int SHORT_ID_LENGTH = 8;

    @Inject
    BabysittersRepositoryEnt babysittersRepositoryEnt;

    @Inject
    UsersRepositoryEnt usersRepositoryEnt;

    @Override public void addElement(EmploymentEnt element) {
        element.setUuid(RandomStringUtils.randomNumeric(SHORT_ID_LENGTH));
        super.addElement(element);
    }

    @PostConstruct
    public void initEmploymentList() {
        babysittersRepositoryEnt.getBabysittersList().get(1).setEmployed(true);
        babysittersRepositoryEnt.getBabysittersList().get(2).setEmployed(true);
        addElement(new EmploymentEnt(
                babysittersRepositoryEnt.getBabysittersList().get(1),
                (ClientEnt) usersRepositoryEnt.getElements().get(2)));
        addElement(new EmploymentEnt(
                babysittersRepositoryEnt.getBabysittersList().get(2),
                (ClientEnt) usersRepositoryEnt.getElements().get(3)));
    }

    public String toString() {
        ToStringBuilder stringBuilder = new ToStringBuilder(this);
        stringBuilder.append(System.getProperty("line.separator"));

        for (EmploymentEnt employment : getElements()) {
            stringBuilder.append("employment", employment.toString());
            stringBuilder.append(System.getProperty("line.separator"));
        }

        return stringBuilder.toString();
    }
}
