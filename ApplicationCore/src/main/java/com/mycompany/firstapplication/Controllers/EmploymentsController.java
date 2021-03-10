package com.mycompany.firstapplication.Controllers;

import com.mycompany.firstapplication.Babysitters.Babysitter;
import com.mycompany.firstapplication.Employment.Employment;
import com.mycompany.firstapplication.services.EmploymentsService;
import com.mycompany.firstapplication.Exceptions.EmploymentException;
import com.mycompany.firstapplication.Exceptions.RepositoryException;
import com.mycompany.firstapplication.Users.Client;
import com.mycompany.firstapplication.services.UsersService;
import com.mycompany.firstapplication.utils.IdentityUtils;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@ConversationScoped
@Named
public class EmploymentsController extends Conversational implements Serializable {

    @Inject
    private EmploymentsService employmentsService;

    @Inject
    private UsersService usersService;

    @Inject
    private IdentityUtils identityUtils;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Babysitter currentBabysitter;
    private Client currentClient;

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle(
            "bundles/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());

    public String processNewEmployment() {
        if (identityUtils.isInClientRole()) {
            setCurrentClient((Client) usersService.getUsersRepository()
                    .findUserByLogin(identityUtils.getUserLogin()));
        }
        beginNewConversation();
        return "NewEmploymentConfirm";
    }

    public String confirmNewEmployment() {
        int errorNo = 0;
        try {
            employmentsService.checkIfBabysitterExists(currentBabysitter);
            employmentsService.checkIfBabysitterIsCurrentlyEmployed(currentBabysitter);
            errorNo++;
            employmentsService.employBabysitter(currentClient, currentBabysitter);
        } catch (EmploymentException e) {
            if (errorNo == 0) {
                FacesContext.getCurrentInstance().addMessage(
                        "newEmploymentConfirm:validationLabel",
                        new FacesMessage(
                                resourceBundle
                                        .getString("ValidatorMessageBabysitterAlreadyEmployed")));
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        "newEmploymentConfirm:validationLabel",
                        new FacesMessage(
                                resourceBundle
                                        .getString("ValidatorMessageEmploymentRequirements")));
            }
            return "";
        } catch (RepositoryException e) {
            FacesContext.getCurrentInstance().addMessage(
                    "newEmploymentConfirm:validationLabel",
                    new FacesMessage(
                            resourceBundle.getString("ValidatorMessageBabysitterDoesNotExist")));
            return "";
        }
        return reject();
    }

    public void valueChangedUser(ValueChangeEvent event) {
        if (!event.getNewValue().toString().equals("0")) {
            String id = event.getNewValue().toString();
            employmentsService.setCurrentEmployments(
                    employmentsService.getEmploymentsRepository().showSelected(id));
        }
    }

    public void valueChangedBabysitter(ValueChangeEvent event) {
        if (!event.getNewValue().toString().equals("0")) {
            String id = event.getNewValue().toString();
            employmentsService.setCurrentEmployments(
                    employmentsService.getEmploymentsRepository().showSelected(id));
        }
    }

    public EmploymentsService getEmploymentsManager() {
        return employmentsService;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public Babysitter getCurrentBabysitter() {
        return currentBabysitter;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentBabysitter(Babysitter currentBabysitter) {
        this.currentBabysitter = currentBabysitter;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public void setCurrentEmployments(List<Employment> currentEmployments) {
        employmentsService.setCurrentEmployments(currentEmployments);
    }

    public void employmentValidation(FacesContext context, UIComponent component, Object value) {
        try {
            employmentsService.checkIfBabysitterMeetsRequirements(currentBabysitter,
                    currentClient.getAgeOfTheYoungestChild(),
                    currentClient.getNumberOfChildren());
        } catch (EmploymentException exception) {

            throw new ValidatorException(new FacesMessage(
                    resourceBundle.getString("ValidatorMessageEmploymentRequirements")));
        }

        if (currentBabysitter.isEmployed())
            throw new ValidatorException(new FacesMessage(
                    resourceBundle.getString("ValidatorMessageBabysitterAlreadyEmployed")));
    }


    public String deleteEmployment(Employment employment) {
        try {
            employmentsService.deleteEmployment(employment);
        } catch (RepositoryException exception) {
            FacesContext.getCurrentInstance().addMessage(
                    "EmploymentList:errorLabel",
                    new FacesMessage(resourceBundle.getString("employmentDoesNotExist")));
            return "";
        }
        return "EmploymentList";
    }

    public String reject() {
        endCurrentConversation();
        return "main";
    }

    public Employment getActualEmploymentForClientOrNull(Client client) {
        try {
            return employmentsService.getActualEmploymentForClient(client);
        } catch (EmploymentException e) {
            return null;
        }
    }

    public Employment getActualEmploymentForBabysitterOrNull(Babysitter babysitter) {
        try {
            return employmentsService.getActualEmploymentForBabysitter(babysitter);
        } catch (EmploymentException e) {
            return null;
        }
    }

    public List<Employment> getCurrentEmployments() {
        return employmentsService.getCurrentEmployments();
    }

    public void refreshCurrent() {
        employmentsService.initCurrentPersons();
    }
}
