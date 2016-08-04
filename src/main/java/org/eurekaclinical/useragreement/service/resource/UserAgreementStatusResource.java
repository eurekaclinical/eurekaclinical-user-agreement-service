package org.eurekaclinical.useragreement.service.resource;

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
import com.google.inject.persist.Transactional;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eurekaclinical.common.util.UserSupport;
import org.eurekaclinical.standardapis.exception.HttpStatusException;
import org.eurekaclinical.useragreement.client.comm.UserAgreement;
import org.eurekaclinical.useragreement.client.comm.UserAgreementStatus;
import org.eurekaclinical.useragreement.service.dao.UserAgreementDao;
import org.eurekaclinical.useragreement.service.entity.UserAgreementStatusEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eurekaclinical.useragreement.service.dao.UserAgreementStatusDao;
import org.eurekaclinical.useragreement.service.entity.UserAgreementEntity;

/**
 * RESTful end-point for {@link UserAgreementStatusEntity} related methods.
 *
 * @author Andrew Post
 */
@Path("/protected/useragreementstatuses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class UserAgreementStatusResource {

    /**
     * The class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAgreementStatusResource.class);
    /**
     * Data access object to work with user agreement objects.
     */
    private final UserAgreementStatusDao userAgreementStatusDao;

    private final UserSupport userSupport;
    private final UserAgreementDao userAgreementDao;

    /**
     * Creates the user agreement REST endpoint.
     *
     * @param inUserAgreementStatusDao DAO used to access {@link UserAgreementStatusEntity}
     * related functionality.
     * @param inUserAgreementDao DAO used to access user agreements.
     */
    @Inject
    public UserAgreementStatusResource(UserAgreementStatusDao inUserAgreementStatusDao, UserAgreementDao inUserAgreementDao) {
        this.userAgreementStatusDao = inUserAgreementStatusDao;
        this.userAgreementDao = inUserAgreementDao;
        this.userSupport = new UserSupport();
    }

    /**
     * Get a list of all users in the system.
     *
     * @return a list of {@link UserAgreement} objects.
     */
    @GET
    @RolesAllowed({"admin"})
    public List<UserAgreementStatus> getAll() {
        List<UserAgreementStatusEntity> uaes = this.userAgreementStatusDao.getAll();
        List<UserAgreementStatus> result = new ArrayList<>(uaes.size());
        for (UserAgreementStatusEntity uae : uaes) {
            UserAgreementStatus status = new UserAgreementStatus();
            status.setId(uae.getId());
            status.setUsername(uae.getUsername());
            status.setExpiry(uae.getExpiry());
            status.setFullname(uae.getFullname());
            status.setStatus(uae.getState());
            status.setUserAgreement(uae.getUserAgreement().getId());
            result.add(status);
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
    public UserAgreementStatus getById(@PathParam("id") Long inId, @Context HttpServletRequest req) {
        UserAgreementStatusEntity uae = this.userAgreementStatusDao.retrieve(inId);
        if (uae == null) {
            throw new HttpStatusException(Response.Status.NOT_FOUND);
        }
        String username = uae.getUsername();
        checkUsername(username, req, Response.Status.FORBIDDEN);
        UserAgreementStatus status = new UserAgreementStatus();
        status.setId(uae.getId());
        status.setUsername(uae.getUsername());
        status.setExpiry(uae.getExpiry());
        status.setFullname(uae.getFullname());
        status.setStatus(uae.getState());
        status.setUserAgreement(uae.getUserAgreement().getId());
        return status;
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
    public UserAgreementStatus getMine(@Context HttpServletRequest req) {
        Principal principal = req.getUserPrincipal();
        String username = principal.getName();
        UserAgreementStatusEntity uae = this.userAgreementStatusDao.getByUsername(username);
        if (uae == null) {
            throw new HttpStatusException(Response.Status.NOT_FOUND);
        }
        UserAgreementStatus status = new UserAgreementStatus();
        status.setId(uae.getId());
        status.setUsername(uae.getUsername());
        status.setExpiry(uae.getExpiry());
        status.setFullname(uae.getFullname());
        status.setStatus(uae.getState());
        status.setUserAgreement(uae.getUserAgreement().getId());
        return status;
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
    public Response create(UserAgreementStatus inUserAgreement, @Context HttpServletRequest req) {
        String username = inUserAgreement.getUsername();
        if (!req.getRemoteUser().equals(username)) {
            throw new HttpStatusException(Response.Status.BAD_REQUEST);
        }
        UserAgreementStatusEntity userAgreementEntity = this.userAgreementStatusDao.getByUsername(username);
        if (userAgreementEntity == null) {
            userAgreementEntity = new UserAgreementStatusEntity();
            userAgreementEntity.setUsername(username);
        }
        userAgreementEntity.setFullname(inUserAgreement.getFullname());
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, 1);
        userAgreementEntity.setExpiry(now.getTime());
        UserAgreementEntity uae = this.userAgreementDao.retrieve(inUserAgreement.getUserAgreement());
        if (uae == null) {
            throw new HttpStatusException(Response.Status.BAD_REQUEST);
        }
        userAgreementEntity.setUserAgreement(uae);
        this.userAgreementStatusDao.create(userAgreementEntity);
        return Response.created(URI.create("/" + userAgreementEntity.getId())).build();
    }

    private void checkUsername(String username, HttpServletRequest req, Response.Status status) {
        if (!req.isUserInRole("admin") && !this.userSupport.isSameUser(req, username)) {
            throw new HttpStatusException(status);
        }
    }
}
