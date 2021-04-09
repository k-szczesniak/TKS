package pl.ks.dk.tks.security;

import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.userinterface.rest.UserRestUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.HashSet;


@ApplicationScoped
public class AuthenticationIdentityStore implements IdentityStore {

    @Inject
    UserRestUseCase userRestUseCase;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential =
                    (UsernamePasswordCredential) credential;
            User user = userRestUseCase
                    .getUserByLoginAndPassword(usernamePasswordCredential.getCaller(),
                            usernamePasswordCredential.getPasswordAsString());
            if (user != null && userRestUseCase.checkIfUserIsActive(user.getLogin())) {
                return new CredentialValidationResult(user.getLogin(), new HashSet<>(
                        Arrays.asList(user.getRole())));
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }

}
