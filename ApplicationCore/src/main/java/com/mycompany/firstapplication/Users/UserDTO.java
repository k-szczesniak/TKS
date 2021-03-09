package com.mycompany.firstapplication.Users;

public class UserDTO {

    private final String uuid;
    private final String login;
    private final String role;
    private final String name;
    private final String surname;
    private Integer numberOfChildren;
    private Integer ageOfTheYoungestChild;

    public UserDTO(String uuid, String login, String role, String name, String surname) {
        this.uuid = uuid;
        this.login = login;
        this.role = role;
        this.name = name;
        this.surname = surname;
    }

    public UserDTO(String uuid, String login, String role, String name, String surname, int numberOfChildren, int ageOfTheYoungestChild) {
        this.uuid = uuid;
        this.login = login;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.numberOfChildren = numberOfChildren;
        this.ageOfTheYoungestChild = ageOfTheYoungestChild;
    }

    public String getUuid() {
        return uuid;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public Integer getAgeOfTheYoungestChild() {
        return ageOfTheYoungestChild;
    }
}
