package pl.ks.dk.tks.soapmodel.users;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://soap.adapters.kurs.pl/client", name = "Client")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientSOAP extends UserSOAP{
    public ClientSOAP() {
    }

    private Integer numberOfChildren;

    private Integer ageOfTheYoungestChild;

    private String role;

    public ClientSOAP(String login, String name, String surname, String password, String role, int numberOfChildren,
                  int ageOfTheYoungestChild) {
        super(login, name, surname, password);
        this.role = role;
        this.numberOfChildren = numberOfChildren;
        this.ageOfTheYoungestChild = ageOfTheYoungestChild;
    }
}
