package pl.ks.dk.tks.restadapters;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import pl.ks.dk.tks.converters.BabysitterDTOConverter;
import pl.ks.dk.tks.dtomodel.babysitters.BabysitterDTO;
import pl.ks.dk.tks.dtomodel.babysitters.TeachingSitterDTO;
import pl.ks.dk.tks.dtomodel.babysitters.TidingSitterDTO;
import pl.ks.dk.tks.dtomodel.interfaces.EntityToSignDTO;
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
import java.util.Set;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/resources")
public class ResourcesRestAdapter {

    @Inject
    private BabysitterUseCase babysitterUseCase;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @GET
    @Path("{uuid}")
    @Timed(name = "getBabysitter",
            tags = {"method=babysitter"},
            absolute = true,
            description = "Time to get babysitter")
    @Counted(name = "getBabysitterInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getBabysitter(@PathParam("uuid") String uuid) {
        try {
            EntityToSignDTO entityToSign =
                    BabysitterDTOConverter.convertBabysitterToBabysitterDTO(babysitterUseCase.getBabysitterByKey(uuid));
            return Response.status(200)
                    .header("ETag", EntityIdentitySignerVerifier.calculateETag(entityToSign))
                    .entity(entityToSign)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    @Timed(name = "getAllBabysitters",
            tags = {"method=babysitter"},
            absolute = true,
            description = "Time to get all babysitters")
    @Counted(name = "getAllBabysittersInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response getAllBabysitters() {
        try {
            return Response.status(200)
                    .entity(BabysitterDTOConverter
                            .convertBabysitterListToBabysitterDTOList(babysitterUseCase.getAllBabysitters()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @PUT
    @Path("/standard/{uuid}")
    @Timed(name = "updateBabysitter",
            tags = {"method=babysitter"},
            absolute = true,
            description = "Time to update babysitter")
    @Counted(name = "updateBabysitterInvocations",
            absolute = true,
            description = "Number of invocations")
    @EntitySignatureValidatorFilterBinding
    public Response updateBabysitter(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header,
                                     BabysitterDTO babysitterDTO) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, babysitterDTO)) {
            return Response.status(406).build();
        }
        try {
            validation(babysitterDTO);
            babysitterUseCase.updateBabysitter(BabysitterDTOConverter.convertBabysitterDTOToBabysitter(babysitterDTO), uuid);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }

    @PUT
    @Path("/teaching/{uuid}")
    @Timed(name = "updateTeachingSitter",
            tags = {"method=teachingsitter"},
            absolute = true,
            description = "Time to update teachingsitter")
    @Counted(name = "updateTeachingSitterInvocations",
            absolute = true,
            description = "Number of invocations")
    @EntitySignatureValidatorFilterBinding
    public Response updateTeachingSitter(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header,
                                         TeachingSitterDTO teachingSitterDTO) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, teachingSitterDTO)) {
            return Response.status(406).build();
        }
        try {
            validation(teachingSitterDTO);
            babysitterUseCase.updateBabysitter(BabysitterDTOConverter.convertBabysitterDTOToBabysitter(teachingSitterDTO), uuid);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }

    @PUT
    @Path("/tiding/{uuid}")
    @Timed(name = "updateTidingSitter",
            tags = {"method=tidingsitter"},
            absolute = true,
            description = "Time to update tidingsitter")
    @Counted(name = "updateTidingSitterInvocations",
            absolute = true,
            description = "Number of invocations")
    @EntitySignatureValidatorFilterBinding
    public Response updateTidingSitter(@PathParam("uuid") String uuid, @HeaderParam("If-Match") String header,
                                       TidingSitterDTO tidingSitterDTO) {
        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(header, tidingSitterDTO)) {
            return Response.status(406).build();
        }
        try {
            validation(tidingSitterDTO);
            babysitterUseCase.updateBabysitter(BabysitterDTOConverter.convertBabysitterDTOToBabysitter(tidingSitterDTO), uuid);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(200).build();
    }

    @POST
    @Path("/standard")
    @Timed(name = "createBabysitter",
            tags = {"method=babysitter"},
            absolute = true,
            description = "Time to create babysitter")
    @Counted(name = "createBabysitterInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response createBabysitter(BabysitterDTO babysitterDTO) {
        try {
            validation(babysitterDTO);
            babysitterUseCase.addBabysitter(BabysitterDTOConverter.convertBabysitterDTOToBabysitter(babysitterDTO));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(201).build();
    }

    @POST
    @Path("/teaching")
    @Timed(name = "createTeachingSitter",
            tags = {"method=teachingsitter"},
            absolute = true,
            description = "Time to create teachingsitter")
    @Counted(name = "createTeachingSitterInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response createTeachingSitter(TeachingSitterDTO teachingSitterDTO) {
        try {
            validation(teachingSitterDTO);
            babysitterUseCase.addBabysitter(BabysitterDTOConverter.convertBabysitterDTOToBabysitter(teachingSitterDTO));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(201).build();
    }

    @POST
    @Path("/tiding")
    @Timed(name = "createTidingSitter",
            tags = {"method=tidingsitter"},
            absolute = true,
            description = "Time to create tidingsitter")
    @Counted(name = "createTidingSitterInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response createTidingSitter(TidingSitterDTO tidingSitterDTO) {
        try {
            validation(tidingSitterDTO);
            babysitterUseCase.addBabysitter(BabysitterDTOConverter.convertBabysitterDTOToBabysitter(tidingSitterDTO));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(422).build();
        }
        return Response.status(201).build();
    }

    @DELETE
    @Path("{uuid}")
    @Timed(name = "deleteBabysitter",
            tags = {"method=babysitter"},
            absolute = true,
            description = "Time to delete babysitter")
    @Counted(name = "deleteBabysitterInvocations",
            absolute = true,
            description = "Number of invocations")
    public Response deleteBabysitter(@PathParam("uuid") String uuid) {
        try {
            babysitterUseCase.deleteBabysitter(uuid);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        return Response.status(204).build();
    }

    private <T> void validation(T babysitter) {
        Set<ConstraintViolation<T>> errors = validator.validate(babysitter);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Bean validation error");
        }
    }
}
