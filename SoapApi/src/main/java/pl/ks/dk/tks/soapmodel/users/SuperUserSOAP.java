package pl.ks.dk.tks.soapmodel.users;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://soap.adapters.kurs.pl/superuser", name = "SuperUser")
@XmlAccessorType(XmlAccessType.FIELD)
public class SuperUserSOAP extends UserSOAP{
    public SuperUserSOAP() {
    }

    private String role;

    @Override
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public SuperUserSOAP(String login, String name, String surname, String password, String role) {
        super(login, name, surname, password);
        this.role = role;
    }
}
