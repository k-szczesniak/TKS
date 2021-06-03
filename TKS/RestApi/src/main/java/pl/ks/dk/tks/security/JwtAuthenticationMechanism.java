package pl.ks.dk.tks.security;

import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

@ApplicationScoped
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Inject
    JWTGeneratorVerifier jwtGeneratorVerifier;

    public JwtAuthenticationMechanism() {
    }

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                HttpMessageContext httpMessageContext) {

        if ((httpServletRequest.getRequestURL().toString().endsWith("/auth"))
                || (httpServletRequest.getRequestURL().toString().endsWith("/java"))
                || (httpServletRequest.getRequestURL().toString().endsWith("API"))) {
            return httpMessageContext.doNothing();
        }

        String authHeader = httpServletRequest.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return httpMessageContext.responseUnauthorized();
        }

        String tokenToValidate = authHeader.substring(BEARER.length()).trim();
        if (JWTGeneratorVerifier.validateJWT(tokenToValidate)) {
            try {
                SignedJWT jwtToken = SignedJWT.parse(tokenToValidate);
                String login = jwtToken.getJWTClaimsSet().getSubject();
                String role = jwtToken.getJWTClaimsSet().getStringClaim("role");
                Date expirationTime = (Date) (jwtToken.getJWTClaimsSet().getClaim("exp"));

                if (new Date().after(expirationTime)) {
                    return httpMessageContext.responseUnauthorized();
                }
                return httpMessageContext.notifyContainerAboutLogin(login, new HashSet<>(Collections.singletonList(role)));
            } catch (ParseException e) {
                e.printStackTrace();
                return httpMessageContext.responseUnauthorized();
            }
        }
        return null;
    }

}
