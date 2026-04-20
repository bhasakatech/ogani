package com.bhasaka.ogani.core.models.productCarousel;

import com.bhasaka.ogani.core.services.ProductService;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Sling Model for Product Carousel component.
 *
 * This model is responsible for fetching and exposing a list of Product
 * objects to the HTL layer for rendering a product carousel.
 *
 * The model adapts from a Sling Resource and retrieves configuration
 * properties from the JCR, such as the root path where product data is stored.
 *
 * Flow:
 * - HTL adapts the resource to this model
 * - ValueMapValue injects component properties like rootPath
 * - SlingObject injects ResourceResolver
 * - OSGiService injects ProductService
 * - PostConstruct initializes and fetches product data
 *
 * Responsibilities:
 * - Delegates product fetching logic to ProductService
 * - Stores list of products for rendering
 * - Provides helper methods for HTL such as isEmpty
 *
 * Injection Strategy:
 * Uses DefaultInjectionStrategy.OPTIONAL, meaning missing properties
 * will not cause errors.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCarouselModel {

    private static final Logger log = LoggerFactory.getLogger(ProductCarouselModel.class);

    /**
     * Root path in JCR where product content is stored.
     */
    @ValueMapValue
    private String rootPath;

    /**
     * Sling ResourceResolver used to access repository resources.
     */
    @SlingObject
    private ResourceResolver resolver;

    /**
     * OSGi service used to fetch product data.
     */
    @OSGiService
    private ProductService productService;

    /**
     * List of products fetched from the repository.
     */
    private List<Product> productList;

    /**
     * Initializes the model after dependency injection.
     *
     * <p>This method calls {@link ProductService#getProducts(String, ResourceResolver)}
     * to fetch product data based on the configured root path.</p>
     *
     * <p>Any exceptions during initialization are logged.</p>
     */
    @PostConstruct
    protected void init() {
        try {
            productList = productService.getProducts(rootPath, resolver);
        } catch (Exception e) {
            log.error("Error initializing ProductCarouselModel", e);
        }
    }

    /**
     * Returns the list of products.
     *
     * @return list of {@link Product}, or null if not initialized
     */
    public List<Product> getProductList() {
        return productList;
    }

    /**
     * Checks whether the product list is empty.
     *
     * @return true if product list is null or empty, false otherwise
     */
    public boolean isEmpty() {
        return productList == null || productList.isEmpty();
    }
}