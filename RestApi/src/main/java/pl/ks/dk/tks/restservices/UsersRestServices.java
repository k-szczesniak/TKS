package pl.ks.dk.tks.restservices;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.exceptions.UserException;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.dto.UserDTOWrapper;
import pl.ks.dk.tks.filters.EntitySignatureValidatorFilterBinding;
import pl.ks.dk.tks.userinterface.UserUseCase;
import pl.ks.dk.tks.utils.EntityIdentitySignerVerifier;

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
public class UsersRestServices {

    //TODO: W DOMAIN MODEL NIE POWINNO BYC PAYLOAD

    @Inject
    private UserUseCase userUseCase;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @GET
    @Path("_self")
    public Response findSelf(@Context SecurityContext securityContext) {
        return Response.status(200)
                .entity(UserDTOWrapper.wrap(userUseCase.getUserByLogin(securityContext.getUserPrincipal().getName())))
                .build();
    }

    @GET
    @Path("{uuid}")
    public Response getClient(@PathParam("uuid") String uuid) {
        try {
            return Response.status(200)
                    .header("ETag", EntityIdentitySignerVerifier.calculateETag((userUseCase.getUserByKey(uuid))))
                    .entity(UserDTOWrapper.wrap(userUseCase.getUserByKey(uuid)))
                    .build();
        } catch (UserException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    public Response getAllUsers() {
        return Response.status(200).entity(UserDTOWrapper.listWrapper(userUseCase.getAllUsers()))
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
            BeanUtils.copyProperties(userUseCase.getUserByKey(uuid), admin);
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
            BeanUtils.copyProperties(userUseCase.getUserByKey(uuid), superUser);
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
            BeanUtils.copyProperties(userUseCase.getUserByKey(uuid), client);
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
        userUseCase.addUser(admin);
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
        userUseCase.addUser(superUser);
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
        userUseCase.addUser(client);
        return Response.status(201).build();
    }

    public <T> void validation(T user) {
        Set<ConstraintViolation<T>> errors = validator.validate(user);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Bean validation error");
        }
    }


}
