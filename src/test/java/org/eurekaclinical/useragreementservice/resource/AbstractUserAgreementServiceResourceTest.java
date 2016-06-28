package org.eurekaclinical.useragreementservice.resource;

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

import javax.servlet.ServletContextListener;

import com.google.inject.Module;

import org.eurekaclinical.useragreementservice.config.AppTestModule;
import org.eurekaclinical.useragreementservice.config.ContextTestListener;
import org.eurekaclinical.useragreementservice.test.AbstractResourceTest;
import org.eurekaclinical.useragreementservice.test.Setup;
import org.eurekaclinical.useragreementservice.test.TestDataProvider;

/**
 * @author hrathod
 */
public abstract class AbstractUserAgreementServiceResourceTest extends AbstractResourceTest {

    @Override
    protected final Class<? extends ServletContextListener> getListener() {
        return ContextTestListener.class;
    }

    @Override
    protected Class<? extends TestDataProvider> getDataProvider() {
        return Setup.class;
    }

    @Override
    protected Module[] getModules() {
        return new Module[]{new AppTestModule()};
    }
}
