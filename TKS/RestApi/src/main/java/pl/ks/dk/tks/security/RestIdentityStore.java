package pl.ks.dk.tks.security;

import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.services.exceptions.ServiceException;
import pl.ks.dk.tks.userinterface.UserUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;


@ApplicationScoped
public class RestIdentityStore implements IdentityStore {

    @Inject
    UserUseCase userUseCase;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential =
                    (UsernamePasswordCredential) credential;
            User user = null;
            try {
                user = userUseCase
                        .getUserByLoginAndPassword(usernamePasswordCredential.getCaller(),
                                usernamePasswordCredential.getPasswordAsString());
            } catch (ServiceException e) {
                e.printStackTrace();
                return CredentialValidationResult.INVALID_RESULT;
            }
            if (user != null && user.isActive()) {
                return new CredentialValidationResult(user.getLogin(), new HashSet<>(
                        Collections.singletonList(user.getRole())));
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }

}
