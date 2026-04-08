package com.bhasaka.ogani.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import javax.annotation.PostConstruct;
import java.util.*;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CheckoutModel {

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutModel.class);
    private static final String cartPageChildData = "jcr:content/root/container/container/cart_page/products";

    @ValueMapValue
    private String fragmentFolder;

    @ValueMapValue
    private String firstName;

    @ValueMapValue
    private String lastName;

    @ValueMapValue
    private String country;

    @ValueMapValue
    private String address;

    @ValueMapValue
    private String apartment;

    @ValueMapValue
    private String townOrCity;

    @ValueMapValue
    private String stateOrCountry;

    @ValueMapValue
    private String zipCode;

    @ValueMapValue
    private String phone;

    @ValueMapValue
    private String email;

    @SlingObject
    private ResourceResolver resolver;

    @ValueMapValue
    private String cartPath;

    @ValueMapValue
    private String orderTitle;

    @ValueMapValue
    private String subtotalLabel;

    @ValueMapValue
    private String finalTotalLabel;

    @ValueMapValue
    private String placeOrderLabel;

    @ValueMapValue
    private String accountPassword;

    @ValueMapValue
    private String orderNotes;

    private List<ProductCart> products = new ArrayList<>();
    private double subtotal = 0;

    @PostConstruct
    protected void init() {

        if (cartPath == null || cartPath.isEmpty()) {
            LOG.info("Cart path missing");
            return;
        }

        Resource cartPage = resolver.getResource(cartPath);

        if (cartPage == null) {
            LOG.info("Cart page not found");
            return;
        }

        Resource productsNode = cartPage.getChild(cartPageChildData);

        if (productsNode == null) {
            LOG.info("Products node NOT FOUND");
            return;
        }
        LOG.info("Products node FOUND");
        for (Resource item : productsNode.getChildren()) {

            LOG.info("Item Node: {}", item.getPath());

            ValueMap vm = item.getValueMap();
            LOG.info("Properties: {}", vm.keySet());

            String cfPath = vm.get("cfPath", String.class);
            LOG.info("cfPath value: {}", cfPath);

            if (cfPath == null || cfPath.isEmpty()) {
                LOG.error("cfPath is NULL or EMPTY");
                continue;
            }

            Resource data = resolver.getResource(cfPath + "/jcr:content/data/master");

            if (data == null) {
                LOG.error("CF DATA NOT FOUND for: {}", cfPath);
                continue;
            }

            ValueMap cfVm = data.getValueMap();

            String title = cfVm.get("title", "");
            double price = cfVm.get("currentPrice", 0.0);

            LOG.info("Product Loaded: {}",  title + " | " + price);

            ProductCart p = new ProductCart();
            p.setTitle(title);
            p.setCurrentPrice(price);
            p.setImage(cfVm.get("image", ""));

            products.add(p);
            subtotal += price;
        }
        LOG.info("TOTAL PRODUCTS: {}", products.size());
    }

    // Resolve Image
    private String resolveImage(String value) {

        if (value == null || value.isEmpty()) return "";

        if (value.startsWith("/content/dam")) {
            return value;
        }

        Resource resolved = resolver.resolve(value);
        return resolved != null ? resolved.getPath() : value;
    }

    private Resource findCartComponent(Resource resource) {

        if (resource == null) return null;

        if ("Ogani/components/cart-page".equals(resource.getResourceType())) {
            return resource;
        }

        for (Resource child : resource.getChildren()) {
            Resource found = findCartComponent(child);
            if (found != null) return found;
        }

        return null;
    }

    public List<ProductCart> getProducts() { return products; }
    public double getSubtotal() { return subtotal; }
    public double getTotal() { return subtotal; }
    public boolean isEmpty() { return products.isEmpty(); }

    public String getOrderTitle() { return orderTitle != null ? orderTitle : "Your Order"; }
    public String getSubtotalLabel() { return subtotalLabel != null ? subtotalLabel : "Subtotal"; }
    public String getFinalTotalLabel() { return finalTotalLabel != null ? finalTotalLabel : "Total"; }
    public String getPlaceOrderLabel() { return placeOrderLabel != null ? placeOrderLabel : "PLACE ORDER"; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getApartment() {
        return apartment;
    }

    public String getTownOrCity() {
        return townOrCity;
    }

    public String getStateOrCountry() {
        return stateOrCountry;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCartPath() {
        return cartPath;
    }

    public String getFragmentFolder() {
        return fragmentFolder;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public String getAccountPassword() {
        return accountPassword;
    }
}