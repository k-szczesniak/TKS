package com.mycompany.firstapplication.Employment;

import com.mycompany.firstapplication.Babysitters.Babysitter;
import com.mycompany.firstapplication.Exceptions.EmploymentException;
import com.mycompany.firstapplication.Users.Client;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Employment {

    private String uniqueID;
    private Babysitter babysitter;
    private Client client;
    private LocalDateTime beginningOfEmployment;
    private LocalDateTime endOfEmployment;


    public Employment() {

    }

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
            long differenceInSeconds = SECONDS.between(beginningOfEmployment, endOfEmployment);
            return Math.ceil(differenceInSeconds / 3600.0);
        }
        throw new EmploymentException("Employment has not been ended");
    }

    public double employmentCost() {
        if (isEnded()) {
            return employmentDurationInHours() * babysitter.priceForHour();
        }
        throw new EmploymentException("Employment has not been ended");
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

    public Babysitter getBabysitter() {
        return babysitter;
    }

    public Client getClient() {
        return client;
    }

    public String getUuid() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setBabysitter(Babysitter babysitter) {
        this.babysitter = babysitter;
    }

    public LocalDateTime getBeginningOfEmployment() {
        return beginningOfEmployment;
    }
}
