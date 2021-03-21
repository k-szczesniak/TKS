package pl.ks.dk.tks.restservices;

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
public class EmploymentsRestServices {

    @Inject
    private BabysitterUseCase babysitterUseCase;

    @Inject
    private UserUseCase userUseCase;

    @Inject
    private EmploymentUseCase employmentUseCase;

    @GET
    public Response getAllEmployments(@Context SecurityContext securityContext) {
        return Response.status(200)
                .entity(EmploymentDTOConverter.convertEmploymentListToEmploymentDTOList(
                        employmentUseCase.getActualEmploymentsForClient((Client)
                                userUseCase.getUserByLogin(securityContext.getUserPrincipal().getName()))))
                .build();
    }

    @POST
    @Path("{uuid}")
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
