package pl.ks.dk.tks.soapmodel.users;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://soap.adapters.kurs.pl/admin", name = "Admin")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminSOAP extends UserSOAP{
    public AdminSOAP() {
    }

    private String role;

    public AdminSOAP(String login, String name, String surname, String password, String role) {
        super(login, name, surname, password);
        this.role = role;
    }
}
