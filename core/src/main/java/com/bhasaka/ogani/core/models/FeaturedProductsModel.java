package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.beans.Category;
import com.bhasaka.ogani.core.models.featuredproducts.CategoryItemModel;
import com.bhasaka.ogani.core.models.featuredproducts.Product;
import com.bhasaka.ogani.core.models.featuredproducts.ProductCFModel;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class for fetching and preparing Featured Products data.
 * <p>
 * This class retrieves Content Fragment data from DAM based on a predefined path,
 * processes it, and exposes product and category information to the frontend.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FeaturedProductsModel {

    private static final Logger LOG = LoggerFactory.getLogger(FeaturedProductsModel.class);

    /** Path where featured product content fragments are stored */
    private static final String CF_PARENT_PATH = "/content/dam/Ogani/content-fragments/featured";

    /** List to store processed product data */
    private final List<Product> products = new ArrayList<>();

    @ValueMapValue
    private String sectionTitle;

    @ValueMapValue
    private Integer numberOfProducts;

    @ValueMapValue
    private Boolean enableHoverActions;

    @SlingObject
    private ResourceResolver resourceResolver;

    @OSGiService
    private QueryBuilder queryBuilder;

    @ChildResource(name = "categoryTags")
    private List<CategoryItemModel> categoryItems;

    /**
     * Initializes the model after injection.
     */
    @PostConstruct
    protected void init() {
        LOG.info("Initializing FeaturedProductsModel");
        loadProducts();
    }

    /**
     * Loads products from Content Fragments using QueryBuilder.
     */
    private void loadProducts() {

        if (resourceResolver == null || queryBuilder == null) {
            LOG.error("ResourceResolver or QueryBuilder is null");
            return;
        }

        int limit = (numberOfProducts != null && numberOfProducts > 0) ? numberOfProducts : 8;
        LOG.info("Fetching products with limit: {}", limit);

        Map<String, String> predicate = new HashMap<>();
        predicate.put("path", CF_PARENT_PATH);
        predicate.put("type", "dam:Asset");
        predicate.put("property", "jcr:content/contentFragment");
        predicate.put("property.value", "true");
        predicate.put("p.limit", String.valueOf(limit));

        Session session = resourceResolver.adaptTo(Session.class);
        if (session == null) {
            LOG.error("Session is null, cannot execute query");
            return;
        }

        Query query = queryBuilder.createQuery(PredicateGroup.create(predicate), session);
        SearchResult result = query.getResult();

        LOG.info("Total hits found: {}", result.getHits().size());

        for (Hit hit : result.getHits()) {
            try {
                Resource contentFragment = resourceResolver.getResource(hit.getPath());
                if (contentFragment == null) {
                    LOG.error("Content Fragment resource is null for path: {}", hit.getPath());
                    continue;
                }

                Resource metadataResource = contentFragment.getChild("jcr:content/data/master");
                if (metadataResource == null) {
                    LOG.error("Metadata resource not found for: {}", hit.getPath());
                    continue;
                }

                ProductCFModel productCFModel = metadataResource.adaptTo(ProductCFModel.class);
                if (productCFModel == null) {
                    LOG.error("Failed to adapt resource to ProductCFModel for: {}", hit.getPath());
                    continue;
                }

                List<String> cleanCategories = new ArrayList<>();
                if (productCFModel.getCategory() != null) {
                    for (String cat : productCFModel.getCategory()) {
                        cleanCategories.add(extractKey(cat));
                    }
                }

                if (productCFModel.getTitle() != null && productCFModel.getPrice() != null) {
                    products.add(new Product(
                            productCFModel.getTitle(),
                            productCFModel.getPrice(),
                            productCFModel.getImage(),
                            cleanCategories
                    ));
                    LOG.info("Product added: {}", productCFModel.getTitle());
                } else {
                    LOG.error("Product skipped due to missing title or price");
                }

            } catch (RepositoryException e) {
                LOG.error("Repository Exception while fetching CF", e);
            }
        }
    }

    /**
     * Returns list of categories derived from category tags.
     *
     * @return list of Category objects
     */
    public List<Category> getCategories() {
        List<Category> list = new ArrayList<>();

        if (categoryItems != null) {
            for (CategoryItemModel item : categoryItems) {

                String tag = item.getCategoryTag();
                if (tag != null) {
                    String key = extractKey(tag);
                    String name = toTitleCase(key.replace("-", " "));
                    list.add(new Category(key, name));
                }
            }
        }
        LOG.info("Total categories processed: {}", list.size());
        return list;
    }

    /**
     * Extracts key from tag path.
     *
     * @param tag full tag path
     * @return extracted key
     */
    private String extractKey(String tag) {
        return tag.substring(tag.lastIndexOf("/") + 1);
    }

    /**
     * Converts string to Title Case format.
     *
     * @param str input string
     * @return formatted string
     */
    private String toTitleCase(String str) {
        if (str == null || str.isEmpty()) return str;

        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    /**
     * Returns section title.
     *
     * @return section title or default value
     */
    public String getSectionTitle() {
        return sectionTitle != null ? sectionTitle : "Featured Products";
    }

    /**
     * Returns list of products.
     *
     * @return list of Product objects
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Indicates whether hover actions are enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnableHoverActions() {
        return enableHoverActions != null && enableHoverActions;
    }
}