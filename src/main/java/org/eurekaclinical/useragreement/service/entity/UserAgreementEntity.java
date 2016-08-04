package org.eurekaclinical.useragreement.service.entity;

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
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eurekaclinical.useragreement.client.comm.UserAgreement;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "user_agreements")
public class UserAgreementEntity {

    @Id
    @SequenceGenerator(name = "USER_AGR_SEQ_GENERATOR", sequenceName = "USER_AGR_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "USER_AGR_SEQ_GENERATOR")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date effectiveAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredAt;

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEffectiveAt() {
        return effectiveAt;
    }

    public void setEffectiveAt(Date effectiveAt) {
        this.effectiveAt = effectiveAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public UserAgreement toUserAgreement() {
        UserAgreement result = new UserAgreement();
        result.setText(this.text);
        result.setId(this.id);
        result.setEffectiveAt(this.effectiveAt);
        result.setExpiredAt(this.expiredAt);
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserAgreementEntity other = (UserAgreementEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserAgreementEntity{" + "id=" + id + ", text=" + text + ", effectiveAt=" + effectiveAt + ", expiredAt=" + expiredAt + '}';
    }
    
}
