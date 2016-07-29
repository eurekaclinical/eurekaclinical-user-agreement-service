package org.eurekaclinical.useragreementservice.entity;

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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eurekaclinical.useragreementcommon.comm.Status;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "user_agreement_statuses")
public class UserAgreementStatusEntity {
    
    @Id
    @SequenceGenerator(name = "USER_AGR_STAT_SEQ_GENERATOR", sequenceName = "USER_AGR_STAT_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "USER_AGR_STAT_SEQ_GENERATOR")
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String fullname;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private UserAgreementEntity userAgreement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public UserAgreementEntity getUserAgreement() {
        return userAgreement;
    }

    public void setUserAgreement(UserAgreementEntity userAgreement) {
        this.userAgreement = userAgreement;
    }
    
    public boolean isExpired() {
        Date now = new Date();
        return !now.before(this.expiry);
    }
    
    public boolean isActive() {
        return !isExpired();
    }
    
    public Status getState() {
        if (isExpired()) {
            return Status.EXPIRED;
        } else {
            return Status.ACTIVE;
        }
    }
}
