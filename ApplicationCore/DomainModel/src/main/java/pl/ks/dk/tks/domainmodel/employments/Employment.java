package pl.ks.dk.tks.domainmodel.employments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.exceptions.EmploymentException;
import pl.ks.dk.tks.domainmodel.users.Client;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//TODO: NA KONIEC: PRZENIESC METODY Z EMPLOYMENT DO MANAGERA

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

    public void endEmployment() {
        endOfEmployment = LocalDateTime.now();
    }

    public boolean isEnded() {
        return !(endOfEmployment == null);
    }

    public double employmentDurationInHours() {
        if (isEnded()) {
            long differenceInSeconds = ChronoUnit.SECONDS.between(beginningOfEmployment, endOfEmployment);
            return Math.ceil(differenceInSeconds / 3600.0);
        }
        throw new EmploymentException("Employment has not been ended");
    }

    public double employmentCost() {
        if (isEnded()) {
            return employmentDurationInHours() * babysitter.getBasePriceForHour();
        }
        throw new EmploymentException("Employment has not been ended");
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
