package com.mycompany.firstapplication.Employment;

import com.mycompany.firstapplication.Babysitters.Babysitter;
import com.mycompany.firstapplication.Babysitters.BabysittersManager;
import com.mycompany.firstapplication.Exceptions.EmploymentException;
import com.mycompany.firstapplication.Users.Client;
import com.mycompany.firstapplication.Users.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmploymentsManager implements Serializable {

    @Inject
    private EmploymentsRepository employmentsRepository;

    @Inject
    private BabysittersManager babysittersManager;

    public EmploymentsManager(EmploymentsRepository employmentsRepository) {
        this.employmentsRepository = employmentsRepository;
    }

    public EmploymentsManager() {
    }

    private List<Employment> currentEmployments;

    public List<Employment> getCurrentEmployments() {
        return currentEmployments;
    }

    public void setCurrentEmployments(List<Employment> currentEmployments) {
        this.currentEmployments = currentEmployments;
    }

    public void employBabysitter(Client client, Babysitter babysitter) {
        checkIfUserIsActive(client);
        checkIfBabysitterMeetsRequirements(babysitter, client.getAgeOfTheYoungestChild(),
                client.getNumberOfChildren());
        checkIfBabysitterIsCurrentlyEmployed(babysitter);

        babysitter.setEmployed(true);
        Employment employment = new Employment(babysitter, client);
        employmentsRepository.addElement(employment);
    }

    public void checkIfBabysitterExists(Babysitter babysitter) {
        babysittersManager.getBabysittersRepository().findByKey(babysitter.getUuid());
    }

    private void checkIfUserIsActive(Client client) {
        if (!client.isActive()) {
            throw new EmploymentException("Client is not active");
        }
    }

    public void checkIfBabysitterMeetsRequirements(Babysitter babysitter, int minAge,
                                                   int numberOfChildren) {
        if (babysitter.getMinChildAge() > minAge) {
            throw new EmploymentException("Babysitter does not meet requirements");
        }
        if (babysitter.getMaxNumberOfChildrenInTheFamily() < numberOfChildren) {
            throw new EmploymentException("Babysitter does not meet requirements");
        }
    }

    public void checkIfBabysitterIsCurrentlyEmployed(Babysitter babysitter) {
        if (babysitter.isEmployed()) {
            throw new EmploymentException("Babysitter already employed");
        }
    }

    public void endEmployment(Employment employment) {
        if (employment.getEndOfEmployment() != null) {
            throw new EmploymentException("Employment already ended");
        }
        employment.endEmployment();
        employment.getBabysitter().setEmployed(false);
    }

    public EmploymentsRepository getEmploymentsRepository() {
        return employmentsRepository;
    }

    public int numberOfCurrentClientEmployment(Client client) {
        List<Employment> allEmploymentsList = employmentsRepository.getElements();
        int counter = 0;

        for (Employment employment : allEmploymentsList) {
            if (employment.getClient() == client &&
                    employment.getEndOfEmployment() == null) {
                counter++;
            }
        }
        return counter;
    }

    public int numberOfEndedClientEmployment(Client client) {
        List<Employment> allEmploymentsList = employmentsRepository.getElements();
        int counter = 0;

        for (Employment employment : allEmploymentsList) {
            if (employment.getClient() == client &&
                    employment.getEndOfEmployment() != null) {
                counter++;
            }
        }
        return counter;
    }

    public Employment getActualEmploymentForBabysitter(Babysitter babysitter) {
        List<Employment> allEmploymentsList = employmentsRepository.getElements();

        for (Employment employment : allEmploymentsList) {
            if (employment.getBabysitter() == babysitter &&
                    employment.getEndOfEmployment() == null) {
                return employment;
            }
        }
        throw new EmploymentException("Babysitter is not already employed");
    }

    public List<Employment> getAllEmploymentsForBabysitter(Babysitter babysitter) {
        List<Employment> allEmploymentsList = employmentsRepository.getElements();
        List<Employment> allEmploymentsForBabysitterList = new ArrayList<>();

        for (Employment employment : allEmploymentsList) {
            if (employment.getBabysitter() == babysitter) {
                allEmploymentsForBabysitterList.add(employment);
            }
        }
        return allEmploymentsForBabysitterList;
    }

    public Employment getActualEmploymentForClient(Client client) {
        List<Employment> allEmploymentsList = employmentsRepository.getElements();

        for (Employment employment : allEmploymentsList) {
            if (employment.getClient() == client && employment.getEndOfEmployment() == null) {
                return employment;
            }
        }
        throw new EmploymentException("Client does not have any actual employment");
    }

    public List<Employment> getActualEmploymentsForClient(Client client) {
        List<Employment> actualEmploymentList = new ArrayList<>();
        List<Employment> allEmploymentsList = employmentsRepository.getElements();
        for (Employment employment : allEmploymentsList) {
            if (employment.getClient() == client && employment.getEndOfEmployment() == null)
                actualEmploymentList.add(employment);
        }
        return actualEmploymentList;
    }

    public List<Employment> getAllEmploymentsForClient(Client client) {
        List<Employment> allEmploymentsList = employmentsRepository.getElements();
        List<Employment> allEmploymentsForFamilyList = new ArrayList<>();

        for (Employment employment : allEmploymentsList) {
            if (employment.getClient() == client) {
                allEmploymentsForFamilyList.add(employment);
            }
        }
        return allEmploymentsForFamilyList;
    }

    public void deleteEmployment(Employment employment) {
        employment.getBabysitter().setEmployed(false);
        getEmploymentsRepository().deleteElement(employment);
    }

    public Babysitter[] getAllBabysittersConnectedToEmploymentArray() {
        List<Babysitter> babysittersList = new ArrayList<>();
        for (Employment employment : employmentsRepository.getElements()) {
            if (!babysittersList.contains(employment.getBabysitter()) && employment.getBabysitter() != null) {
                babysittersList.add(employment.getBabysitter());
            }
        }
        return babysittersList.toArray(new Babysitter[0]);
    }

    public User[] getAllUsersWithEmploymentArray() {
        List<User> userList = new ArrayList<>();
        for (Employment employment : employmentsRepository.getElements()) {
            if (!userList.contains(employment.getClient())) {
                userList.add(employment.getClient());
            }
        }
        return userList.toArray(new User[0]);
    }

    @PostConstruct
    public void initCurrentPersons() {
        currentEmployments = getEmploymentsRepository().getElements();
    }

}
