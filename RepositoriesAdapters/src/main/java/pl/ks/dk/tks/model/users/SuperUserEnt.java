package pl.ks.dk.tks.model.users;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

public class SuperUserEnt extends UserEnt {

    @NotNull
    @Pattern(regexp = "SuperUser")
    private String role;

    public SuperUserEnt() {
    }

    public SuperUserEnt(String login, String name, String surname, String password, String role) {
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
