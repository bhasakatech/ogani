package com.bhasaka.ogani.core.models.productCarousel;

import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductCarouselModel {

    @SlingObject
    private ResourceResolver resolver;

    @ValueMapValue
    private String productsPath;

    @ValueMapValue
    private boolean autoDiscount;

    @ValueMapValue
    private int defaultDiscount;

    private List<Product> products;

    @PostConstruct
    protected void init() {
        products = new ArrayList<>();

        if (productsPath == null) return;

        Resource folder = resolver.getResource(productsPath);

        if (folder != null) {
            for (Resource res : folder.getChildren()) {

                ValueMap vm = res.getValueMap();

                double original = vm.get("originalPrice", 0.0);
                double current = vm.get("currentPrice", 0.0);

                int discount = 0;

                if (autoDiscount && original > 0) {
                    discount = (int) (((original - current) / original) * 100);
                } else {
                    discount = defaultDiscount;
                }

                products.add(new Product(
                        vm.get("title", ""),
                        vm.get("category", ""),
                        vm.get("image", ""),
                        original,
                        current,
                        discount
                ));
            }
        }
    }

    public List<Product> getProducts() {
        return products;
    }
}