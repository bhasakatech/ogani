package com.bhasaka.ogani.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import javax.annotation.PostConstruct;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model class for Checkout component.
 * <p>
 * Handles cart product retrieval, subtotal calculation,
 * and exposes checkout form fields and placeholders.
 */
@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CheckoutModel {

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutModel.class);

    /** Path to cart products inside cart page */
    private static final String cartPageChildData = "jcr:content/root/container/container/cart_page/products";

    @ValueMapValue
    private String checkoutTitle;

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

    // Placeholder fields
    @ValueMapValue private String firstNamePlaceholder;
    @ValueMapValue private String lastNamePlaceholder;
    @ValueMapValue private String countryPlaceholder;
    @ValueMapValue private String addressPlaceholder;
    @ValueMapValue private String apartmentPlaceholder;
    @ValueMapValue private String townOrCityPlaceholder;
    @ValueMapValue private String stateOrCountryPlaceholder;
    @ValueMapValue private String zipCodePlaceholder;
    @ValueMapValue private String phonePlaceholder;
    @ValueMapValue private String emailPlaceholder;
    @ValueMapValue private String accountPasswordPlaceholder;
    @ValueMapValue private String orderNotesPlaceholder;

    @ValueMapValue
    private String accountInformation;

    @ValueMapValue
    private String paymentDescription;

    /** List of cart products */
    private List<ProductCart> products = new ArrayList<>();

    /** Total subtotal value */
    private double subtotal = 0;

    /**
     * Initializes checkout model and loads cart products.
     */
    @PostConstruct
    protected void init() {

        LOG.info("Initializing CheckoutModel");

        if (cartPath == null || cartPath.isEmpty()) {
            LOG.error("Cart path is missing");
            return;
        }

        Resource cartPage = resolver.getResource(cartPath);

        if (cartPage == null) {
            LOG.error("Cart page not found at path: {}", cartPath);
            return;
        }

        Resource productsNode = cartPage.getChild(cartPageChildData);

        if (productsNode == null) {
            LOG.error("Products node NOT FOUND at: {}", cartPageChildData);
            return;
        }

        LOG.info("Products node FOUND");

        for (Resource item : productsNode.getChildren()) {

            LOG.info("Processing item node: {}", item.getPath());

            ValueMap vm = item.getValueMap();
            String cfPath = vm.get("cfPath", String.class);

            LOG.info("cfPath value: {}", cfPath);

            if (cfPath == null || cfPath.isEmpty()) {
                LOG.error("cfPath is NULL or EMPTY for item: {}", item.getPath());
                continue;
            }

            Resource data = resolver.getResource(cfPath + "/jcr:content/data/master");

            if (data == null) {
                LOG.error("Content Fragment data NOT FOUND for: {}", cfPath);
                continue;
            }

            ValueMap cfVm = data.getValueMap();

            String title = cfVm.get("title", "");
            double price = cfVm.get("currentPrice", 0.0);

            LOG.info("Product Loaded: {} | {}", title, price);

            ProductCart p = new ProductCart();
            p.setTitle(title);
            p.setCurrentPrice(price);
            p.setImage(cfVm.get("image", ""));

            products.add(p);
            subtotal += price;
        }

        LOG.info("Total products loaded: {}", products.size());
        LOG.info("Subtotal calculated: {}", subtotal);
    }

    /**
     * Resolves image path.
     *
     * @param value image reference
     * @return resolved path
     */
    private String resolveImage(String value) {

        if (value == null || value.isEmpty()) return "";

        if (value.startsWith("/content/dam")) {
            return value;
        }

        Resource resolved = resolver.resolve(value);
        return resolved != null ? resolved.getPath() : value;
    }

    /**
     * Recursively finds cart component resource.
     *
     * @param resource root resource
     * @return cart component resource if found
     */
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

    /** Getters */

    public List<ProductCart> getProducts() { return products; }

    public double getSubtotal() { return subtotal; }

    public double getTotal() { return subtotal; }

    public boolean isEmpty() { return products.isEmpty(); }

    public String getOrderTitle() { return orderTitle != null ? orderTitle : "Your Order"; }

    public String getSubtotalLabel() { return subtotalLabel != null ? subtotalLabel : "Subtotal"; }

    public String getFinalTotalLabel() { return finalTotalLabel != null ? finalTotalLabel : "Total"; }

    public String getPlaceOrderLabel() { return placeOrderLabel != null ? placeOrderLabel : "PLACE ORDER"; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getCountry() { return country; }

    public String getAddress() { return address; }

    public String getApartment() { return apartment; }

    public String getTownOrCity() { return townOrCity; }

    public String getStateOrCountry() { return stateOrCountry; }

    public String getZipCode() { return zipCode; }

    public String getPhone() { return phone; }

    public String getEmail() { return email; }

    public String getCartPath() { return cartPath; }

    public String getFragmentFolder() { return fragmentFolder; }

    public String getOrderNotes() { return orderNotes; }

    public String getAccountPassword() { return accountPassword; }

    public String getCheckoutTitle() { return checkoutTitle; }

    public String getFirstNamePlaceholder() { return firstNamePlaceholder; }

    public String getLastNamePlaceholder() { return lastNamePlaceholder; }

    public String getCountryPlaceholder() { return countryPlaceholder; }

    public String getAddressPlaceholder() { return addressPlaceholder; }

    public String getApartmentPlaceholder() { return apartmentPlaceholder; }

    public String getTownOrCityPlaceholder() { return townOrCityPlaceholder; }

    public String getStateOrCountryPlaceholder() { return stateOrCountryPlaceholder; }

    public String getZipCodePlaceholder() { return zipCodePlaceholder; }

    public String getPhonePlaceholder() { return phonePlaceholder; }

    public String getEmailPlaceholder() { return emailPlaceholder; }

    public String getAccountPasswordPlaceholder() { return accountPasswordPlaceholder; }

    public String getOrderNotesPlaceholder() { return orderNotesPlaceholder; }

    public String getAccountInformation() { return accountInformation; }

    public String getPaymentDescription() { return paymentDescription; }
}