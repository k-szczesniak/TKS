package pl.ks.dk.tks.domainmodel.users;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public abstract class User {

    private boolean isActive = true;

    @NotNull
    @Size(min = 2, max = 20)
    private String login;

    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    @NotNull
    @Size(min = 2, max = 20)
    private String surname;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    private String uuid;

    public User(String login, String name, String surname, String password) {
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
