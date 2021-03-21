package pl.ks.dk.tks.dtomodel.babysitters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TeachingSitterDTO extends BabysitterDTO {

    @NotNull
    @Min(0)
    @Max(70)
    private Integer yearsOfExperienceInTeaching;

    public TeachingSitterDTO(String name, String surname, Double basePriceForHour, Integer minChildAge,
                             Integer maxNumberOfChildrenInTheFamily, Integer yearsOfExperienceInTeaching) {
        super(name, surname, basePriceForHour, minChildAge, maxNumberOfChildrenInTheFamily);
        this.yearsOfExperienceInTeaching = yearsOfExperienceInTeaching;
    }

    @Override
    public Double getBasePriceForHour() {
        return super.getBasePriceForHour() * (1 + yearsOfExperienceInTeaching / 10.0);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("yearsOfExperienceInTeaching", yearsOfExperienceInTeaching)
                .toString();
    }
}
