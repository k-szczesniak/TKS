package pl.ks.dk.tks.domainmodel.employments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.exceptions.EmploymentException;
import pl.ks.dk.tks.domainmodel.users.Client;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
public class Employment {

    private String uuid;
    private Babysitter babysitter;
    private Client client;
    private LocalDateTime beginningOfEmployment;
    private LocalDateTime endOfEmployment;

    public Employment(Babysitter babysitter, Client client) {
        this.babysitter = babysitter;
        this.client = client;
        beginningOfEmployment = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "uniqueID: " + uuid +
                "\nbabysitter: " + babysitter.getName() + " " + babysitter.getSurname() +
                "\nuser: " + client.getLogin() +
                "\nbeginningOfEmployment: " + beginningOfEmployment +
                "\nendOfEmployment: " + endOfEmployment;
    }
}
