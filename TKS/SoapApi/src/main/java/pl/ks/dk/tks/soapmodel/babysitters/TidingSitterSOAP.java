package pl.ks.dk.tks.soapmodel.babysitters;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(namespace = "http://soap.adapters.kurs.pl/tidingsitter", name = "TidingSitter")
@XmlAccessorType(XmlAccessType.FIELD)
public class TidingSitterSOAP extends BabysitterSOAP implements Serializable {

    private Double valueOfCleaningEquipment;

    public TidingSitterSOAP() {
    }

    public TidingSitterSOAP(String name, String surname, Double basePriceForHour, Integer minChildAge,
                            Integer maxNumberOfChildrenInTheFamily, double valueOfCleaningEquipment) {
        super(name, surname, basePriceForHour, minChildAge, maxNumberOfChildrenInTheFamily);
        this.valueOfCleaningEquipment = valueOfCleaningEquipment;
    }

    @Override
    public Double getBasePriceForHour() {
        return super.getBasePriceForHour() * (1 + valueOfCleaningEquipment / 500.0);
    }

    public Double getValueOfCleaningEquipment() {
        return valueOfCleaningEquipment;
    }

    public void setValueOfCleaningEquipment(Double valueOfCleaningEquipment) {
        this.valueOfCleaningEquipment = valueOfCleaningEquipment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("valueOfCleaningEquipment", valueOfCleaningEquipment)
                .toString();
    }
}
