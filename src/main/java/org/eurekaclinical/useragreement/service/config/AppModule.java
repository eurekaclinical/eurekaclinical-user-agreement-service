package org.eurekaclinical.useragreement.service.config;

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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
 
import org.eurekaclinical.standardapis.dao.RoleDao;
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.dao.UserTemplateDao;
import org.eurekaclinical.useragreement.service.dao.JpaRoleDao;
import org.eurekaclinical.useragreement.service.dao.JpaUserAgreementDao;
import org.eurekaclinical.useragreement.service.dao.JpaUserAgreementStatusDao;
import org.eurekaclinical.useragreement.service.dao.JpaUserDao;
import org.eurekaclinical.useragreement.service.dao.UserAgreementDao;
import org.eurekaclinical.useragreement.service.dao.UserAgreementServiceRoleDao;
import org.eurekaclinical.useragreement.service.dao.UserAgreementStatusDao;
import org.eurekaclinical.useragreement.service.entity.AuthorizedRoleEntity;
import org.eurekaclinical.useragreement.service.entity.AuthorizedUserEntity;

/**
 * @author arpost
 */
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
    	 bind(UserAgreementServiceRoleDao.class).to(JpaRoleDao.class);
        bind(UserAgreementDao.class).to(JpaUserAgreementDao.class);
        bind(UserAgreementStatusDao.class).to(JpaUserAgreementStatusDao.class);
        bind(new TypeLiteral<UserDao<AuthorizedUserEntity>>() {}).to(JpaUserDao.class);
        bind(new TypeLiteral<UserDao<? extends org.eurekaclinical.standardapis.entity.UserEntity<? extends org.eurekaclinical.standardapis.entity.RoleEntity>>>() {}).to(JpaUserDao.class);
        bind(new TypeLiteral<RoleDao<AuthorizedRoleEntity>>() {}).to(JpaRoleDao.class);
    }
}
