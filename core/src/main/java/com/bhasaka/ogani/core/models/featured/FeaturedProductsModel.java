package com.bhasaka.ogani.core.models.featured;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import javax.annotation.PostConstruct;
import javax.jcr.Session;
import java.util.*;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FeaturedProductsModel {

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
    private List<Resource> categoryItems;

    private final List<Product> products = new ArrayList<>();

    private static final String CF_PARENT_PATH = "/content/dam/Ogani/content-fragments/featured";

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
                Resource cf = resourceResolver.getResource(hit.getPath());
                if (cf == null) continue;

                Resource master = cf.getChild("jcr:content/data/master");
                if (master == null) continue;

                ValueMap vm = master.getValueMap();

                String title = vm.get("title", String.class);
                String price = vm.get("price", String.class);
                String image = vm.get("image", String.class);
                String[] categories = vm.get("category", String[].class);

                // Convert categories to clean values
                List<String> cleanCategories = new ArrayList<>();
                if (categories != null) {
                    for (String cat : categories) {
                        cleanCategories.add(extractKey(cat));
                    }
                }

                if (title != null && price != null) {
                    products.add(new Product(title, price, image, cleanCategories));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    // Categories for tabs
    public List<Category> getCategories() {
        List<Category> list = new ArrayList<>();

        if (categoryItems != null) {
            for (Resource item : categoryItems) {
                String tag = item.getValueMap().get("categoryTag", String.class);

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

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Getters
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