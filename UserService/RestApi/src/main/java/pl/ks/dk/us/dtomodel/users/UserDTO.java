package pl.ks.dk.us.dtomodel.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONPropertyIgnore;
import pl.ks.dk.us.dtomodel.interfaces.EntityToSignDTO;
import pl.ks.dk.us.users.Role;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements EntityToSignDTO, Serializable {

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

    private Integer numberOfChildren;

    private Integer ageOfTheYoungestChild;

    public UserDTO(String login, String name, String surname, String password, String role) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = Role.valueOf(role);
    }

    public Map<String, String> takePayload() {
        Map<String, String> map = new HashMap<>();
        map.put("uuid", getUuid());
        return map;
    }

    public String getRole() {
        return role.toString();
    }

    @JSONPropertyIgnore
    public String getPassword() {
        return password;
    }
}
