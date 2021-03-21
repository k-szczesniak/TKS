package pl.ks.dk.tks.security;

import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.userinterface.UserUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.HashSet;

//TODO: NA KONIEC: JAK NIE DZIALA TO PEWNIE TUTAJ, 27 i 29 linijka

@ApplicationScoped
public class AuthenticationIdentityStore implements IdentityStore {

    @Inject
    UserUseCase userUseCase;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential =
                    (UsernamePasswordCredential) credential;
            User user = userUseCase
                    .getUserByLoginAndPassword(usernamePasswordCredential.getCaller(),
                            usernamePasswordCredential.getPasswordAsString());
            if (user != null && userUseCase.checkIfUserIsActive(user.getLogin())) {
                return new CredentialValidationResult(user.getLogin(), new HashSet<>(
                        Arrays.asList(user.getRole())));
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }

}
