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
import com.google.inject.persist.Transactional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import org.eurekaclinical.common.comm.Role;
import org.eurekaclinical.common.resource.AbstractRoleResource;
import org.eurekaclinical.useragreement.service.dao.UserAgreementServiceRoleDao;
import org.eurekaclinical.useragreement.service.entity.AuthorizedRoleEntity;


/**
 *
 * @author Andrew Post
 */
@Path("/protected/roles")
@Transactional
public class RoleResource extends AbstractRoleResource<AuthorizedRoleEntity, Role> {

    @Inject
    public RoleResource(UserAgreementServiceRoleDao inRoleDao) {
        super(inRoleDao);
    }

    @Override
    protected Role toComm(AuthorizedRoleEntity roleEntity, HttpServletRequest req) {
        Role role = new Role();
        role.setId(roleEntity.getId());
        role.setName(roleEntity.getName());
        return role;
    }

}
