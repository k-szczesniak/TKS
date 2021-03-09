package com.mycompany.firstapplication.Babysitters;

import com.mycompany.firstapplication.Exceptions.BabysitterException;
import com.mycompany.firstapplication.Interfaces.EntityToSign;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

public class Babysitter implements Cloneable, EntityToSign {

    @NotNull
    @Size(min=2, max=20)
    private String name;

    @NotNull
    @Size(min=2, max=20)
    private String surname;

    @NotNull
    @Min(0)
    @Max(1000)
    private Integer basePriceForHour;

    @NotNull
    @Min(0)
    @Max(15)
    private Integer minChildAge;

    @NotNull
    @Min(0)
    @Max(15)
    private Integer maxNumberOfChildrenInTheFamily;

    @AssertFalse
    private boolean employed;

    private String uniqueID;

    public Babysitter() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Babysitter(String name, String surname, Integer basePriceForHour, Integer minChildAge,
                      int maxNumberOfChildrenInTheFamily) {
        this.name = name;
        this.surname = surname;
        this.basePriceForHour = basePriceForHour;
        this.minChildAge = minChildAge;
        this.maxNumberOfChildrenInTheFamily = maxNumberOfChildrenInTheFamily;
        if (basePriceForHour <= 0 || minChildAge < 0 || maxNumberOfChildrenInTheFamily <= 0) {
            throw new BabysitterException("Invalid argument");
        }
    }

    public double priceForHour() {
        return basePriceForHour;
    }

    public Integer getYearsOfExperienceInTeaching() {
        return null;
    }

    public Double getValueOfCleaningEquipment() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBasePriceForHour(Integer basePriceForHour) {
        this.basePriceForHour = basePriceForHour;
    }

    public void setMinChildAge(Integer minChildAge) {
        this.minChildAge = minChildAge;
    }

    public void setMaxNumberOfChildrenInTheFamily(Integer maxNumberOfChildrenInTheFamily) {
        this.maxNumberOfChildrenInTheFamily = maxNumberOfChildrenInTheFamily;
    }

    public boolean isEmployed() {
        return employed;
    }

    public void setEmployed(boolean employed) {
        this.employed = employed;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getBasePriceForHour() {
        return basePriceForHour;
    }

    public Integer getMinChildAge() {
        return minChildAge;
    }

    public Integer getMaxNumberOfChildrenInTheFamily() {
        return maxNumberOfChildrenInTheFamily;
    }

    public String getUuid() {
        return uniqueID;
    }

    public void setUuid(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    @Override
    public String toString() {
        return "name: " + name +
                "\nsurname: " + surname +
                "\nbasePrice: " + basePriceForHour +
                "\nminChildAge: " + minChildAge +
                "\nmaxNumberOfChildrenInTheFamily: " + minChildAge;
    }

    @Override
    public Map<String, String> takePayload() {
        Map<String, String> map = new HashMap<>();
        map.put("uuid", getUuid());
        map.put("employed", String.valueOf(isEmployed()));
        return map;
    }
}
