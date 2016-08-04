package org.eurekaclinical.useragreement.service.dao;

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

import java.util.Date;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eurekaclinical.standardapis.dao.GenericDao;
import org.eurekaclinical.useragreement.service.entity.UserAgreementEntity;
import org.eurekaclinical.useragreement.service.entity.UserAgreementEntity_;

/**
 *
 * @author Andrew Post
 */
public class JpaUserAgreementDao extends GenericDao<UserAgreementEntity, Long> implements UserAgreementDao {

    /**
     * Construct instance with the given EntityManager provider.
     *
     * @param inEMProvider The entity manager provider.
     */
    @Inject
    public JpaUserAgreementDao(final Provider<EntityManager> inEMProvider) {
        super(UserAgreementEntity.class, inEMProvider);
    }
    
    /**
     * Gets the current user agreement.
     * 
     * @return a user agreement entity, or <code>null</code> if none has been
     * created.
     */
    @Override
    public UserAgreementEntity getCurrent() {
        UserAgreementEntity result;
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserAgreementEntity> criteriaQuery = builder.createQuery(UserAgreementEntity.class);
        Root<UserAgreementEntity> root = criteriaQuery.from(UserAgreementEntity.class);
        criteriaQuery.where(
                builder.or(
                        builder.isNull(root.get(UserAgreementEntity_.expiredAt)),
                        builder.greaterThanOrEqualTo(root.get(UserAgreementEntity_.expiredAt), new Date())
                ));
        try {
            result = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException ex) {
            result = null;
        }
        return result;
    }

    /**
     * Updates the user agreement, or creates it if none has been previously
     * created.
     * 
     * @param inText the text of the agreement in Markdown format.
     * @return a user agreement entity.
     */
    @Override
    public UserAgreementEntity createOrUpdate(String inText) {
        if (inText == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        UserAgreementEntity current = getCurrent();
        Date now = new Date();
        if (current != null) {
            current.setExpiredAt(now);
        }
        UserAgreementEntity result = new UserAgreementEntity();
        result.setText(inText);
        result.setEffectiveAt(now);
        return create(result);
    }
}
