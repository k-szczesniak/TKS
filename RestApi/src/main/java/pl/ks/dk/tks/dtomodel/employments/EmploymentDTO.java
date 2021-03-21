package pl.ks.dk.tks.dtomodel.employments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ks.dk.tks.dtomodel.exceptions.EmploymentExceptionDTO;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.users.ClientEnt;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;
@Getter
@Setter
@NoArgsConstructor
public class EmploymentDTO {
    private String uniqueID;
    private BabysitterEnt babysitter;
    private ClientEnt client;
    private LocalDateTime beginningOfEmployment;
    private LocalDateTime endOfEmployment;

    public EmploymentDTO(BabysitterEnt babysitter, ClientEnt client) {
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
        throw new EmploymentExceptionDTO("Employment has not been ended");
    }

    public double employmentCost() {
        if (isEnded()) {
            return employmentDurationInHours() * babysitter.getBasePriceForHour();
        }
        throw new EmploymentExceptionDTO("Employment has not been ended");
    }

    @Override
    public String toString() {
        return "uniqueID: " + uniqueID +
                "\nbabysitter: " + babysitter.getName() + " " + babysitter.getSurname() +
                "\nuser: " + client.getLogin() +
                "\nbeginningOfEmployment: " + beginningOfEmployment +
                "\nendOfEmployment: " + endOfEmployment;
    }
}
//TODO: DTO model nie zwraca hasła