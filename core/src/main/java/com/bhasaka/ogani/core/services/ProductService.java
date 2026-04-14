package com.bhasaka.ogani.core.services;

import com.bhasaka.ogani.core.models.productCarousel.Product;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface ProductService {
    List<Product> getProducts(String rootPath, ResourceResolver resolver);
}