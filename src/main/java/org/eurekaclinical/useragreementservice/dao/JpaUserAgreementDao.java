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

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.eurekaclinical.standardapis.dao.GenericDao;

import org.eurekaclinical.useragreementservice.entity.UserAgreementEntity;
import org.eurekaclinical.useragreementservice.entity.UserAgreementEntity_;

/**
 * Implements the {@link UserAgreementDao} interface, with the use of JPA entity
 * managers.
 *
 * @author Andrew Post
 *
 */
public class JpaUserAgreementDao extends GenericDao<UserAgreementEntity, Long> implements UserAgreementDao {

    /**
     * The class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaUserAgreementDao.class);

    /**
     * Construct instance with the given EntityManager provider.
     *
     * @param inEMProvider The entity manager provider.
     */
    @Inject
    public JpaUserAgreementDao(final Provider<EntityManager> inEMProvider) {
        super(UserAgreementEntity.class, inEMProvider);
    }

    @Override
    public UserAgreementEntity getByUsername(String username) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserAgreementEntity> criteriaQuery = builder.createQuery(UserAgreementEntity.class);
        Root<UserAgreementEntity> root = criteriaQuery.from(UserAgreementEntity.class);
        Path<String> usernamePath = root.get(UserAgreementEntity_.username);
        TypedQuery<UserAgreementEntity> query = entityManager.createQuery(criteriaQuery.where(
                builder.equal(usernamePath, username)));
        UserAgreementEntity result = null;
        try {
            result = query.getSingleResult();
        } catch (NonUniqueResultException nure) {
            String msg = MessageFormat.format(
                    "More than one user with {0} = {1}",
                    UserAgreementEntity_.username, username);
            LOGGER.error(msg);
            throw new AssertionError(msg);
        } catch (NoResultException nre) {
            LOGGER.debug("No user with {} = {} and {} = {}",
                    UserAgreementEntity_.username, username);
        }
        return result;
    }

}
