package pl.ks.dk.tks.domainmodel.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

    @NotNull
    @Pattern(regexp = "Admin")
    private String role;

    public Admin(String login, String name, String surname, String password, String role) {
        super(login, name, surname, password);
        this.role = role;
    }

}
