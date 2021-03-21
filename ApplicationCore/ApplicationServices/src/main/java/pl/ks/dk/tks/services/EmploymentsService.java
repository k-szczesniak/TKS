package pl.ks.dk.tks.services;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.domainmodel.exceptions.EmploymentException;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.infrastructure.employments.AddEmploymentPort;
import pl.ks.dk.tks.infrastructure.employments.GetEmploymentPort;
import pl.ks.dk.tks.services.exceptions.ServiceException;
import pl.ks.dk.tks.userinterface.EmploymentUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class EmploymentsService implements EmploymentUseCase {

    @Inject
    private AddEmploymentPort addEmploymentPort;

    @Inject
    private GetEmploymentPort getEmploymentPort;

    @Override
    public List<Employment> getActualEmploymentsForClient(Client client) {
        return getEmploymentPort.getActualEmploymentsForClient(client);
    }

    @Override
    public void employ(Client client, Babysitter babysitter) throws ServiceException {

        try {
            checkIfUserIsActive(client);
            checkIfBabysitterMeetsRequirements(babysitter, client.getAgeOfTheYoungestChild(), client.getNumberOfChildren());
            checkIfBabysitterIsCurrentlyEmployed(babysitter);
            addEmploymentPort.addEmployment(client, babysitter);
        } catch (AdapterException | EmploymentException exception) {
            throw new ServiceException(exception.getMessage(), exception);
        }
    }

    private void checkIfUserIsActive(Client client) {
        if (!client.isActive()) {
            throw new EmploymentException("Client is not active");
        }
    }

    private void checkIfBabysitterMeetsRequirements(Babysitter babysitter, int minAge,
                                                    int numberOfChildren) {
        if (babysitter.getMinChildAge() > minAge) {
            throw new EmploymentException("Babysitter does not meet requirements");
        }
        if (babysitter.getMaxNumberOfChildrenInTheFamily() < numberOfChildren) {
            throw new EmploymentException("Babysitter does not meet requirements");
        }
    }

    private void checkIfBabysitterIsCurrentlyEmployed(Babysitter babysitter) {
        if (babysitter.isEmployed()) {
            throw new EmploymentException("Babysitter already employed");
        }
    }


//    @Inject
//    private EmploymentsRepository employmentsRepository;
//
//    @Inject
//    private BabysittersService babysittersService;
//
//    public EmploymentsService(EmploymentsRepository employmentsRepository) {
//        this.employmentsRepository = employmentsRepository;
//    }
//
//    public EmploymentsService() {
//    }
//
//    private List<Employment> currentEmployments;
//
//    public List<Employment> getCurrentEmployments() {
//        return currentEmployments;
//    }
//
//    public void setCurrentEmployments(List<Employment> currentEmployments) {
//        this.currentEmployments = currentEmployments;
//    }
//
//    public void employBabysitter(Client client, Babysitter babysitter) {
//        checkIfUserIsActive(client);
//        checkIfBabysitterMeetsRequirements(babysitter, client.getAgeOfTheYoungestChild(),
//                client.getNumberOfChildren());
//        checkIfBabysitterIsCurrentlyEmployed(babysitter);
//
//        babysitter.setEmployed(true);
//        Employment employment = new Employment(babysitter, client);
//        employmentsRepository.addElement(employment);
//    }
//
//    public void checkIfBabysitterExists(Babysitter babysitter) {
//        babysittersService.getBabysittersRepository().findByKey(babysitter.getUuid());
//    }
//
//    private void checkIfUserIsActive(Client client) {
//        if (!client.isActive()) {
//            throw new EmploymentException("Client is not active");
//        }
//    }
//
//    public void checkIfBabysitterMeetsRequirements(Babysitter babysitter, int minAge,
//                                                   int numberOfChildren) {
//        if (babysitter.getMinChildAge() > minAge) {
//            throw new EmploymentException("Babysitter does not meet requirements");
//        }
//        if (babysitter.getMaxNumberOfChildrenInTheFamily() < numberOfChildren) {
//            throw new EmploymentException("Babysitter does not meet requirements");
//        }
//    }
//
//    public void checkIfBabysitterIsCurrentlyEmployed(Babysitter babysitter) {
//        if (babysitter.isEmployed()) {
//            throw new EmploymentException("Babysitter already employed");
//        }
//    }
//
//    public void endEmployment(Employment employment) {
//        if (employment.getEndOfEmployment() != null) {
//            throw new EmploymentException("Employment already ended");
//        }
//        employment.endEmployment();
//        employment.getBabysitter().setEmployed(false);
//    }
//
//    public EmploymentsRepository getEmploymentsRepository() {
//        return employmentsRepository;
//    }
//
//    public int numberOfCurrentClientEmployment(Client client) {
//        List<Employment> allEmploymentsList = employmentsRepository.getElements();
//        int counter = 0;
//
//        for (Employment employment : allEmploymentsList) {
//            if (employment.getClient() == client &&
//                    employment.getEndOfEmployment() == null) {
//                counter++;
//            }
//        }
//        return counter;
//    }
//
//    public int numberOfEndedClientEmployment(Client client) {
//        List<Employment> allEmploymentsList = employmentsRepository.getElements();
//        int counter = 0;
//
//        for (Employment employment : allEmploymentsList) {
//            if (employment.getClient() == client &&
//                    employment.getEndOfEmployment() != null) {
//                counter++;
//            }
//        }
//        return counter;
//    }
//
//    public Employment getActualEmploymentForBabysitter(Babysitter babysitter) {
//        List<Employment> allEmploymentsList = employmentsRepository.getElements();
//
//        for (Employment employment : allEmploymentsList) {
//            if (employment.getBabysitter() == babysitter &&
//                    employment.getEndOfEmployment() == null) {
//                return employment;
//            }
//        }
//        throw new EmploymentException("Babysitter is not already employed");
//    }
//
//    public List<Employment> getAllEmploymentsForBabysitter(Babysitter babysitter) {
//        List<Employment> allEmploymentsList = employmentsRepository.getElements();
//        List<Employment> allEmploymentsForBabysitterList = new ArrayList<>();
//
//        for (Employment employment : allEmploymentsList) {
//            if (employment.getBabysitter() == babysitter) {
//                allEmploymentsForBabysitterList.add(employment);
//            }
//        }
//        return allEmploymentsForBabysitterList;
//    }

//    public Employment getActualEmploymentForClient(Client client) {
//        List<Employment> allEmploymentsList = employmentsRepository.getElements();
//
//        for (Employment employment : allEmploymentsList) {
//            if (employment.getClient() == client && employment.getEndOfEmployment() == null) {
//                return employment;
//            }
//        }
//        throw new EmploymentException("Client does not have any actual employment");
//    }

//    public List<Employment> getActualEmploymentsForClient(Client client) {
//        List<Employment> actualEmploymentList = new ArrayList<>();
//        List<Employment> allEmploymentsList = employmentsRepository.getElements();
//        for (Employment employment : allEmploymentsList) {
//            if (employment.getClient() == client && employment.getEndOfEmployment() == null)
//                actualEmploymentList.add(employment);
//        }
//        return actualEmploymentList;
//    }
//
//    public List<Employment> getAllEmploymentsForClient(Client client) {
//        List<Employment> allEmploymentsList = employmentsRepository.getElements();
//        List<Employment> allEmploymentsForFamilyList = new ArrayList<>();
//
//        for (Employment employment : allEmploymentsList) {
//            if (employment.getClient() == client) {
//                allEmploymentsForFamilyList.add(employment);
//            }
//        }
//        return allEmploymentsForFamilyList;
//    }
//
//    public void deleteEmployment(Employment employment) {
//        employment.getBabysitter().setEmployed(false);
//        getEmploymentsRepository().deleteElement(employment);
//    }
//
//    public Babysitter[] getAllBabysittersConnectedToEmploymentArray() {
//        List<Babysitter> babysittersList = new ArrayList<>();
//        for (Employment employment : employmentsRepository.getElements()) {
//            if (!babysittersList.contains(employment.getBabysitter()) && employment.getBabysitter() != null) {
//                babysittersList.add(employment.getBabysitter());
//            }
//        }
//        return babysittersList.toArray(new Babysitter[0]);
//    }
//
//    public User[] getAllUsersWithEmploymentArray() {
//        List<User> userList = new ArrayList<>();
//        for (Employment employment : employmentsRepository.getElements()) {
//            if (!userList.contains(employment.getClient())) {
//                userList.add(employment.getClient());
//            }
//        }
//        return userList.toArray(new User[0]);
//    }
//
//    @PostConstruct
//    public void initCurrentPersons() {
//        currentEmployments = getEmploymentsRepository().getElements();
//    }

}
