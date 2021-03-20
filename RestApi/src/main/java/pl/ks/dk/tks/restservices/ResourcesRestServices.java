package pl.ks.dk.tks.restservices;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.babysitters.TeachingSitter;
import pl.ks.dk.tks.domainmodel.babysitters.TidingSitter;
import pl.ks.dk.tks.domainmodel.interfaces.EntityToSign;
import pl.ks.dk.tks.filters.EntitySignatureValidatorFilterBinding;
import pl.ks.dk.tks.userinterface.BabysitterUseCase;
import pl.ks.dk.tks.utils.EntityIdentitySignerVerifier;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/resources")
public class ResourcesRestServices {

    //TODO: PRZEROBIC PRZYJMOWANE NA DTO
    //TODO: WYJATKI OGARNAC
    //TODO: PRZENIESC POWTARZAJACY SIE KOD

    @Inject
    private BabysitterUseCase babysitterUseCase;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @GET
    @Path("{uuid}")
    public Response getBabysitter(@PathParam("uuid") String uuid) {
        try {
            EntityToSign entityToSign = babysitterUseCase.getBabysitterByKey(uuid);
            return Response.status(200)
                    .header("ETag", EntityIdentitySignerVerifier.calculateETag(entityToSign))
                    .entity(entityToSign).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    public Response getAllBabysitters() {
        return Response.status(200).entity(babysitterUseCase.getAllBabysitters()).build();
    }

    @PUT
    @Path("/standard/{uuid}")
    @EntitySignatureValidatorFilterBinding
    public Response updateBabysitter(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header,
                                     Babysitter babysitter) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, babysitter)) {
            return Response.status(406).build();
        }
        try {
            validation(babysitter);
            BeanUtils.copyProperties(babysitterUseCase.getBabysitterByKey(uuid), babysitter);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }

    @PUT
    @Path("/teaching/{uuid}")
    @EntitySignatureValidatorFilterBinding
    public Response updateTeachingSitter(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header,
                                         TeachingSitter teachingSitter) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, teachingSitter)) {
            return Response.status(406).build();
        }
        try {
            validation(teachingSitter);
            BeanUtils.copyProperties(babysitterUseCase.getBabysitterByKey(uuid), teachingSitter);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }

    @PUT
    @Path("/tiding/{uuid}")
    @EntitySignatureValidatorFilterBinding
    public Response updateTidingSitter(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header,
                                       TidingSitter tidingSitter) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, tidingSitter)) {
            return Response.status(406).build();
        }
        try {
            validation(tidingSitter);
            BeanUtils.copyProperties(babysitterUseCase.getBabysitterByKey(uuid), tidingSitter);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }

    @POST
    @Path("/standard")
    public Response createBabysitter(Babysitter babysitter) {
        try {
            validation(babysitter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        babysitterUseCase.addBabysitter(babysitter);
        return Response.status(201).build();
    }

    @POST
    @Path("/teaching")
    public Response createTeachingSitter(TeachingSitter teachingSitter) {
        try {
            validation(teachingSitter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        babysitterUseCase.addBabysitter(teachingSitter);
        return Response.status(201).build();
    }

    @POST
    @Path("/tiding")
    public Response createTidingSitter(TidingSitter tidingSitter) {
        try {
            validation(tidingSitter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        babysitterUseCase.addBabysitter(tidingSitter);
        return Response.status(201).build();
    }

    @DELETE
    @Path("{uuid}")
    public Response deleteBabysitter(@PathParam("uuid") String uuid) {
        babysitterUseCase.deleteBabysitter(babysitterUseCase.getBabysitterByKey(uuid));
        return Response.status(204).build();
    }

    private <T> void validation(T babysitter) {
        Set<ConstraintViolation<T>> errors = validator.validate(babysitter);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Bean validation error");
        }
    }
}
