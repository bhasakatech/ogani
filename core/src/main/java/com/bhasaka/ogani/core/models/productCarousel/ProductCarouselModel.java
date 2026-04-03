package com.bhasaka.ogani.core.models.productCarousel;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCarouselModel {

    @ValueMapValue
    private String rootPath;

    @SlingObject
    private ResourceResolver resolver;

    private List<Product> productList = new ArrayList<>();

    @PostConstruct
    protected void init() {

        if (rootPath == null || rootPath.isEmpty()) {
            return;
        }

        Resource rootResource = resolver.getResource(rootPath);

        if (rootResource == null) {
            return;
        }

        for (Resource child : rootResource.getChildren()) {

            try {
                Resource dataResource = child.getChild("jcr:content/data/master");
                if (dataResource == null) {
                    continue;
                }

                ValueMap vm = dataResource.getValueMap();

                Product product = new Product();

                product.setTitle(vm.get("title", ""));
                product.setCategory(vm.get("category", ""));

                String imageValue = vm.get("image", String.class);
                product.setImage(resolveImage(imageValue));

                Number originalPriceNum = vm.get("originalPrice", Number.class);
                Number currentPriceNum = vm.get("currentPrice", Number.class);
                Number discountNum = vm.get("discount", Number.class);

                double originalPrice = originalPriceNum != null ? originalPriceNum.doubleValue() : 0.0;
                double currentPrice = currentPriceNum != null ? currentPriceNum.doubleValue() : 0.0;
                double discount = discountNum != null ? discountNum.doubleValue() : 0.0;

                if (discount > 0 && originalPrice > 0) {
                    currentPrice = originalPrice - (originalPrice * discount / 100);
                } else if (originalPrice > 0 && currentPrice > 0) {
                    discount = ((originalPrice - currentPrice) / originalPrice) * 100;
                }

                product.setOriginalPrice(originalPrice);
                product.setCurrentPrice(currentPrice);
                product.setDiscount((int) Math.round(discount));

                productList.add(product);

            } catch (Exception ignored) {
            }
        }
    }

    private String resolveImage(String value) {

        if (value == null || value.isEmpty()) {
            return "";
        }

        if (value.startsWith("/content/dam")) {
            return value;
        }

        Resource resolved = resolver.resolve(value);
        return resolved != null ? resolved.getPath() : value;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public boolean isEmpty() {
        return productList == null || productList.isEmpty();
    }
}