package pl.ks.dk.tks.dtomodel.babysitters;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class TidingSitterDTO extends BabysitterDTO {

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("20000.00")
    private Double valueOfCleaningEquipment;

    public TidingSitterDTO() {
    }

    public TidingSitterDTO(String name, String surname, Integer basePriceForHour, Integer minChildAge,
                           Integer maxNumberOfChildrenInTheFamily, double valueOfCleaningEquipment) {
        super(name, surname, basePriceForHour, minChildAge, maxNumberOfChildrenInTheFamily);
        this.valueOfCleaningEquipment = valueOfCleaningEquipment;
    }

    @Override
    public double priceForHour() {
        return getBasePriceForHour() * (1 + valueOfCleaningEquipment / 500.0);
    }

    public void setValueOfCleaningEquipment(Double valueOfCleaningEquipment) {
        this.valueOfCleaningEquipment = valueOfCleaningEquipment;
    }

    @Override
    public Double getValueOfCleaningEquipment() {
        return valueOfCleaningEquipment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("valueOfCleaningEquipment", valueOfCleaningEquipment)
                .toString();
    }
}
