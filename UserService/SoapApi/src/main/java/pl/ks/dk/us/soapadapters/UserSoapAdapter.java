package pl.ks.dk.us.soapadapters;

import pl.ks.dk.us.services.exceptions.ServiceException;
import pl.ks.dk.us.soapmodel.exceptions.UserSoapException;
import pl.ks.dk.us.soapmodel.users.*;
import pl.ks.dk.us.userinterface.UserUseCase;

import javax.inject.Inject;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "UserAPI")
public class UserSoapAdapter {

    @Inject
    private UserUseCase userUseCase;

    public UserSOAP getUser(String uuid) throws UserSoapException{
        try {
            return UserSOAPConverter.convertUserToUserSOAP(userUseCase.getUserByKey(uuid));
        } catch (ServiceException e) {
            throw new UserSoapException(e);
        }
    }

    public int getUserCount() {
        return userUseCase.getUsersCount();
    }

    public List<UserSOAP> getAllUsers() {
        return UserSOAPConverter.convertUserListToUserSOAPList(userUseCase.getAllUsers());
    }

    public void createUser(UserSOAP userSOAP) throws UserSoapException {
        try {
            userUseCase.addUser(UserSOAPConverter.convertUserSOAPToUser(userSOAP));
        } catch (Exception e) {
            throw new UserSoapException(e);
        }
    }
}
