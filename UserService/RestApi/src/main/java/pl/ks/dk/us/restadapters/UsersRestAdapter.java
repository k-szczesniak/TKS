package pl.ks.dk.us.restadapters;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.json.JSONObject;
import pl.ks.dk.us.Publisher;
import pl.ks.dk.us.converters.UserDTOConverter;
import pl.ks.dk.us.dtomodel.interfaces.EntityToSignDTO;
import pl.ks.dk.us.dtomodel.users.UserDTO;
import pl.ks.dk.us.filters.EntitySignatureValidatorFilterBinding;
import pl.ks.dk.us.userinterface.UserUseCase;
import pl.ks.dk.us.utils.EntityIdentitySignerVerifier;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.Set;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UsersRestAdapter {

    @Inject
    private Publisher publisher;

    @Inject
    private UserUseCase userUseCase;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @GET
    @Path("_self")
    @Timed(name = "findSelf",
            tags = {"method=users"},
            absolute = true,
            description = "Time to get personal information")
    @Counted(name = "findSelfInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response findSelf(@Context SecurityContext securityContext) {
        try {
            return Response.status(200)
                    .entity(JSONObject.wrap(UserDTOConverter.convertUserToUserDTO(
                            userUseCase.getUserByLogin(securityContext.getUserPrincipal().getName()))).toString())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    @Path("{uuid}")
    @Timed(name = "getClient",
            tags = {"method=users"},
            absolute = true,
            description = "Time to get client")
    @Counted(name = "getClientInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getUser(@PathParam("uuid") String uuid) {
        try {
            EntityToSignDTO entityToSign = UserDTOConverter.convertUserToUserDTO(userUseCase.getUserByKey(uuid));
            return Response.status(200)
                    .header("ETag", EntityIdentitySignerVerifier.calculateETag(entityToSign))
                    .entity(JSONObject.wrap(entityToSign).toString())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    @Timed(name = "getAllUsers",
            tags = {"method=users"},
            absolute = true,
            description = "Time to get all users")
    @Counted(name = "getAllUsersInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getAllUsers() {
        try {
            return Response.status(200)
                    .entity(JSONObject.wrap(UserDTOConverter.convertUserListToUserDTOList(userUseCase.getAllUsers()))
                            .toString())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @PUT
    @Path("/user/{uuid}")
    @Timed(name = "updateUser",
            tags = {"method=users"},
            absolute = true,
            description = "Time to update user")
    @Counted(name = "updateUserInvocations",
            absolute = true,
            description = "Number of invocations")
    @EntitySignatureValidatorFilterBinding
    public Response updateUser(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header,
                               UserDTO userDTO) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, userDTO)) {
            return Response.status(406).build();
        }
        try {
            validation(userDTO);
            if (userDTO.getRole().equals("Client")) {
                publisher.updateUser(Publisher.Serialization
                        .clientToJsonString(UserDTOConverter.convertUserDTOToUser(userDTO), userDTO.getNumberOfChildren(),
                                userDTO.getAgeOfTheYoungestChild()));
            } else if (userDTO.getRole().equals("Admin") || userDTO.getRole().equals("SuperUser")) {
                publisher.updateUser(Publisher.Serialization.userToJsonString(UserDTOConverter.convertUserDTOToUser(userDTO)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }

    @POST
    @Path("/user")
    @Timed(name = "createUser",
            tags = {"method=users"},
            absolute = true,
            description = "Time to create user")
    @Counted(name = "createUserInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response createUser(UserDTO userDTO) {
        try {
            validation(userDTO);
            if (userDTO.getRole().equals("Client")) {
                publisher.createUser(Publisher.Serialization
                        .clientToJsonString(UserDTOConverter.convertUserDTOToUser(userDTO), userDTO.getNumberOfChildren(),
                                userDTO.getAgeOfTheYoungestChild()));
            } else if (userDTO.getRole().equals("Admin") || userDTO.getRole().equals("SuperUser")) {
                publisher.createUser(Publisher.Serialization.userToJsonString(UserDTOConverter.convertUserDTOToUser(userDTO)));
            }
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(201).build();
    }

    public <T> void validation(T user) {
        Set<ConstraintViolation<T>> errors = validator.validate(user);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Bean validation error");
        }
    }


}
