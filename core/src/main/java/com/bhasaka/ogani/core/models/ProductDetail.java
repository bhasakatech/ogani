package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.beans.Product;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductDetail {

    private static final Logger LOG = LoggerFactory.getLogger(ProductDetail.class);

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    @ValueMapValue
    private String cfPath;

    @ValueMapValue private String availability;
    @ValueMapValue private String shipping;
    @ValueMapValue private String weight;

    @ValueMapValue private String facebook;
    @ValueMapValue private String twitter;
    @ValueMapValue private String instagram;
    @ValueMapValue private String pinterest;

    // New tab fields from dialog
    @ValueMapValue private String prdDesc;
    @ValueMapValue private String prdInfo;
    @ValueMapValue private String prdReviews;

    private List<Product> products = new ArrayList<>();

    @PostConstruct
    protected void init() {
        cfPath = resource.getValueMap().get("cfPath", String.class);
        LOG.info("CF PATH = {}", cfPath);

        if (cfPath == null || cfPath.isEmpty()) return;

        Resource parent = resourceResolver.getResource(cfPath);
        if (parent == null) return;

        for (Resource child : parent.getChildren()) {
            Resource data = child.getChild("jcr:content/data/master");
            if (data != null) {
                ValueMap vm = data.getValueMap();
                String title = vm.get("title", String.class);
                String price = vm.get("price", String.class);
                String image = vm.get("image", String.class);
                String desc = vm.get("description", String.class);
                String[] category = vm.get("category", String[].class);

                products.add(new Product(title, price, image, desc, category));
            }
        }
    }

    public List<Product> getProducts() { return products; }

    public String getAvailability() { return availability; }
    public String getShipping() { return shipping; }
    public String getWeight() { return weight; }

    public String getFacebook() { return facebook; }
    public String getTwitter() { return twitter; }
    public String getInstagram() { return instagram; }
    public String getPinterest() { return pinterest; }

    public String getPrdDesc() {
        return prdDesc;
    }

    public String getPrdInfo() {
        return prdInfo;
    }

    public String getPrdReviews() {
        return prdReviews;
    }
}