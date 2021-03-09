package com.mycompany.firstapplication.RestServices;

import com.mycompany.firstapplication.Babysitters.Babysitter;
import com.mycompany.firstapplication.Babysitters.BabysittersManager;
import com.mycompany.firstapplication.Employment.EmploymentsManager;
import com.mycompany.firstapplication.Exceptions.EmploymentException;
import com.mycompany.firstapplication.Exceptions.RepositoryException;
import com.mycompany.firstapplication.Exceptions.UserException;
import com.mycompany.firstapplication.Users.Client;
import com.mycompany.firstapplication.Users.UsersManager;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/employment")
public class EmploymentServices {

    @Inject
    private UsersManager usersManager;

    @Inject
    private BabysittersManager babysitterManager;

    @Inject
    private EmploymentsManager employmentsManager;

    @GET
    public Response getAllEmployments(@Context SecurityContext securityContext) {
        return Response.status(200)
                .entity(employmentsManager.getActualEmploymentsForClient((Client)
                        usersManager.findByLogin(securityContext.getUserPrincipal().getName())))
                .build();
    }

    @POST
    @Path("{uuid}")
    public Response employ(@Context SecurityContext securityContext,
                           @PathParam("uuid") String uuid) {
        try {
            Client client =
                    (Client) usersManager.findByLogin(securityContext.getUserPrincipal().getName());
            Babysitter babysitter = babysitterManager.findByKey(uuid);
            employmentsManager.employBabysitter(client, babysitter);
        } catch (UserException | RepositoryException | EmploymentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(201).build();
    }
}

