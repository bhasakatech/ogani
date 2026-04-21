package com.bhasaka.ogani.core.services.Impl;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.bhasaka.ogani.core.services.CfSearchService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.HashMap;
import java.util.Map;

@Component(service = CfSearchService.class)
public class CfSearchServiceImpl implements CfSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(CfSearchServiceImpl.class);

    private static final String CF_ROOT_PATH = "/content/dam/Ogani/content-fragments";

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    public JsonArray getSuggestions(String searchTerm, ResourceResolver resolver) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        try {
            Session session = resolver.adaptTo(Session.class);
            if (session == null) {
                return arrayBuilder.build();
            }

            Map<String, String> map = new HashMap<>();
            map.put("path", CF_ROOT_PATH);
            map.put("type", "dam:Asset");

            map.put("property", "jcr:content/contentFragment");
            map.put("property.value", "true");

            map.put("nodename", "*" + searchTerm + "*");

            map.put("p.limit", "10");

            Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
            SearchResult result = query.getResult();

            for (Hit hit : result.getHits()) {
                Resource resource = hit.getResource();
                if (resource != null) {
                    ContentFragment cf = resource.adaptTo(ContentFragment.class);
                    if (cf != null) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

                        objectBuilder.add("name", resource.getName());
                        objectBuilder.add("path", resource.getPath());

                        String imageUrl = "";
                        ContentElement imageElement = cf.getElement("image");

                        if (imageElement != null && imageElement.getContent() != null) {
                            imageUrl = imageElement.getContent();
                        }

                        objectBuilder.add("image", imageUrl);

                        arrayBuilder.add(objectBuilder);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error occurred while fetching Content Fragment suggestions for term: {}", searchTerm, e);
        }

        return arrayBuilder.build();
    }
}