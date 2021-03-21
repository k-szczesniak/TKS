package pl.ks.dk.tks.dtomodel.employments;

import pl.ks.dk.tks.dtomodel.exceptions.EmploymentExceptionDTO;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.users.ClientEnt;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public class EmploymentDTO {
    private String uniqueID;
    private BabysitterEnt babysitter;
    private ClientEnt client;
    private LocalDateTime beginningOfEmployment;
    private LocalDateTime endOfEmployment;


    public EmploymentDTO() {

    }

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
            return employmentDurationInHours() * babysitter.priceForHour();
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

    public void setEndOfEmployment(LocalDateTime endOfEmployment) {
        this.endOfEmployment = endOfEmployment;
    }

    public LocalDateTime getEndOfEmployment() {
        return endOfEmployment;
    }

    public BabysitterEnt getBabysitter() {
        return babysitter;
    }

    public ClientEnt getClient() {
        return client;
    }

    public String getUuid() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setBabysitter(BabysitterEnt babysitter) {
        this.babysitter = babysitter;
    }

    public LocalDateTime getBeginningOfEmployment() {
        return beginningOfEmployment;
    }
}