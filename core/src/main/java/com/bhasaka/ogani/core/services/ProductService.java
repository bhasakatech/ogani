package com.bhasaka.ogani.core.services;

import com.bhasaka.ogani.core.models.productCarousel.Product;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

/**
 * Service interface for fetching product data from the repository.
 *
 * This service defines the contract for retrieving a list of Product objects
 * from a given JCR path.
 *
 * Responsibilities:
 * - Fetch product resources from repository
 * - Convert resource data into Product objects
 * - Support reuse across multiple components like carousel and cart
 *
 * This service is typically injected into Sling Models using @OSGiService.
 */
public interface ProductService {

    /**
     * Retrieves a list of products from the given root path.
     *
     * @param rootPath the JCR path where product nodes exist
     * @param resolver the ResourceResolver used to access repository
     * @return list of Product objects, or empty list if no data found
     */
    List<Product> getProducts(String rootPath, ResourceResolver resolver);
}