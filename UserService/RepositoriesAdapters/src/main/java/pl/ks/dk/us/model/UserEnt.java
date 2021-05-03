package pl.ks.dk.us.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserEnt {

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

    private String role;

//    TODO: Zastanowić się nad BeanValidation, czy jest potrzebne jeżeli obiekt jest tworzony jako DTO

    public UserEnt(String login, String name, String surname, String password, String role) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = role;
    }

    public void changeActive() {
        isActive = !isActive;
    }
}
