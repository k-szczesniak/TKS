package pl.ks.dk.tks.restadapters;

import pl.ks.dk.tks.converters.EmploymentDTOConverter;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.userinterface.rest.BabysitterRestUseCase;
import pl.ks.dk.tks.userinterface.rest.EmploymentRestUseCase;
import pl.ks.dk.tks.userinterface.rest.UserRestUseCase;

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
    private BabysitterRestUseCase babysitterRestUseCase;

    @Inject
    private UserRestUseCase userRestUseCase;

    @Inject
    private EmploymentRestUseCase employmentRestUseCase;

    @GET
    public Response getAllEmployments(@Context SecurityContext securityContext) {
        return Response.status(200)
                .entity(EmploymentDTOConverter.convertEmploymentListToEmploymentDTOList(
                        employmentRestUseCase.getActualEmploymentsForClient(
                                userRestUseCase.getUserByLogin(securityContext.getUserPrincipal().getName()).getUuid())))
                .build();
    }

    @POST
    @Path("{uuid}")
    public Response employ(@Context SecurityContext securityContext, @PathParam("uuid") String uuid) {
        try {
            employmentRestUseCase.employ((Client) userRestUseCase.getUserByLogin(securityContext.getUserPrincipal().getName()),
                    babysitterRestUseCase.getBabysitterByKey(uuid));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(201).build();
    }
}
