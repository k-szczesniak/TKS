package pl.ks.dk.tks.soapadapters;

import jakarta.inject.Inject;
import pl.ks.dk.tks.services.exceptions.ServiceException;
import pl.ks.dk.tks.soapmodel.exceptions.UserSoapException;
import pl.ks.dk.tks.soapmodel.users.*;
import pl.ks.dk.tks.userinterface.soap.UserSoapUseCase;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "ClientAPI")
public class UserSoapAdapter {

    @Inject
    private UserSoapUseCase userSoapUseCase;

    public UserSOAP getClient(String uuid) throws UserSoapException{
        try {
            return UserSOAPConverter.convertUserToUserSOAP(userSoapUseCase.getUserByKey(uuid));
        } catch (ServiceException e) {
            throw new UserSoapException(e);
        }
    }

    public int getClientCount() {
        return userSoapUseCase.getUsersCount();
    }

    public List<UserSOAP> getAllUsers() {
        return UserSOAPConverter.convertUserListToUserSOAPList(userSoapUseCase.getAllUsers());
    }

    public void createAdmin(AdminSOAP adminSOAP) throws UserSoapException {
        try {
            userSoapUseCase.addUser(UserSOAPConverter.convertUserSOAPToUser(adminSOAP));
        } catch (Exception e) {
            throw new UserSoapException(e);
        }
    }

    public void createSuperUser(SuperUserSOAP superUserSOAP) throws UserSoapException {
        try {
            userSoapUseCase.addUser(UserSOAPConverter.convertUserSOAPToUser(superUserSOAP));
        } catch (Exception e) {
            throw new UserSoapException(e);
        }
    }

    public void createClient(ClientSOAP clientSOAP) throws UserSoapException {
        try {
            userSoapUseCase.addUser(UserSOAPConverter.convertUserSOAPToUser(clientSOAP));
        } catch (Exception e) {
            throw new UserSoapException(e);
        }
    }
}
