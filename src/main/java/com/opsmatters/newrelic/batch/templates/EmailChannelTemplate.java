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

package com.opsmatters.newrelic.batch.templates;

import com.opsmatters.newrelic.api.model.alerts.channels.EmailChannel;
import com.opsmatters.newrelic.api.model.alerts.channels.EmailConfiguration;

/**
 * Template that defines the email alert channel file format.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class EmailChannelTemplate extends FileTemplate
{
    /**
     * The type of the template.  
     */
    public static final String TYPE = "email-channel";

    // The template columns
    public TemplateColumn NAME = TemplateColumn.builder()
        .name(EmailChannel.NAME)
        .header("Name")
        .build();
    public TemplateColumn RECIPIENTS = TemplateColumn.builder()
        .name(EmailConfiguration.RECIPIENTS)
        .header("Recipients")
        .build();
    public TemplateColumn INCLUDE_JSON_ATTACHMENT = TemplateColumn.builder()
        .name(EmailConfiguration.INCLUDE_JSON_ATTACHMENT)
        .header("Include JSON Attachment")
        .mandatory(false)
        .defaultValue("true")
        .build();

    /**
     * Default constructor.
     */
    public EmailChannelTemplate()
    {
        addColumn(NAME);
        addColumn(TEMPLATE_TYPE);
        addColumn(RECIPIENTS);
        addColumn(INCLUDE_JSON_ATTACHMENT);
    }

    /**
     * Returns the type of the template.
     * @return The type of the template
     */
    public String getType()
    {
        return TYPE;
    }
}