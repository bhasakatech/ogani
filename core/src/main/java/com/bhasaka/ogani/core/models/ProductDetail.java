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

/**
 * Sling Model for the Product Detail component.
 * <p>
 * This model adapts from a {@link SlingHttpServletRequest} and exposes authored fields
 * such as availability, shipping, weight, social links, product descriptions, and
 * dynamically loads product data from a Content Fragment path.
 * </p>
 *
 * <h3>Usage in HTL:</h3>
 * <pre>
 *     ${productDetail.availability}
 *     ${productDetail.products}
 * </pre>
 *
 * The model retrieves child resources under the configured Content Fragment path
 * and maps them into {@link Product} beans.
 */
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

    /** Path to the Content Fragment root. */
    @ValueMapValue
    private String cfPath;

    /** Product availability text. */
    @ValueMapValue private String availability;
    /** Shipping information text. */
    @ValueMapValue private String shipping;
    /** Product weight text. */
    @ValueMapValue private String weight;

    /** Social media links. */
    @ValueMapValue private String facebook;
    @ValueMapValue private String twitter;
    @ValueMapValue private String instagram;
    @ValueMapValue private String pinterest;

    /** Add-to-cart button label. */
    @ValueMapValue private String addButton;

    /** Tab titles. */
    @ValueMapValue private String prdDescTitle;
    @ValueMapValue private String prdInfoTitle;
    @ValueMapValue private String prdReviewTitle;

    /** Tab content. */
    @ValueMapValue private String prdDesc;
    @ValueMapValue private String prdInfo;
    @ValueMapValue private String prdReviews;

    /** List of products loaded from Content Fragment children. */
    private List<Product> products = new ArrayList<>();

    /**
     * Initialization logic to load product data from the Content Fragment path.
     * Runs after Sling Model injection.
     */
    @PostConstruct
    protected void init() {
        LOG.info("Initializing ProductDetail model for resource: {}", resource.getPath());

        cfPath = resource.getValueMap().get("cfPath", String.class);
        LOG.info("Configured Content Fragment path: {}", cfPath);

        if (cfPath == null || cfPath.isEmpty()) {
            LOG.warn("No Content Fragment path authored, skipping product loading.");
            return;
        }

        Resource parent = resourceResolver.getResource(cfPath);
        if (parent == null) {
            LOG.error("Unable to resolve Content Fragment path: {}", cfPath);
            return;
        }

        for (Resource child : parent.getChildren()) {
            Resource data = child.getChild("jcr:content/data/master");
            if (data != null) {
                ValueMap vm = data.getValueMap();
                String title = vm.get("title", String.class);
                String price = vm.get("price", String.class);
                String image = vm.get("image", String.class);
                String desc = vm.get("description", String.class);
                String[] category = vm.get("category", String[].class);

                LOG.debug("Loaded product: title={}, price={}, image={}", title, price, image);
                products.add(new Product(title, price, image, desc, category));
            } else {
                LOG.warn("Child resource {} missing expected data node.", child.getPath());
            }
        }

        LOG.info("Total products loaded: {}", products.size());
    }

    /** @return list of products loaded from Content Fragment children */
    public List<Product> getProducts() { return products; }

    public String getAvailability() {
        LOG.debug("Availability retrieved: {}", availability);
        return availability;
    }
    public String getShipping() {
        LOG.debug("Shipping retrieved: {}", shipping);
        return shipping;
    }
    public String getWeight() {
        LOG.debug("Weight retrieved: {}", weight);
        return weight;
    }

    public String getFacebook() { return facebook; }
    public String getTwitter() { return twitter; }
    public String getInstagram() { return instagram; }
    public String getPinterest() { return pinterest; }

    public String getPrdDesc() { return prdDesc; }
    public String getPrdInfo() { return prdInfo; }
    public String getPrdReviews() { return prdReviews; }

    public String getPrdDescTitle() { return prdDescTitle; }
    public String getPrdInfoTitle() { return prdInfoTitle; }
    public String getPrdReviewTitle() { return prdReviewTitle; }

    public String getAddButton() { return addButton; }
}
