package com.bhasaka.ogani.core.config;

import org.apache.http.client.methods.HttpUriRequest;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Quote Service Configuration")
public @interface QuoteServiceConfiguration {

    @AttributeDefinition(name = "Enable Config")
    boolean enabled() default true;

    @AttributeDefinition(name = "API Url")
    public String apiURL() default "zenquotes.io/api/random";





}
