package pl.ks.dk.tks.soapmodel.babysitters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(namespace = "http://soap.adapters.kurs.pl/babysitter", name = "Babysitter")
@XmlAccessorType(XmlAccessType.FIELD)
public class BabysitterSOAP implements Serializable {

    private String name;

    private String surname;

    private Double basePriceForHour;

    private Integer minChildAge;

    private Integer maxNumberOfChildrenInTheFamily;

    private boolean employed;

    private String uuid;

    public BabysitterSOAP() {
    }

    public BabysitterSOAP(String name, String surname, Double basePriceForHour, Integer minChildAge,
                          int maxNumberOfChildrenInTheFamily) {
        this.name = name;
        this.surname = surname;
        this.basePriceForHour = basePriceForHour;
        this.minChildAge = minChildAge;
        this.maxNumberOfChildrenInTheFamily = maxNumberOfChildrenInTheFamily;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Double getBasePriceForHour() {
        return basePriceForHour;
    }

    public void setBasePriceForHour(Double basePriceForHour) {
        this.basePriceForHour = basePriceForHour;
    }

    public Integer getMinChildAge() {
        return minChildAge;
    }

    public void setMinChildAge(Integer minChildAge) {
        this.minChildAge = minChildAge;
    }

    public Integer getMaxNumberOfChildrenInTheFamily() {
        return maxNumberOfChildrenInTheFamily;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "name: " + name +
                "\nsurname: " + surname +
                "\nbasePrice: " + basePriceForHour +
                "\nminChildAge: " + minChildAge +
                "\nmaxNumberOfChildrenInTheFamily: " + minChildAge;
    }
}
