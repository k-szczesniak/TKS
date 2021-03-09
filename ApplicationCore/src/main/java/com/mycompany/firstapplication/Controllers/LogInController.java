package com.mycompany.firstapplication.Controllers;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Named
public class LogInController {
    private String username;
    private String password;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle(
            "bundles/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());

    public String logIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)
                context.getExternalContext().getRequest();
        try {
            request.login(username, password);
            logger.log(Level.INFO, resourceBundle.getString("correctLogIn"));
        } catch (ServletException e) {
            logger.log(Level.WARNING, resourceBundle.getString("incorrectLogIn"));
            return "ErrorPage";
        }
        return "main";
    }

    public String logOut() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)
                context.getExternalContext().getRequest();
        try {
            request.logout();
            logger.log(Level.INFO, resourceBundle.getString("correctLogOut"));
        } catch (ServletException e) {
            logger.log(Level.WARNING, resourceBundle.getString("incorrectLogOut"));
        }
        return "main";
    }

    public String backToMain() {
        return "main";
    }

    public String tryAgain() {
        return "LoginPage";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
