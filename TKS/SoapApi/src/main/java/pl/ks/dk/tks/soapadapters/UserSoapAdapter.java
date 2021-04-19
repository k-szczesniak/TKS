package pl.ks.dk.tks.soapadapters;

import pl.ks.dk.tks.services.exceptions.ServiceException;
import pl.ks.dk.tks.soapmodel.exceptions.UserSoapException;
import pl.ks.dk.tks.soapmodel.users.*;
import pl.ks.dk.tks.userinterface.UserUseCase;

import javax.inject.Inject;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "ClientAPI")
public class UserSoapAdapter {

    @Inject
    private UserUseCase userUseCase;

    public UserSOAP getClient(String uuid) throws UserSoapException{
        try {
            return UserSOAPConverter.convertUserToUserSOAP(userUseCase.getUserByKey(uuid));
        } catch (ServiceException e) {
            throw new UserSoapException(e);
        }
    }

    public int getClientCount() {
        return userUseCase.getUsersCount();
    }

    public List<UserSOAP> getAllUsers() {
        return UserSOAPConverter.convertUserListToUserSOAPList(userUseCase.getAllUsers());
    }

    public void createAdmin(AdminSOAP adminSOAP) throws UserSoapException {
        try {
            userUseCase.addUser(UserSOAPConverter.convertUserSOAPToUser(adminSOAP));
        } catch (Exception e) {
            throw new UserSoapException(e);
        }
    }

    public void createSuperUser(SuperUserSOAP superUserSOAP) throws UserSoapException {
        try {
            userUseCase.addUser(UserSOAPConverter.convertUserSOAPToUser(superUserSOAP));
        } catch (Exception e) {
            throw new UserSoapException(e);
        }
    }

    public void createClient(ClientSOAP clientSOAP) throws UserSoapException {
        try {
            userUseCase.addUser(UserSOAPConverter.convertUserSOAPToUser(clientSOAP));
        } catch (Exception e) {
            throw new UserSoapException(e);
        }
    }
}
