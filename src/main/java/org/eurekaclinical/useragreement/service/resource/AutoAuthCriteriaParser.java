package org.eurekaclinical.useragreement.service.resource;

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

  
import freemarker.template.TemplateException;
import java.util.Map;

/**
 * Parses auto-authorization criteria expressed as a Freemarker Template
 * Language expression.
 * 
 * @author Andrew Post
 */
public class AutoAuthCriteriaParser {

    private static final FreemarkerBuiltIns FREEMARKER_BUILTINS = new FreemarkerBuiltIns();

    /**
     * Parses the provided criteria expression, which must have a 
     * boolean value.
     * 
     * @param criteria the criteria expression string. If <code>null</code>,
     * this method returns <code>true</code>.
     * @param attributes any user attribute-value pairs. The attribute names
     * may be used as variables.
     * @return <code>true</code> if the provided criteria is <code>null</code>
     * or evaluates to <code>true</code>, <code>false</code> otherwise.
     * @throws CriteriaParseException if an error occurred parsing the criteria
     * expression, most likely because the expression is invalid.
     */
    public boolean parse(String criteria, Map<String, ? extends Object> attributes) throws CriteriaParseException {
        if (criteria == null) {
            return true;
        } else {
            try {
                return Boolean.parseBoolean(FREEMARKER_BUILTINS.eval(criteria, attributes));
            } catch (TemplateException ex) {
                throw new CriteriaParseException(ex);
            }
        }
    }

}
