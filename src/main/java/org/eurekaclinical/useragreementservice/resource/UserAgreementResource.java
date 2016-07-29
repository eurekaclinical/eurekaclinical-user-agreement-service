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
import com.google.inject.persist.Transactional;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eurekaclinical.standardapis.exception.HttpStatusException;
import org.eurekaclinical.useragreementcommon.comm.UserAgreement;
import org.eurekaclinical.useragreementservice.dao.UserAgreementDao;
import org.eurekaclinical.useragreementservice.entity.UserAgreementEntity;

/**
 *
 * @author Andrew Post
 */
@Path("/protected/useragreements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class UserAgreementResource {

    private final UserAgreementDao userAgreementDao;

    @Inject
    public UserAgreementResource(UserAgreementDao inUserAgreementDao) {
        this.userAgreementDao = inUserAgreementDao;
    }

    @GET
    public List<UserAgreement> getAll() {
        List<UserAgreement> results = new ArrayList<>();
        for (UserAgreementEntity uae : this.userAgreementDao.getAll()) {
            results.add(uae.toUserAgreement());
        }
        return results;
    }

    @GET
    @Path("/current")
    public UserAgreement getCurrent() {
        UserAgreementEntity uae = this.userAgreementDao.getCurrent();
        if (uae == null) {
            throw new HttpStatusException(Response.Status.NOT_FOUND);
        }
        return uae.toUserAgreement();
    }

    @GET
    @Path("/{id}")
    public UserAgreement getAny(@PathParam("id") Long inId, @Context HttpServletRequest req) {
        UserAgreementEntity uae = this.userAgreementDao.retrieve(inId);
        if (uae == null) {
            throw new HttpStatusException(Response.Status.NOT_FOUND);
        }
        return uae.toUserAgreement();
    }

    @RolesAllowed("admin")
    @POST
    public Response create(UserAgreement userAgreement) {
        UserAgreementEntity uae = this.userAgreementDao.createOrUpdate(userAgreement.getText());
        return Response.created(URI.create("/" + uae.getId())).build();
    }

}
