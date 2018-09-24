package org.eurekaclinical.useragreement.service.dao;

/*-
 * #%L
 * Eureka! Clinical User Agreement Service
 * %%
 * Copyright (C) 2016 - 2018 Emory University
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



import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.eurekaclinical.standardapis.dao.AbstractJpaUserTemplateDao;
import org.eurekaclinical.standardapis.entity.UserEntity;
import org.eurekaclinical.useragreement.service.entity.AuthorizedRoleEntity;
import org.eurekaclinical.useragreement.service.entity.AuthorizedUserEntity;
import org.eurekaclinical.useragreement.service.entity.UserTemplateEntity;
import org.eurekaclinical.useragreement.service.entity.UserTemplateEntity_;

/**
 *
 *
 * @author Dileep Gunda
 */
public class JpaUserTemplateDao extends AbstractJpaUserTemplateDao<AuthorizedRoleEntity,UserTemplateEntity> {

    /**
     * Create an object with the give entity manager.
     *
     * @param inEMProvider The entity manager to be used for communication with
     * the data store.
     */
    @Inject
    public JpaUserTemplateDao(final Provider<EntityManager> inEMProvider) {
        super(UserTemplateEntity.class, inEMProvider);
    }

    @Override
    public UserTemplateEntity getAutoAuthorizationTemplate() {
        List<UserTemplateEntity> result = this.getListByAttribute(UserTemplateEntity_.autoAuthorize, Boolean.TRUE);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public UserEntity newUserEntityInstance(String username, List<AuthorizedRoleEntity> roles) {
         AuthorizedUserEntity user = new AuthorizedUserEntity();
            user.setUsername(username);
            user.setRoles(roles);
            return user;
    }

}
