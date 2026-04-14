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

@Component(service = ProductService.class)
public class ProductServiceImpl implements ProductService{

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public List<Product> getProducts(String rootPath, ResourceResolver resolver) {

        List<Product> productList = new ArrayList<>();

        try{
            if(rootPath == null || rootPath.isEmpty()){
                log.warn("Root path is empty or null");
                return productList;
            }

            Resource rootResource = resolver.getResource(rootPath);

            if(rootResource == null){
                log.warn("No resource found at path: {}", rootPath);
                return productList;
            }

            for(Resource child : rootResource.getChildren()){
                try{
                    Resource dataResource = child.getChild("jcr:content/data/master");

                    if(dataResource == null){
                        log.debug("skipping child without data node: {}", child.getPath());
                        continue;
                    }

                    ValueMap valueMap = dataResource.getValueMap();
                    Product product = new Product();

                    product.setTitle(valueMap.get("title",""));
                    product.setCategory(valueMap.get("category",""));

                    String imageValue = valueMap.get("image", String.class);
                    product.setImage(resolveImage(imageValue, resolver));

                    double originalPrice = getDouble(valueMap, "originalPrice");
                    double currentPrice = getDouble(valueMap,"currentPrice");
                    double discount = getDouble(valueMap,"discount");

                    // Business logic
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

    private double getDouble(ValueMap vm, String key) {
        try {
            Number num = vm.get(key, Number.class);
            return num != null ? num.doubleValue() : 0.0;
        } catch (Exception e) {
            log.warn("Invalid number format for key: {}", key, e);
            return 0.0;
        }
    }

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
