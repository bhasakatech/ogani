package com.bhasaka.ogani.core.models;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import org.apache.sling.api.resource.Resource;
import com.bhasaka.ogani.core.models.productCarousel.*;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class RelatedProducts {

    @ValueMapValue
    private String sectionTitle;

    @ChildResource(name = "products")
    private List<Resource> productResources;

    private List<Product> products;

    @PostConstruct
    protected void init() {
        if (productResources != null) {
            products = productResources.stream()
                    .map(res -> {
                        String path = res.getValueMap().get("productPath", String.class);
                        if (path != null) {
                            Resource cfResource = res.getResourceResolver().getResource(path + "/jcr:content/data/master");
                            return cfResource != null ? cfResource.adaptTo(Product.class) : null;
                        }
                        return null;
                    })
                    .filter(p -> p != null)
                    .collect(Collectors.toList());
        }
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public List<Product> getProducts() {
        return products;
    }
    
}
