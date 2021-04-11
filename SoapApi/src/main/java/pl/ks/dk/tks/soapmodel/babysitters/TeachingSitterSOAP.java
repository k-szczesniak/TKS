package pl.ks.dk.tks.soapmodel.babysitters;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(namespace = "http://soap.adapters.kurs.pl/teachingsitter", name = "TeachingSitter" )
@XmlAccessorType(XmlAccessType.FIELD)
public class TeachingSitterSOAP extends BabysitterSOAP implements Serializable {

    private Integer yearsOfExperienceInTeaching;

    public TeachingSitterSOAP() {
    }

    public TeachingSitterSOAP(String name, String surname, Double basePriceForHour, Integer minChildAge,
                              Integer maxNumberOfChildrenInTheFamily, Integer yearsOfExperienceInTeaching) {
        super(name, surname, basePriceForHour, minChildAge, maxNumberOfChildrenInTheFamily);
        this.yearsOfExperienceInTeaching = yearsOfExperienceInTeaching;
    }

    @Override
    public Double getBasePriceForHour() {
        return super.getBasePriceForHour() * (1 + yearsOfExperienceInTeaching / 10.0);
    }

    public Integer getYearsOfExperienceInTeaching() {
        return yearsOfExperienceInTeaching;
    }

    public void setYearsOfExperienceInTeaching(Integer yearsOfExperienceInTeaching) {
        this.yearsOfExperienceInTeaching = yearsOfExperienceInTeaching;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("yearsOfExperienceInTeaching", yearsOfExperienceInTeaching)
                .toString();
    }
}
