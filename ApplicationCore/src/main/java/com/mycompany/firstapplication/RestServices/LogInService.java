package com.mycompany.firstapplication.RestServices;

import com.mycompany.firstapplication.Security.JWTAuthenticationMechanism;
import com.mycompany.firstapplication.Security.JWTGeneratorVerifier;
import com.mycompany.firstapplication.Security.LoginData;
import com.mycompany.firstapplication.services.UsersService;
import com.nimbusds.jwt.SignedJWT;

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
import java.text.ParseException;

@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)

@Path("/auth")
public class LogInService {

    @Inject
    IdentityStoreHandler identityStoreHandler;

    @Inject
    private UsersService usersService;

    @POST
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
    public Response updateToken(@Context HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader(JWTAuthenticationMechanism.AUTHORIZATION);
        String tokenToUpdate = authHeader.substring(JWTAuthenticationMechanism.BEARER.length());
        try {
            String login = SignedJWT.parse(tokenToUpdate).getJWTClaimsSet().getSubject();
            if (usersService.checkIfActive(login)) {
                return Response.status(202)
                        .type("application/jwt")
                        .entity(JWTGeneratorVerifier.updateJWTString(tokenToUpdate))
                        .build();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
        return Response.status(401).build();
    }
}
