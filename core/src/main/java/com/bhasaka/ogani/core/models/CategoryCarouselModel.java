package com.bhasaka.ogani.core.models;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import com.bhasaka.ogani.core.models.beans.ProductCategory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CategoryCarouselModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String cfPath;

    private List<ProductCategory> categories = new ArrayList<>();

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
                        categories.add(new ProductCategory(title, image, link));
                    }
                }
            }
        }
    }

    public List<ProductCategory> getCategories() {
        return categories;
    }
    
}
