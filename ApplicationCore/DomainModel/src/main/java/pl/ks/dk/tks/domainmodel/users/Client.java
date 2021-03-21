package pl.ks.dk.tks.domainmodel.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class Client extends User {

    @NotNull
    @Min(0)
    @Max(15)
    private Integer numberOfChildren;

    @NotNull
    @Min(0)
    @Max(15)
    private Integer ageOfTheYoungestChild;

    @NotNull
    @Pattern(regexp = "Client")
    private String role;

    public Client(String login, String name, String surname, String password, String role, int numberOfChildren,
                  int ageOfTheYoungestChild) {
        super(login, name, surname, password);
        this.role = role;
        this.numberOfChildren = numberOfChildren;
        this.ageOfTheYoungestChild = ageOfTheYoungestChild;
    }

}
