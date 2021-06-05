package pl.ks.dk.us.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {

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

    private Role role;

//    TODO: Zastanowić się nad BeanValidation, czy jest potrzebne jeżeli obiekt jest tworzony jako DTO

    public User(String login, String name, String surname, String password, String role) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = Role.valueOf(role);
    }

//    public void changeActive() {
//        isActive = !isActive;
//    }

    public String getRole() {
        return role.toString();
    }
}
