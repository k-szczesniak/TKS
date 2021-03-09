package com.mycompany.firstapplication.Controllers;

import com.mycompany.firstapplication.Exceptions.UserException;
import com.mycompany.firstapplication.Users.*;
import org.apache.commons.beanutils.BeanUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ResourceBundle;

@ConversationScoped
@Named
public class UsersController extends Conversational implements Serializable {

    @Inject
    private UsersManager usersManager;

    private final Client newClient = new Client();
    private final SuperUser newSuperUser = new SuperUser();
    private final Admin newAdmin = new Admin();

    private TypeOfUser typeOfUser = TypeOfUser.ADMIN;
    private List<User> currentUsers;
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle(
            "bundles/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());

    private User copyOfUser;
    private User originalUser;

    public UsersManager getUsersManager() {
        return usersManager;
    }

    public String modifyUser(User user) {
        beginNewConversation();
        try {
            copyOfUser = (User) user.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        originalUser = user;
        return "ModifyUser";
    }


    public Object getSomeUser(TypeOfUser type) {
        switch (type) {
            case ADMIN:
                return newAdmin;
            case SUPERUSER:
                return newSuperUser;
            default:
                return newClient;
        }
    }

    public TypeOfUser getTypeOfUser() {
        return typeOfUser;
    }

    public String userType(String string) {
        beginNewConversation();
        if (string.equals("ADMIN")) {
            this.typeOfUser = TypeOfUser.ADMIN;
        } else if (string.equals("SUPERUSER")) {
            this.typeOfUser = TypeOfUser.SUPERUSER;
        } else {
            this.typeOfUser = TypeOfUser.CLIENT;
        }
        return "NewUser";
    }


    public void loginValidator(FacesContext context, UIComponent component, Object value) {
        String login = (String) value;
        if (!login.matches("\\w{2,}"))
            throw new ValidatorException(new FacesMessage(resourceBundle.getString("validatorMessageLogin")));
        if (!usersManager.getUsersRepository().isLoginUnique(login, originalUser)) {
            throw new ValidatorException(new FacesMessage(resourceBundle.getString("validatorMessageLoginUsed")));
        }
    }

    public String processNewUser() {
        return "NewUserConfirm";
    }

    public String processModifiedUser() {
        return "ModifyUserConfirm";
    }

    public String confirmNewUser(TypeOfUser typeOfUser) {
        try {
            switch (typeOfUser) {
                case ADMIN:
                    usersManager.addUser(newAdmin);
                    break;
                case SUPERUSER:
                    usersManager.addUser(newSuperUser);
                    break;
                case CLIENT:
                    usersManager.addUser(newClient);
                    break;
            }
            return backToMain();
        } catch (UserException e) {
            throw new ValidatorException(new FacesMessage(resourceBundle.getString("validatorMessageLoginUsed")));
        }
    }

    public String modificationBackToMain() {
        try {
            BeanUtils.copyProperties(originalUser, copyOfUser);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        endCurrentConversation();
        return "main";
    }

    public String backToMain() {
        endCurrentConversation();
        return "main";
    }


    public String reject() {
        endCurrentConversation();
        return userType(typeOfUser.toString());
    }

    public void valueChangedId(ValueChangeEvent event) {
        if (!event.getNewValue().toString().equals("0")) {
            String id = event.getNewValue().toString();
            currentUsers = getUsersManager().getUsersRepository().showSelectedUser(id, true);
            setType();
        }
    }

    public void valueChangedLogin(ValueChangeEvent event) {
        if (!event.getNewValue().toString().equals("0")) {
            String login = event.getNewValue().toString();
            currentUsers = getUsersManager().getUsersRepository().showSelectedUser(login, false);
            setType();
        }
    }

    private void setType() {
        if (currentUsers.get(0) instanceof Client) {
            typeOfUser = TypeOfUser.CLIENT;
        } else {
            typeOfUser = TypeOfUser.ADMIN;
        }
    }

    public List<User> getCurrentUsers() {
        return currentUsers;
    }

    @PostConstruct
    public void initCurrentPersons() {
        currentUsers = usersManager.getUsersRepository().getUsersList();
    }

    public User getCopyOfUser() {
        return copyOfUser;
    }

    public User getOriginalUser() {
        return originalUser;
    }
}
