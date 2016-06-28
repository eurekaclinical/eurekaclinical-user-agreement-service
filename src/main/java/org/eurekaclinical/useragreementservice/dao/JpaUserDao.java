package org.eurekaclinical.useragreementservice.dao;

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

import javax.inject.Inject;
import org.eurekaclinical.standardapis.dao.UserDao;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.eurekaclinical.useragreementservice.entity.UserEntity;
import org.eurekaclinical.useragreementservice.entity.UserEntity_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of the {@link UserDao} interface, backed by JPA entities
 * and queries.
 *
 * @author Andrew Post
 */
public class JpaUserDao extends org.eurekaclinical.standardapis.dao.JpaUserDao<UserEntity> {

    /**
     * The class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            JpaUserDao.class);

    /**
     * Create an object with the give entity manager.
     *
     * @param inEMProvider The entity manager to be used for communication with
     * the data store.
     */
    @Inject
    public JpaUserDao(final Provider<EntityManager> inEMProvider) {
        super(UserEntity.class, inEMProvider);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return getUniqueByAttribute(UserEntity_.username, username);
    }

}
