package com.bhasaka.ogani.core.services;


import org.apache.sling.api.resource.ResourceResolver;
import javax.json.JsonArray;


public interface CfSearchService {
    JsonArray getSuggestions(String searchTerm, ResourceResolver resolver);
}