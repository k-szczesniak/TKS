package pl.ks.dk.tks.domainmodel.users;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SuperUser extends User {

    @NotNull
    @Pattern(regexp = "SuperUser")
    private String role;

    public SuperUser() {
    }

    public SuperUser(String login, String name, String surname, String password, String role) {
        super(login, name, surname, password);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
