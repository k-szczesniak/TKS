package com.mycompany.firstapplication.RestServices;

import com.mycompany.firstapplication.Babysitters.Babysitter;
import com.mycompany.firstapplication.Babysitters.BabysittersManager;
import com.mycompany.firstapplication.Babysitters.TeachingSitter;
import com.mycompany.firstapplication.Babysitters.TidingSitter;
import com.mycompany.firstapplication.Exceptions.RepositoryException;
import com.mycompany.firstapplication.Filters.EntitySignatureValidatorFilterBinding;
import com.mycompany.firstapplication.Interfaces.EntityToSign;
import com.mycompany.firstapplication.utils.EntityIdentitySignerVerifier;
import org.apache.commons.beanutils.BeanUtils;

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
public class ResourcesService {

    @Inject
    private BabysittersManager babysittersManager;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @GET
    @Path("{uuid}")
    public Response getBabysitter(@PathParam("uuid") String uuid) {
        try {
            return Response.status(200)
                    .header("ETag", EntityIdentitySignerVerifier.calculateETag((babysittersManager.findByKey(uuid))))
                    .entity(babysittersManager.findByKey(uuid)).build();
        } catch (RepositoryException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    public Response getAllBabysitters() {
        return Response.status(200).entity(babysittersManager.getBabysittersList()).build();
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
            BeanUtils.copyProperties(babysittersManager.findByKey(uuid), babysitter);
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
            BeanUtils.copyProperties(babysittersManager.findByKey(uuid), teachingSitter);
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
            BeanUtils.copyProperties(babysittersManager.findByKey(uuid), tidingSitter);
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
        babysittersManager.addBabysitter(babysitter);
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
        babysittersManager.addBabysitter(teachingSitter);
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
        babysittersManager.addBabysitter(tidingSitter);
        return Response.status(201).build();
    }

    @DELETE
    @Path("{uuid}")
    public Response deleteBabysitter(@PathParam("uuid") String uuid) {
        babysittersManager.deleteBabysitter(babysittersManager.findByKey(uuid));
        return Response.status(204).build();
    }

    private <T> void validation(T babysitter) {
        Set<ConstraintViolation<T>> errors = validator.validate(babysitter);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Bean validation error");
        }
    }
}
