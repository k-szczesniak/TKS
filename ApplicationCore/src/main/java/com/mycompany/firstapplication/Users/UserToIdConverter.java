package com.mycompany.firstapplication.Users;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class UserToIdConverter implements Converter<Client>, Serializable {

    @Inject
    private UsersManager usersManager;

    @Override
    public Client getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return (Client) usersManager.getUsersRepository().findUserByUuid(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Client client) {
        return client.getUuid();
    }
}
