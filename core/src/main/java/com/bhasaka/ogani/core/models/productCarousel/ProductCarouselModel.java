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

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCarouselModel {

    private static final Logger log = LoggerFactory.getLogger(ProductCarouselModel.class);

    @ValueMapValue
    private String rootPath;

    @SlingObject
    private ResourceResolver resolver;

    @OSGiService
    private ProductService productService;

    private List<Product> productList;

    @PostConstruct
    protected void init() {
        try {
            productList = productService.getProducts(rootPath, resolver);
        } catch (Exception e) {
            log.error("Error initializing ProductCarouselModel", e);
        }
    }

    public List<Product> getProductList() {
        return productList;
    }

    public boolean isEmpty() {
        return productList == null || productList.isEmpty();
    }
}