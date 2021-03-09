package com.mycompany.firstapplication.RestServices;

import com.mycompany.firstapplication.Exceptions.UserException;
import com.mycompany.firstapplication.Filters.EntitySignatureValidatorFilterBinding;
import com.mycompany.firstapplication.Users.*;
import com.mycompany.firstapplication.utils.EntityIdentitySignerVerifier;
import org.apache.commons.beanutils.BeanUtils;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UsersService {

    @Inject
    private UsersManager usersManager;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @GET
    @Path("_self")
    public Response findSelf(@Context SecurityContext securityContext) {
        return Response.status(200)
                .entity(UserDTOWrapper.wrap(usersManager.findByLogin(securityContext.getUserPrincipal().getName())))
                .build();
    }

    @GET
    @Path("{uuid}")
    public Response getClient(@PathParam("uuid") String uuid) {
        try {
            return Response.status(200)
                    .header("ETag", EntityIdentitySignerVerifier.calculateETag((usersManager.findByKey(uuid))))
                    .entity(UserDTOWrapper.wrap(usersManager.findByKey(uuid)))
                    .build();
        } catch (UserException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    public Response getAllUsers() {
        return Response.status(200).entity(UserDTOWrapper.listWrapper(usersManager.getUsersList()))
                .build();
    }

    @PUT
    @Path("/admin/{uuid}")
    @EntitySignatureValidatorFilterBinding
    public Response updateAdmin(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header, Admin admin) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, admin)) {
            return Response.status(406).build();
        }
        try {
            validation(admin);
            BeanUtils.copyProperties(usersManager.findByKey(uuid), admin);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }

    @PUT
    @Path("/superUser/{uuid}")
    @EntitySignatureValidatorFilterBinding
    public Response updateSuperUser(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header,
                                    SuperUser superUser) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, superUser)) {
            return Response.status(406).build();
        }
        try {
            validation(superUser);
            BeanUtils.copyProperties(usersManager.findByKey(uuid), superUser);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }

    @PUT
    @Path("/client/{uuid}")
    @EntitySignatureValidatorFilterBinding
    public Response updateClient(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header,
                                 Client client) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, client)) {
            return Response.status(406).build();
        }
        try {
            validation(client);
            BeanUtils.copyProperties(usersManager.findByKey(uuid), client);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }


    @POST
    @Path("/admin")
    public Response createAdmin(Admin admin) {
        try {
            validation(admin);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        usersManager.addUser(admin);
        return Response.status(201).build();
    }

    @POST
    @Path("/superUser")
    public Response createSuperUser(SuperUser superUser) {
        try {
            validation(superUser);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        usersManager.addUser(superUser);
        return Response.status(201).build();
    }

    @POST
    @Path("/client")
    public Response createClient(Client client) {
        try {
            validation(client);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        usersManager.addUser(client);
        return Response.status(201).build();
    }

    public <T> void validation(T user) {
        Set<ConstraintViolation<T>> errors = validator.validate(user);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Bean validation error");
        }
    }


}
