package pl.ks.dk.tks.soapmodel.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientSOAP extends UserSOAP{
    private Integer numberOfChildren;

    private Integer ageOfTheYoungestChild;

    private String role;

    public ClientSOAP(String login, String name, String surname, String password, String role, int numberOfChildren,
                  int ageOfTheYoungestChild) {
        super(login, name, surname, password);
        this.role = role;
        this.numberOfChildren = numberOfChildren;
        this.ageOfTheYoungestChild = ageOfTheYoungestChild;
    }
}
