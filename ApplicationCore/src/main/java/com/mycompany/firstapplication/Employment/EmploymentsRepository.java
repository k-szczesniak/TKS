package com.mycompany.firstapplication.Employment;


import com.mycompany.firstapplication.Babysitters.BabysittersRepository;
import com.mycompany.firstapplication.Exceptions.RepositoryException;
import com.mycompany.firstapplication.Template.Repository;
import com.mycompany.firstapplication.Users.Client;
import com.mycompany.firstapplication.Users.UsersRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmploymentsRepository extends Repository<Employment> {

    final int SHORT_ID_LENGTH = 8;

    @Inject
    BabysittersRepository babysittersRepository;

    @Inject
    UsersRepository usersRepository;

    @Override public void addElement(Employment element) {
        element.setUniqueID(RandomStringUtils.randomNumeric(SHORT_ID_LENGTH));
        super.addElement(element);
    }

    public Employment findByKey(String uuid) {
        List<Employment> employmentList = getElements();

        for (Employment employment : employmentList) {
            if (employment.getUuid().equals(uuid)) {
                return employment;
            }
        }
        throw new RepositoryException("Element not found");
    }

    public List<Employment> showSelected(String id) {
        List<Employment> temporaryEmploymentList = new ArrayList<>();
        for (Employment employment : getElements()) {
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

        for (Employment employment : getElements()) {
            stringBuilder.append("employment", employment.toString());
            stringBuilder.append(System.getProperty("line.separator"));
        }

        return stringBuilder.toString();
    }

    @PostConstruct
    public void initEmploymentList() {
        babysittersRepository.getBabysittersList().get(1).setEmployed(true);
        babysittersRepository.getBabysittersList().get(2).setEmployed(true);
        addElement(new Employment(babysittersRepository.getBabysittersList().get(1), (Client) usersRepository.getElements().get(2)));
        addElement(new Employment(babysittersRepository.getBabysittersList().get(2), (Client) usersRepository.getElements().get(3)));
    }
}
