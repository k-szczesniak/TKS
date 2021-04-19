package pl.ks.dk.tks.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SuperUserEnt extends UserEnt {

    @NotNull
    @Pattern(regexp = "SuperUser")
    private String role;

    public SuperUserEnt(String login, String name, String surname, String password, String role) {
        super(login, name, surname, password);
        this.role = role;
    }

    @Override
    public Map<String, String> takePayload() {
        return super.takePayload();
    }
}
