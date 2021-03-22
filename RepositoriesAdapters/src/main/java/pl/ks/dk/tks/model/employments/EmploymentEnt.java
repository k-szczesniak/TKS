package pl.ks.dk.tks.model.employments;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.exceptions.EmploymentExceptionEnt;
import pl.ks.dk.tks.model.users.ClientEnt;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

@Getter
@Setter
@NoArgsConstructor
public class EmploymentEnt {

    private String uuid;
    private BabysitterEnt babysitter;
    private ClientEnt client;
    private LocalDateTime beginningOfEmployment;
    private LocalDateTime endOfEmployment;

    public EmploymentEnt(BabysitterEnt babysitter, ClientEnt client) {
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
            long differenceInSeconds = SECONDS.between(beginningOfEmployment, endOfEmployment);
            return Math.ceil(differenceInSeconds / 3600.0);
        }
        throw new EmploymentExceptionEnt("Employment has not been ended");
    }

    public double employmentCost() {
        if (isEnded()) {
            return employmentDurationInHours() * babysitter.getBasePriceForHour();
        }
        throw new EmploymentExceptionEnt("Employment has not been ended");
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
