package com.bhasaka.ogani.core.services.Impl;

import com.bhasaka.ogani.core.models.productCarousel.Product;
import com.bhasaka.ogani.core.services.ProductService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ProductService for retrieving product data from JCR.
 *
 * This class is registered as an OSGi component and can be injected into
 * Sling Models using @OSGiService.
 *
 * Responsibilities:
 * - Read product data from repository
 * - Navigate JCR structure (jcr:content/data/master)
 * - Map properties to Product objects
 * - Apply pricing and discount logic
 * - Resolve image paths
 *
 * Error Handling:
 * - Logs warnings for invalid input or missing resources
 * - Logs errors for exceptions
 * - Returns empty list in failure scenarios
 */
@Component(service = ProductService.class)
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    /**
     * Fetches product data from the given root path.
     *
     * Execution flow:
     * - Validate rootPath
     * - Fetch root resource
     * - Iterate child resources
     * - Read product data from nested node
     * - Apply business logic for pricing
     * - Build Product objects
     *
     * @param rootPath the JCR path containing product nodes
     * @param resolver the ResourceResolver used to access repository
     * @return list of Product objects
     */
    @Override
    public List<Product> getProducts(String rootPath, ResourceResolver resolver) {

        List<Product> productList = new ArrayList<>();

        try {
            if (rootPath == null || rootPath.isEmpty()) {
                log.warn("Root path is empty or null");
                return productList;
            }

            Resource rootResource = resolver.getResource(rootPath);

            if (rootResource == null) {
                log.warn("No resource found at path: {}", rootPath);
                return productList;
            }

            for (Resource child : rootResource.getChildren()) {
                try {
                    Resource dataResource = child.getChild("jcr:content/data/master");

                    if (dataResource == null) {
                        log.debug("Skipping child without data node: {}", child.getPath());
                        continue;
                    }

                    ValueMap valueMap = dataResource.getValueMap();
                    Product product = new Product();

                    product.setTitle(valueMap.get("title", ""));
                    product.setCategory(valueMap.get("category", ""));

                    String imageValue = valueMap.get("image", String.class);
                    product.setImage(resolveImage(imageValue, resolver));

                    double originalPrice = getDouble(valueMap, "originalPrice");
                    double currentPrice = getDouble(valueMap, "currentPrice");
                    double discount = getDouble(valueMap, "discount");

                    // Pricing logic
                    if (discount > 0 && originalPrice > 0) {
                        currentPrice = originalPrice - (originalPrice * discount / 100);
                    } else if (originalPrice > 0 && currentPrice > 0) {
                        discount = ((originalPrice - currentPrice) / originalPrice) * 100;
                    }

                    product.setOriginalPrice(originalPrice);
                    product.setCurrentPrice(currentPrice);
                    product.setDiscount(Math.round(discount));

                    productList.add(product);

                } catch (Exception e) {
                    log.error("Error processing child resource: {}", child.getPath(), e);
                }
            }

        } catch (Exception e) {
            log.error("Error fetching product list for rootPath: {}", rootPath, e);
        }

        return productList;
    }

    /**
     * Safely retrieves a numeric value from ValueMap.
     *
     * If the value is null or invalid, returns 0.0.
     *
     * @param vm  the ValueMap containing properties
     * @param key the property key
     * @return numeric value or 0.0
     */
    private double getDouble(ValueMap vm, String key) {
        try {
            Number num = vm.get(key, Number.class);
            return num != null ? num.doubleValue() : 0.0;
        } catch (Exception e) {
            log.warn("Invalid number format for key: {}", key, e);
            return 0.0;
        }
    }

    /**
     * Resolves the image path.
     *
     * Logic:
     * - If null or empty, return empty string
     * - If DAM path, return as is
     * - Otherwise resolve using ResourceResolver
     *
     * @param value image path
     * @param resolver ResourceResolver
     * @return resolved image path or empty string
     */
    private String resolveImage(String value, ResourceResolver resolver) {

        try {
            if (value == null || value.isEmpty()) {
                return "";
            }

            if (value.startsWith("/content/dam")) {
                return value;
            }

            Resource resolved = resolver.resolve(value);
            return resolved != null ? resolved.getPath() : value;

        } catch (Exception e) {
            log.error("Error resolving image path: {}", value, e);
            return "";
        }
    }
}