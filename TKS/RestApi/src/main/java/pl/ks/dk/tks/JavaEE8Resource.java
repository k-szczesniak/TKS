package pl.ks.dk.tks;

import pl.ks.dk.tks.security.JWTGeneratorVerifier;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("java")
@Produces(MediaType.APPLICATION_JSON)
public class JavaEE8Resource {
    @GET
    public Response ping() {
        return Response.status(200)
                .type("application/json")
                .entity("Ping")
                .build();
    }
}
