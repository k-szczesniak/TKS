package pl.ks.dk.tks.domainmodel.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class SuperUser extends User {

    @NotNull
    @Pattern(regexp = "SuperUser")
    private String role;

    public SuperUser(String login, String name, String surname, String password, String role) {
        super(login, name, surname, password);
        this.role = role;
    }
}
