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



import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Provides an API for evaluating Freemarker Template Language expressions.
 * 
 * For more information about Freemarker Template Language expressions, see
 * <a href="http://freemarker.org/docs/dgui_template_exp.html">The
 * FreeMarker Manual</a>.
 * 
 * @author Andrew Post
 */
class FreemarkerBuiltIns {

    private final Configuration cfg;

    FreemarkerBuiltIns() {
        Version freemarkerVersionCompat = new Version(2, 3, 23);
        this.cfg = new Configuration(freemarkerVersionCompat);
        this.cfg.setObjectWrapper(new DefaultObjectWrapper(freemarkerVersionCompat));
    }

    /**
     * Evaluates a string as a Freemarker Template Language expression.
     *
     * @param expr a Freemarker Template Language expression. Cannot be
     * <code>null</code>.
     *
     * @return the result of the expression as a string. Guaranteed not
     * <code>null</code>.
     *
     * @throws TemplateException if an error occurred evaluating the expression.
     */
    String eval(String expr) throws TemplateException {
        return eval(expr, null);
    }

    /**
     * Evaluates a string as a Freemarker Template Language expression.
     * 
     * @param expr a Freemarker Template Language expression. Cannot be
     * <code>null</code>.
     * @param model a map of variable names to values. The variables may be used
     * in the expression.
     *
     * @return the result of the expression as a string. Guaranteed not
     * <code>null</code>.
     *
     * @throws TemplateException if an error occurred evaluating the expression.
     */
    String eval(String expr, Map<String, ? extends Object> model) throws TemplateException {
        if (expr == null) {
            throw new IllegalArgumentException("expr cannot be null");
        }
        try {
            Template t = new Template("t", "${(" + expr.trim() + ")?c}", this.cfg);
            StringWriter w = new StringWriter();
            try (StringWriter ww = w) {
                t.process(model, ww);
            }
            return w.toString();
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
    }

}
