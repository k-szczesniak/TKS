package com.mycompany.firstapplication.Users;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

public class Admin extends User {

    @NotNull
    @Pattern(regexp = "Admin")
    private String role;

    public Admin() {
    }

    public Admin(String login, String name, String surname, String password, String role) {
        super(login, name, surname, password);
        this.role = role;
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
