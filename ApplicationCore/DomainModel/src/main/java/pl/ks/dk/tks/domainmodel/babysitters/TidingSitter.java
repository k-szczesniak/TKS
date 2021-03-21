package pl.ks.dk.tks.domainmodel.babysitters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TidingSitter extends Babysitter {

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("20000.00")
    private Double valueOfCleaningEquipment;

    public TidingSitter(String name, String surname, Double basePriceForHour, Integer minChildAge,
                        Integer maxNumberOfChildrenInTheFamily, double valueOfCleaningEquipment) {
        super(name, surname, basePriceForHour, minChildAge, maxNumberOfChildrenInTheFamily);
        this.valueOfCleaningEquipment = valueOfCleaningEquipment;
    }

    @Override
    public Double getBasePriceForHour() {
        return super.getBasePriceForHour() * (1 + valueOfCleaningEquipment / 500.0);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("valueOfCleaningEquipment", valueOfCleaningEquipment)
                .toString();
    }
}
