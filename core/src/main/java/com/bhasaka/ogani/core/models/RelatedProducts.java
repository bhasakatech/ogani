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


/**
 * Sling Model for Related Products component.
 *
 * This model is responsible for fetching and exposing a list of related products
 * based on product references configured in the component.
 *
 * Data Source:
 * - Reads sectionTitle from component properties
 * - Reads child resources under "products"
 * - Each child resource contains a productPath pointing to a content fragment
 *
 * Responsibilities:
 * - Iterate over product reference resources
 * - Resolve product paths using ResourceResolver
 * - Fetch content fragment data from jcr:content/data/master
 * - Adapt resources to Product model
 * - Filter out invalid or null products
 *
 * Flow:
 * - HTL adapts resource to this model
 * - ValueMapValue injects sectionTitle
 * - ChildResource injects product reference nodes
 * - PostConstruct initializes product list
 * - Each productPath is resolved to actual product data
 * - Valid Product objects are collected into a list
 *
 * Behavior:
 * - Skips entries where productPath is null
 * - Skips entries where resource cannot be resolved
 * - Filters out null Product objects
 * - Returns null or empty list if no valid products exist
 *
 * Injection Strategy:
 * Uses DefaultInjectionStrategy.OPTIONAL to safely handle missing properties.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class RelatedProducts {

    /**
     * Title of the related products section.
     */
    @ValueMapValue
    private String sectionTitle;

    /**
     * List of child resources containing product references.
     */
    @ChildResource(name = "products")
    private List<Resource> productResources;

    /**
     * List of resolved Product objects.
     */
    private List<Product> products;

    /**
     * Initializes the model after dependency injection.
     * Logic:
     * - Iterates through productResources
     * - Reads productPath from each resource
     * - Resolves content fragment resource
     * - Adapts resource to Product model
     * - Filters out null values
     * Ensures only valid Product objects are added to the list.
     */
    @PostConstruct
    protected void init() {
        if (productResources != null) {
            products = productResources.stream()
                    .map(res -> {
                        String path = res.getValueMap().get("productPath", String.class);
                        if (path != null) {
                            Resource cfResource = res.getResourceResolver()
                                    .getResource(path + "/jcr:content/data/master");
                            return cfResource != null ? cfResource.adaptTo(Product.class) : null;
                        }
                        return null;
                    })
                    .filter(p -> p != null)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Returns section title.
     * @return section title
     */
    public String getSectionTitle() {
        return sectionTitle;
    }

    /**
     * Returns list of related products.
     * @return list of Product objects
     */
    public List<Product> getProducts() {
        return products;
    }
}