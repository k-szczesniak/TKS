package com.mycompany.firstapplication.Users;

import com.mycompany.firstapplication.Exceptions.UserException;
import com.mycompany.firstapplication.Interfaces.EntityToSign;
import com.nimbusds.jose.shaded.json.JSONObject;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

public abstract class User implements Cloneable, EntityToSign {

    private boolean isActive = true;

    @NotNull
    @Size(min = 2, max = 20)
    private String login;

    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    @NotNull
    @Size(min = 2, max = 20)
    private String surname;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    private String uniqueID;

    public User() {
    }

    public User(String login, String name, String surname, String password) {
        if (login.isEmpty() || name.isEmpty() || surname.isEmpty() || password.isEmpty()) {
            throw new UserException("Empty login, name, surname or password");
        }
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public void changeActive() {
        isActive = !isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getNumberOfChildren() {
        return null;
    }

    public Integer getAgeOfTheYoungestChild() {
        return null;
    }

    public String getUuid() {
        return uniqueID;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUuid(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getRole() {
        return "Client";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Map<String, String> takePayload() {
        Map<String, String> map = new HashMap<>();
        map.put("uuid", getUuid());
        return map;
    }
}
