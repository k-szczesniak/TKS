package com.mycompany.firstapplication.Users;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

public class Client extends User {

    @NotNull
    @Min(0)
    @Max(15)
    private Integer numberOfChildren;

    @NotNull
    @Min(0)
    @Max(15)
    private Integer ageOfTheYoungestChild;

    @NotNull
    @Pattern(regexp = "Client")
    private String role;

    public Client() {
    }

    public Client(String login, String name, String surname, String password, String role, int numberOfChildren,
                  int ageOfTheYoungestChild) {
        super(login, name, surname, password);
        this.role = role;
        this.numberOfChildren = numberOfChildren;
        this.ageOfTheYoungestChild = ageOfTheYoungestChild;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public void setAgeOfTheYoungestChild(Integer ageOfTheYoungestChild) {
        this.ageOfTheYoungestChild = ageOfTheYoungestChild;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public Integer getAgeOfTheYoungestChild() {
        return ageOfTheYoungestChild;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Map<String, String> takePayload() {
        return super.takePayload();
    }
}
