package pl.ks.dk.tks.repositories;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.ks.dk.tks.model.employments.EmploymentEnt;
import pl.ks.dk.tks.model.exceptions.RepositoryExceptionEnt;
import pl.ks.dk.tks.model.users.ClientEnt;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmploymentsRepositoryEnt extends RepositoryEnt<EmploymentEnt> {

    final int SHORT_ID_LENGTH = 8;

    @Inject
    BabysittersRepositoryEnt babysittersRepositoryEnt;

    @Inject
    UsersRepositoryEnt usersRepositoryEnt;

    @Override public void addElement(EmploymentEnt element) {
        element.setUniqueID(RandomStringUtils.randomNumeric(SHORT_ID_LENGTH));
        super.addElement(element);
    }

    public EmploymentEnt findByKey(String uuid) {
        List<EmploymentEnt> employmentList = getElements();

        for (EmploymentEnt employment : employmentList) {
            if (employment.getUuid().equals(uuid)) {
                return employment;
            }
        }
        throw new RepositoryExceptionEnt("Element not found");
    }

    public List<EmploymentEnt> showSelected(String id) {
        List<EmploymentEnt> temporaryEmploymentList = new ArrayList<>();
        for (EmploymentEnt employment : getElements()) {
            if (employment.getBabysitter() != null) {
                if (employment.getClient().getUuid().equals(id) || employment.getBabysitter().getUuid().equals(id)) {
                    temporaryEmploymentList.add(employment);
                    break;
                }
            } else {
                if (employment.getClient().getUuid().equals(id)) {
                    temporaryEmploymentList.add(employment);
                    break;
                }
            }
        }
        return temporaryEmploymentList;
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
}
