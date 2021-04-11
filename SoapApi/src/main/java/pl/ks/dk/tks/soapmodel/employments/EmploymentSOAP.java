package pl.ks.dk.tks.soapmodel.employments;

import pl.ks.dk.tks.soapadapters.LocalDateXmlAdapter;
import pl.ks.dk.tks.soapmodel.babysitters.BabysitterSOAP;
import pl.ks.dk.tks.soapmodel.users.ClientSOAP;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlType(namespace = "http://soap.adapters.kurs.pl/employment", name = "Employment")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmploymentSOAP {

    public EmploymentSOAP() {
    }

    private String uuid;

    private BabysitterSOAP babysitterSOAP;

    private ClientSOAP clientSOAP;

    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    private LocalDateTime beginningOfEmployment;

    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    private LocalDateTime endOfEmployment;

    public EmploymentSOAP(BabysitterSOAP babysitter, ClientSOAP client) {
        this.babysitterSOAP = babysitter;
        this.clientSOAP = client;
        beginningOfEmployment = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "uniqueID: " + uuid +
                "\nbabysitter: " + babysitterSOAP.getName() + " " + babysitterSOAP.getSurname() +
                "\nuser: " + clientSOAP.getLogin() +
                "\nbeginningOfEmployment: " + beginningOfEmployment +
                "\nendOfEmployment: " + endOfEmployment;
    }
}
