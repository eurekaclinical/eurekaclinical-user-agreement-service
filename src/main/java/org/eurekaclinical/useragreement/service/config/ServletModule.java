package org.eurekaclinical.useragreement.service.config;

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


import org.eurekaclinical.common.config.AbstractAuthorizingJerseyServletModuleWithPersist;
import org.eurekaclinical.useragreement.service.filter.AutoAuthorizationFilter;
import org.eurekaclinical.useragreement.service.props.UserAgreementServiceProperties;

public class ServletModule extends AbstractAuthorizingJerseyServletModuleWithPersist {

    private static final String PACKAGE_NAMES = "org.eurekaclinical.registry.service.resource";

    public ServletModule(UserAgreementServiceProperties inProperties) {
        super(inProperties, PACKAGE_NAMES, false);
    }

    
    @Override
    protected void setupFilters() {
        super.setupFilters();
        filter("/*").through(AutoAuthorizationFilter.class);
    }
}
