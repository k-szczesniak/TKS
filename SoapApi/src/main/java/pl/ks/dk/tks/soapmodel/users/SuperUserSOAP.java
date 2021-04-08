package pl.ks.dk.tks.soapmodel.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuperUserSOAP extends UserSOAP{
    private String role;

    public SuperUserSOAP(String login, String name, String surname, String password, String role) {
        super(login, name, surname, password);
        this.role = role;
    }
}
