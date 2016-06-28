package org.eurekaclinical.useragreementservice.resource;

/*-
 * #%L
 * Eureka! Clinical User Agreement Service
 * %%
 * Copyright (C) 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.inject.Inject;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eurekaclinical.standardapis.exception.HttpStatusException;
import org.eurekaclinical.useragreementservice.comm.UserAgreement;
import org.eurekaclinical.useragreementservice.dao.UserAgreementDao;
import org.eurekaclinical.useragreementservice.entity.UserAgreementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RESTful end-point for {@link UserAgreementEntity} related methods.
 *
 * @author Andrew Post
 */
@Path("/protected/useragreements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAgreementResource {

    /**
     * The class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAgreementResource.class);
    /**
     * Data access object to work with user agreement objects.
     */
    private final UserAgreementDao userAgreementDao;

    /**
     * Creates the user agreement REST endpoint.
     *
     * @param inUserAgreementDao DAO used to access 
     * {@link UserAgreementEntity} related functionality.
     */
    @Inject
    public UserAgreementResource(UserAgreementDao inUserAgreementDao) {
        this.userAgreementDao = inUserAgreementDao;
    }

    /**
     * Get a list of all users in the system.
     *
     * @return a list of {@link UserAgreement} objects.
     */
    @GET
    @RolesAllowed({"admin"})
    public List<UserAgreement> getUserAgreements() {
        List<UserAgreementEntity> uaes = this.userAgreementDao.getAll();
        List<UserAgreement> result = new ArrayList<>(uaes.size());
        for (UserAgreementEntity uae : uaes) {
            UserAgreement userAgreement = new UserAgreement();
            userAgreement.setId(uae.getId());
            userAgreement.setUsername(uae.getUsername());
            userAgreement.setExpiry(uae.getExpiry());
            result.add(userAgreement);
        }
        return result;
    }

    /**
     * Get a user by the user's identification number.
     *
     * @param inId the identification number for the user agreement record to 
     * fetch. Cannot be <code>null</code>.
     * @param req the request object. Cannot be <code>null</code>.
     * @return The user agreement record referenced by the identification 
     * number.
     * @throws HttpStatusException with a 404 status code if there is no user
     * agreement record with that identification number.
     */
    @Path("/{id}")
    @GET
    public UserAgreement getUserAgreementById(@PathParam("id") Long inId, @Context HttpServletRequest req) {
        UserAgreementEntity userAgreementEntity = this.userAgreementDao.retrieve(inId);
        if (userAgreementEntity == null) {
            throw new HttpStatusException(Response.Status.NOT_FOUND);
        }
        String username = userAgreementEntity.getUsername();
        checkUsername(username, req, Response.Status.NOT_FOUND);
        UserAgreement userAgreement = new UserAgreement();
        userAgreement.setId(userAgreementEntity.getId());
        userAgreement.setUsername(userAgreementEntity.getUsername());
        userAgreement.setExpiry(userAgreementEntity.getExpiry());
        return userAgreement;
    }

    /**
     * Get the current user's identification number.
     *
     * @param req the request object. Cannot be <code>null</code>.
     * @return The current user's user agreement record. Cannot be 
     * <code>null</code>.
     * @throws HttpStatusException with a 404 status code if the current user
     * has no user agreement record.
     */
    @Path("/me")
    @GET
    public UserAgreement getMyUserAgreement(@Context HttpServletRequest req) {
        Principal principal = req.getUserPrincipal();
        String username = principal.getName();
        UserAgreementEntity userAgreementEntity = this.userAgreementDao.getByUsername(username);
        if (userAgreementEntity == null) {
            throw new HttpStatusException(Response.Status.NOT_FOUND);
        }
        UserAgreement userAgreement = new UserAgreement();
        userAgreement.setId(userAgreementEntity.getId());
        userAgreement.setUsername(userAgreementEntity.getUsername());
        userAgreement.setExpiry(userAgreementEntity.getExpiry());
        return userAgreement;
    }

    /**
     * Creates a user agreement record for the current user.
     *
     * @param inUserAgreement the new user agreement record, must have the
     * username of the current user. Cannot be <code>null</code>.
     * @param req the request object. Cannot be <code>null</code>.
     * @return a response object with a 201 status code (created) and the URI 
     * for the created record.
     * @throws HttpStatusException if an error occurred. If the fields of the
     * user agreement record are invalid, the exception will have a 400 status
     * code (bad request). If the current user already has a user agreement 
     * record, the exception will have a 409 status code (conflict).
     */
    @POST
    public Response create(UserAgreement inUserAgreement, @Context HttpServletRequest req) {
        String username = inUserAgreement.getUsername();
        checkUsername(username, req, Response.Status.BAD_REQUEST);
        if (this.userAgreementDao.getByUsername(username) != null) {
            throw new HttpStatusException(Response.Status.CONFLICT);
        }
        UserAgreementEntity userAgreementEntity = new UserAgreementEntity();
        userAgreementEntity.setUsername(inUserAgreement.getUsername());
        userAgreementEntity.setExpiry(inUserAgreement.getExpiry());
        this.userAgreementDao.create(userAgreementEntity);
        return Response.created(URI.create("/" + userAgreementEntity.getId())).build();
    }

    /**
     * Updates the current user's user agreement record.
     *
     * @param inUserAgreement the user agreement record to update. It must have 
     * the username of the current user. Cannot be <code>null</code>.
     * @param req the request object. Cannot be <code>null</code>.
     */
    @PUT
    public void update(UserAgreement inUserAgreement, @Context HttpServletRequest req) {
        UserAgreementEntity userAgreementEntity = this.userAgreementDao.retrieve(inUserAgreement.getId());
        String username = userAgreementEntity.getUsername();
        checkUsername(username, req, Response.Status.NOT_FOUND);
        if (!username.equals(inUserAgreement.getUsername())) {
            throw new HttpStatusException(Response.Status.BAD_REQUEST);
        }
        userAgreementEntity.setExpiry(inUserAgreement.getExpiry());
        this.userAgreementDao.update(userAgreementEntity);
    }

    private void checkUsername(String username, HttpServletRequest req, Response.Status status) {
        Principal principal = req.getUserPrincipal();
        String principalUsername = principal.getName();
        if (!principalUsername.equals(username)) {
            throw new HttpStatusException(status);
        }
    }
}
