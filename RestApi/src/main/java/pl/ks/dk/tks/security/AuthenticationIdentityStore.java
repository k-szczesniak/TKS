package pl.ks.dk.tks.security;

import com.mycompany.firstapplication.Users.User;
import com.mycompany.firstapplication.services.UsersService;

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
    UsersService usersService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential =
                    (UsernamePasswordCredential) credential;
            User user = usersService
                    .findByLoginPasswordActive(usernamePasswordCredential.getCaller(),
                            usernamePasswordCredential.getPasswordAsString());
            if (user != null) {
                return new CredentialValidationResult(user.getLogin(), new HashSet<>(
                        Arrays.asList(user.getRole())));
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }

}
