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

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FeaturedProductsModel {

    private static final Logger LOG = LoggerFactory.getLogger(FeaturedProductsModel.class);
    private static final String CF_PARENT_PATH = "/content/dam/Ogani/content-fragments/featured";

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

    @PostConstruct
    protected void init() {
        loadProducts();
    }

    private void loadProducts() {

        if (resourceResolver == null || queryBuilder == null) return;

        int limit = (numberOfProducts != null && numberOfProducts > 0) ? numberOfProducts : 8;

        Map<String, String> predicate = new HashMap<>();
        predicate.put("path", CF_PARENT_PATH);
        predicate.put("type", "dam:Asset");
        predicate.put("property", "jcr:content/contentFragment");
        predicate.put("property.value", "true");
        predicate.put("p.limit", String.valueOf(limit));

        Session session = resourceResolver.adaptTo(Session.class);
        if (session == null) return;

        Query query = queryBuilder.createQuery(PredicateGroup.create(predicate), session);
        SearchResult result = query.getResult();

        for (Hit hit : result.getHits()) {
            try {
                Resource contentFragment = resourceResolver.getResource(hit.getPath());
                if (contentFragment == null) continue;

                Resource metadataResource  = contentFragment.getChild("jcr:content/data/master");
                if (metadataResource == null) continue;

                ProductCFModel productCFModel = metadataResource.adaptTo(ProductCFModel.class);
                if (productCFModel == null) continue;

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
                }

            } catch (RepositoryException e) {
                LOG.error("Repository Exception while fetching CF", e);
            }
        }
    }

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
        return list;
    }

    private String extractKey(String tag) {
        return tag.substring(tag.lastIndexOf("/") + 1);
    }

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

    public String getSectionTitle() {
        return sectionTitle != null ? sectionTitle : "Featured Products";
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isEnableHoverActions() {
        return enableHoverActions != null && enableHoverActions;
    }
}