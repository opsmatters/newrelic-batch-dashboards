/*
 * Copyright 2018 Gerald Curley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opsmatters.newrelic.batch.parsers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import com.opsmatters.core.documents.InputFileReader;
import com.opsmatters.newrelic.api.model.alerts.policies.AlertPolicy;
import com.opsmatters.newrelic.api.model.alerts.conditions.NrqlAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.Term;
import com.opsmatters.newrelic.api.model.alerts.conditions.Nrql;
import com.opsmatters.newrelic.batch.templates.FileTemplate;
import com.opsmatters.newrelic.batch.templates.TemplateFactory;
import com.opsmatters.newrelic.batch.templates.FileInstance;

/**
 * Parser that converts NRQL alert conditions from report lines.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class NrqlAlertConditionParser extends TermsConditionParser<NrqlAlertCondition>
{
    private static final Logger logger = Logger.getLogger(NrqlAlertConditionParser.class.getName());

    /**
     * Private constructor.
     */
    private NrqlAlertConditionParser()
    {
    }

    /**
     * Register this class with the given template.
     * @param template The template to register with this class
     */
    public static void registerTemplate(FileTemplate template)
    {
        TemplateFactory.registerTemplate(NrqlAlertConditionParser.class, template);
    }

    /**
     * Reads the alert conditions from the given lines.
     * @param policies The set of alert policies for the conditions
     * @param headers The headers of the file
     * @param lines The lines of the file
     * @return The alert conditions read from the lines
     */
    public static List<NrqlAlertCondition> parse(List<AlertPolicy> policies, String[] headers, List<String[]> lines)
    {
        return new NrqlAlertConditionParser().get(policies, headers, lines);
    }

    /**
     * Reads the alert conditions from the given reader.
     * @param policies The set of alert policies for the conditions
     * @param reader The input stream reader used to read the lines
     * @return The alert conditions read from the lines
     * @throws IOException if there is a problem reading the input file or it does not exist
     */
    public static List<NrqlAlertCondition> parse(List<AlertPolicy> policies, InputFileReader reader) throws IOException
    {
        reader.parse();
        return parse(policies, reader.getHeaders(), reader.getRows());
    }

    /**
     * Reads the alert condition from the given line.
     * @param file The file instance with the columns
     * @param line The input file line
     * @return The alert condition created
     */
    protected NrqlAlertCondition create(FileInstance file, String[] line)
    {
        String name = file.getString(NrqlAlertCondition.NAME, line);
        List<Term> terms = getTerms(file, line);

        Nrql nrql = Nrql.builder()
            .query(file.getString(Nrql.QUERY, line))
            .sinceValue(file.getInteger(Nrql.SINCE_VALUE, line))
            .build();

        NrqlAlertCondition ret = NrqlAlertCondition.builder()
            .name(file.getString(NrqlAlertCondition.NAME, line))
            .valueFunction(file.getString(NrqlAlertCondition.VALUE_FUNCTION, line))
            .nrql(nrql)
            .terms(terms)
            .enabled(true)
            .build();

        if(ret.numTerms() == 0)
            throw new IllegalArgumentException("NRQL alert condition missing thresholds: "+name);

        return ret;
    }
}