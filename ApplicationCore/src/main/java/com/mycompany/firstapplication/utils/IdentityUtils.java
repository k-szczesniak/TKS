package com.mycompany.firstapplication.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@RequestScoped
@Named
public class IdentityUtils implements Serializable {
    @Inject
    private HttpServletRequest request;

    private ResourceBundle resourceBundle;


    public String getWelcomeSentence() {
        loadBundles();
        return (null == request.getUserPrincipal()) ?
                resourceBundle.getString("welcome").concat(resourceBundle.getString("guest")) :
                resourceBundle.getString("welcome").concat(request.getUserPrincipal().getName());
    }

    public String getUserLogin() {
        return request.getUserPrincipal().getName();
    }

    public boolean isLoggedIn() {
        return request.getUserPrincipal() != null;
    }

    public boolean isInAdminRole() {
        return request.isUserInRole("Admin");
    }

    public boolean isInSuperUserRole() {
        return request.isUserInRole("SuperUser");
    }

    public boolean isInClientRole() {
        return request.isUserInRole("Client");
    }

    public String userRole() {
        List<String> roles = Arrays.asList("Admin", "SuperUser", "Client");
        for (String role : roles) {
            if (request.isUserInRole(role)) {
                return resourceBundle.getString("role").concat(role);
            }
        }
        return "";
    }

    private void loadBundles() {
        resourceBundle = ResourceBundle.getBundle(
                "bundles/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    }
}
