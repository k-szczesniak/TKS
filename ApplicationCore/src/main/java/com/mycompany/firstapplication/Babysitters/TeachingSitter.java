package com.mycompany.firstapplication.Babysitters;

import com.mycompany.firstapplication.Exceptions.BabysitterException;
import com.mycompany.firstapplication.Interfaces.EntityToSign;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class TeachingSitter extends Babysitter {

    @NotNull
    @Min(0)
    @Max(70)
    private Integer yearsOfExperienceInTeaching;

    public TeachingSitter() {
    }

    public TeachingSitter(String name, String surname, Integer basePriceForHour, Integer minChildAge,
                          Integer maxNumberOfChildrenInTheFamily, Integer yearsOfExperienceInTeaching) {
        super(name, surname, basePriceForHour, minChildAge, maxNumberOfChildrenInTheFamily);
        this.yearsOfExperienceInTeaching = yearsOfExperienceInTeaching;
        if (yearsOfExperienceInTeaching < 0) {
            throw new BabysitterException("Invalid argument");
        }
    }

    @Override
    public double priceForHour() {
        return getBasePriceForHour() * (1 + yearsOfExperienceInTeaching / 10.0);
    }

    public void setYearsOfExperienceInTeaching(Integer yearsOfExperienceInTeaching) {
        this.yearsOfExperienceInTeaching = yearsOfExperienceInTeaching;
    }

    @Override
    public Integer getYearsOfExperienceInTeaching() {
        return yearsOfExperienceInTeaching;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("yearsOfExperienceInTeaching", yearsOfExperienceInTeaching)
                .toString();
    }
}
