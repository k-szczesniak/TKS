package pl.ks.dk.tks.soapmodel.employments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ks.dk.tks.soapmodel.babysitters.BabysitterSOAP;
import pl.ks.dk.tks.soapmodel.users.ClientSOAP;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EmploymentSOAP {
    private String uuid;
    private BabysitterSOAP babysitterSOAP;
    private ClientSOAP clientSOAP;
    private LocalDateTime beginningOfEmployment;
    private LocalDateTime endOfEmployment;

    public EmploymentSOAP(BabysitterSOAP babysitter, ClientSOAP client) {
        this.babysitterSOAP = babysitter;
        this.clientSOAP = client;
        beginningOfEmployment = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "uniqueID: " + uuid +
                "\nbabysitter: " + babysitterSOAP.getName() + " " + babysitterSOAP.getSurname() +
                "\nuser: " + clientSOAP.getLogin() +
                "\nbeginningOfEmployment: " + beginningOfEmployment +
                "\nendOfEmployment: " + endOfEmployment;
    }
}
