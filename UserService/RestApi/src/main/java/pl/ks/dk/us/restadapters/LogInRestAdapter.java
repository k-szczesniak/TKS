package pl.ks.dk.us.restadapters;

import com.nimbusds.jwt.SignedJWT;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import pl.ks.dk.us.security.JWTAuthenticationMechanism;
import pl.ks.dk.us.security.JWTGeneratorVerifier;
import pl.ks.dk.us.security.LoginData;
import pl.ks.dk.us.userinterface.UserUseCase;

import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)

@Path("/auth")
public class LogInRestAdapter {

    @Inject
    IdentityStoreHandler identityStoreHandler;

    @Inject
    private UserUseCase userUseCase;

    @POST
    @Timed(name = "logIn",
            tags = {"method=login"},
            absolute = true,
            description = "Time to login")
    @Counted(name = "logInInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response logIn(LoginData loginData) {
        Credential credential = new UsernamePasswordCredential(loginData.getLogin(),
                new Password(loginData.getPassword()));
        CredentialValidationResult result = identityStoreHandler.validate(credential);
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return Response.status(202)
                    .type("application/jwt")
                    .entity(JWTGeneratorVerifier.generateJWTString(result))
                    .build();
        }
        return Response.status(401).build();
    }

    @GET
    @Path("/update")
    @Timed(name = "updateToken",
            tags = {"method=login"},
            absolute = true,
            description = "Time to update token")
    @Counted(name = "updateTokenInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response updateToken(@Context HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader(JWTAuthenticationMechanism.AUTHORIZATION);
        String tokenToUpdate = authHeader.substring(JWTAuthenticationMechanism.BEARER.length());
        try {
            String login = SignedJWT.parse(tokenToUpdate).getJWTClaimsSet().getSubject();
            if (userUseCase.checkIfUserIsActive(login)) {
                return Response.status(202)
                        .type("application/jwt")
                        .entity(JWTGeneratorVerifier.updateJWTString(tokenToUpdate))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
        return Response.status(401).build();
    }
}
