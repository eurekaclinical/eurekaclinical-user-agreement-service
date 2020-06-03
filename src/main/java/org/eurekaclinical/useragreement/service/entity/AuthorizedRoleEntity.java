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

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A bean class to hold information related to roles in the system.
 *
 * @author hrathod
 *
 */
@Entity
@Table(name = "roles")
public class AuthorizedRoleEntity implements org.eurekaclinical.standardapis.entity.RoleEntity {

    /**
     * The role's unique identifier.
     */
    @Id
    @SequenceGenerator(name = "ROLE_SEQ_GENERATOR", sequenceName = "ROLE_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ROLE_SEQ_GENERATOR")
    private Long id;
    /**
     * The role's name.
     */
    @Column(unique = true, nullable = false)
    private String name;
    /**
     * Is this role a default role? Default roles are assigned to all new users.
     */
    private boolean defaultRole;

    /**
     * Create an empty role.
     */
    public AuthorizedRoleEntity() {
    }

    /**
     * Get the role's identification number.
     *
     * @return A {@link Long} representing the role's id.
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Set the role's identification number.
     *
     * @param inId The number representing the role's id.
     */
    @Override
    public void setId(Long inId) {
        this.id = inId;
    }

    /**
     * Get the role's name.
     *
     * @return A String containing the role's name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Set the role's name.
     *
     * @param inName A string containing the role's name.
     */
    @Override
    public void setName(String inName) {
        this.name = inName;
    }

    /**
     * Is this role a default role?
     *
     * @return True if the role is a default role, false otherwise.
     */
    @Override
    public boolean isDefaultRole() {
        return this.defaultRole;
    }

    /**
     * Set the role's default flag.
     *
     * @param inDefaultRole True or False, True indicating a default role, False
     * indicating a non-default role.
     */
    @Override
    public void setDefaultRole(boolean inDefaultRole) {
        this.defaultRole = inDefaultRole;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final AuthorizedRoleEntity other = (AuthorizedRoleEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RoleEntity{" + "id=" + id + ", name=" + name + ", defaultRole=" + defaultRole + '}';
    }

}
