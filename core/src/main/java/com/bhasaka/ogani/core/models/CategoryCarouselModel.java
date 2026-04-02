package com.bhasaka.ogani.core.models;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.bhasaka.ogani.core.models.beans.Category;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class)
public class CategoryCarouselModel {

    @Inject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String cfPath;

    private List<Category> categories = new ArrayList<>();

    @PostConstruct
    protected void init() {

        Resource folder = resourceResolver.getResource(cfPath);

        if (folder != null) {
            Iterator<Resource> fragments = folder.listChildren();

            while (fragments.hasNext()) {
                Resource cf = fragments.next();

                Resource data = cf.getChild("jcr:content/data/master");

                if (data != null) {
                    String title = data.getValueMap().get("title", String.class);
                    String image = data.getValueMap().get("image", String.class);
                    String link = data.getValueMap().get("link", String.class);

                    if (title != null && image != null) {
                        categories.add(new Category(title, image, link));
                    }
                }
            }
        }
    }

    public List<Category> getCategories() {
        return categories;
    }
    
}
