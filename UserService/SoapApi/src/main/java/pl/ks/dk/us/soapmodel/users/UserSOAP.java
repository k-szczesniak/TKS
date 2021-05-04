package pl.ks.dk.us.soapmodel.users;

import pl.ks.dk.us.users.Role;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://soap.adapters.kurs.pl/user", name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserSOAP {

    public UserSOAP() {
    }

    private boolean isActive = true;

    private String login;

    private String name;

    private String surname;

    private String password;

    private String uuid;

    private Role role;

    public UserSOAP(String login, String name, String surname, String password, String role) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = Role.valueOf(role);
    }

    public void changeActive() {
        isActive = !isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRole() {
        return role.toString();
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
