package pl.ks.dk.tks.soapmodel.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class UserSOAP {
    private boolean isActive = true;

    private String login;

    private String name;

    private String surname;

    private String password;

    private String uuid;

    public UserSOAP(String login, String name, String surname, String password) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public void changeActive() {
        isActive = !isActive;
    }

    public String getRole() {
        return "Client";
    }
}
