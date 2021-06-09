package pl.ks.dk.tks.restadapters;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import pl.ks.dk.tks.converters.EmploymentDTOConverter;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.userinterface.BabysitterUseCase;
import pl.ks.dk.tks.userinterface.EmploymentUseCase;
import pl.ks.dk.tks.userinterface.UserUseCase;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/employment")
public class EmploymentsRestAdapter {

    @Inject
    private BabysitterUseCase babysitterUseCase;

    @Inject
    private UserUseCase userUseCase;

    @Inject
    private EmploymentUseCase employmentUseCase;

    @GET
    @Timed(name = "getAllEmployments",
            tags = {"method=employment"},
            absolute = true,
            description = "Time to get all employments")
    @Counted(name = "getAllEmploymentsInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getAllEmployments(@Context SecurityContext securityContext) {
        return Response.status(200)
                .entity(EmploymentDTOConverter.convertEmploymentListToEmploymentDTOList(
                        employmentUseCase.getActualEmploymentsForClient(
                                userUseCase.getUserByLogin(securityContext.getUserPrincipal().getName()).getUuid())))
                .build();
    }

    @POST
    @Path("{uuid}")
    @Timed(name = "employ",
            tags = {"method=employment"},
            absolute = true,
            description = "Time to employ")
    @Counted(name = "employInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response employ(@Context SecurityContext securityContext, @PathParam("uuid") String uuid) {
        try {
            employmentUseCase.employ((Client) userUseCase.getUserByLogin(securityContext.getUserPrincipal().getName()),
                    babysitterUseCase.getBabysitterByKey(uuid));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(201).build();
    }
}
