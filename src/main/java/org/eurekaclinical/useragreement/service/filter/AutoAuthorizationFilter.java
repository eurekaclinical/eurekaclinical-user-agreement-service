package org.eurekaclinical.useragreement.service.filter;

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


import org.eurekaclinical.useragreement.service.entity.UserEntity;
import org.eurekaclinical.useragreement.service.entity.UserTemplateEntity;
import org.eurekaclinical.useragreement.service.entity.RoleEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eurekaclinical.common.filter.AbstractAutoAuthorizationFilter;
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.dao.UserTemplateDao;




@Singleton
public class AutoAuthorizationFilter extends AbstractAutoAuthorizationFilter<RoleEntity, UserEntity, UserTemplateEntity>{
 
@Inject
    public AutoAuthorizationFilter(UserTemplateDao<UserTemplateEntity> inUserTemplateDao,
            UserDao<UserEntity> inUserDao) {
        super(inUserTemplateDao, inUserDao);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected UserEntity toUserEntity(UserTemplateEntity userTemplate, String username) {
        UserEntity user = new UserEntity();
        user.setUsername(username); 
        user.setRoles(userTemplate.getRoles());
        return user;

    } 

}
