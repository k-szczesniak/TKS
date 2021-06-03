package pl.ks.dk.tks.restadapters;

import org.json.JSONObject;
import pl.ks.dk.tks.converters.UserDTOConverter;
import pl.ks.dk.tks.dtomodel.interfaces.EntityToSignDTO;
import pl.ks.dk.tks.dtomodel.users.AdminDTO;
import pl.ks.dk.tks.dtomodel.users.ClientDTO;
import pl.ks.dk.tks.dtomodel.users.SuperUserDTO;
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
import java.util.Set;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UsersRestAdapter {

    @Inject
    private UserUseCase userUseCase;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @GET
    @Path("_self")
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
    public Response getClient(@PathParam("uuid") String uuid) {
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

    public <T> void validation(T user) {
        Set<ConstraintViolation<T>> errors = validator.validate(user);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Bean validation error");
        }
    }


}
