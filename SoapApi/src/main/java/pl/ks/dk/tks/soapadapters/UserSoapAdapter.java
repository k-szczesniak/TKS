package pl.ks.dk.tks.soapadapters;

import jakarta.inject.Inject;
import pl.ks.dk.tks.soapmodel.users.AdminSOAP;
import pl.ks.dk.tks.soapmodel.users.ClientSOAP;
import pl.ks.dk.tks.soapmodel.users.SuperUserSOAP;
import pl.ks.dk.tks.soapmodel.users.UserSOAP;
import pl.ks.dk.tks.userinterface.soap.UserSoapUseCase;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "ClientAPI")
public class UserSoapAdapter {

    @Inject
    private UserSoapUseCase userSoapUseCase;

//    public UserSOAP getClient(String uuid) {
//        return userSoapUseCase.getUserByKey(uuid);
//    }
//
    @WebMethod
    public int getClientCount() {
        return userSoapUseCase.getUsersCount();
    }
//
//    public List<UserSOAP> getAllUsers() {
//        return userSoapUseCase.getAllUsers();
//    }
//
//    public void createAdmin(AdminSOAP adminSOAP) {
//        return userSoapUseCase.addUser(adminSOAP);
//    }
//
//    public void createSuperUser(SuperUserSOAP superUserSOAP) {
//        return userSoapUseCase.addUser(superUserSOAP);
//    }
//
//    public void createClient(ClientSOAP clientSOAP) {
//        return userSoapUseCase.addUser(clientSOAP);
//    }
}
