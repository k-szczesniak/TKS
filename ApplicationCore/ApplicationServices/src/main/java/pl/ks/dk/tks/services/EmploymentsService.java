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
    public List<Employment> getActualEmploymentsForClient(String key) {
        return getEmploymentPort.getActualEmploymentsForClient(key);
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
}
