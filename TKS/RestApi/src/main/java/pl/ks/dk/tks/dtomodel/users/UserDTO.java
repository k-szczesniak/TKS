package pl.ks.dk.tks.dtomodel.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONPropertyIgnore;
import pl.ks.dk.tks.dtomodel.interfaces.EntityToSignDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public abstract class UserDTO implements EntityToSignDTO, Serializable {

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

    public UserDTO(String login, String name, String surname, String password) {
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

    public Map<String, String> takePayload() {
        Map<String, String> map = new HashMap<>();
        map.put("uuid", getUuid());
        return map;
    }

    @JSONPropertyIgnore
    public String getPassword() {
        return password;
    }
}
