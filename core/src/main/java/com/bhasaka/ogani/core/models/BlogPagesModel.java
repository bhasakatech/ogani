package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.beans.BlogPageItem;
import com.bhasaka.ogani.core.models.beans.CategoryItem;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.jcr.Session;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Model class for Blog Pages component.
 * <p>
 * Handles blog listing with pagination, category filtering,
 * search functionality, and recent blogs.
 */
@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BlogPagesModel {

    private static final Logger LOG = LoggerFactory.getLogger(BlogPagesModel.class);

    @ValueMapValue
    private String blogParentPath;

    @ValueMapValue
    private int itemsPerPage;

    @ValueMapValue(name = "categoryRootPath")
    private String categoryRootPath;

    @ValueMapValue
    private String recentBlogPagesLimit;

    @ValueMapValue
    private String searchPlaceholder;

    @ValueMapValue
    private int minSearchLength;

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private QueryBuilder queryBuilder;

    private int currentPage;
    private int totalPages;
    private List<BlogPageItem> paginatedBlogs = new ArrayList<>();
    private Map<String, CategoryItem> categories = new LinkedHashMap<>();
    private List<BlogPageItem> recentBlogs = new ArrayList<>();

    /**
     * Initializes the model and loads all required data.
     */
    @PostConstruct
    protected void init() {
        LOG.info("Initializing BlogPagesModel");
        loadCategories();
        loadBlogs();
        loadRecentBlogs();
    }

    /**
     * Loads blogs with pagination, search, and category filters.
     */
    private void loadBlogs() {

        Map<String, String> map = new HashMap<>();

        if (resourceResolver == null || blogParentPath == null) {
            LOG.error("ResourceResolver or blogParentPath is null");
            return;
        }

        String search = request.getParameter("search");
        if (search != null && !search.isEmpty() && search.length() >= minSearchLength) {
            map.put("fulltext", search);
            LOG.info("Search applied: {}", search);
        }

        String category = request.getParameter("category");

        int limit = itemsPerPage > 0 ? itemsPerPage : 4;

        currentPage = 1;
        try {
            currentPage = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            LOG.info("Invalid page parameter, defaulting to page 1");
        }

        int offset = (currentPage - 1) * limit;

        map.put("path", blogParentPath);
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/cq:tags");
        map.put("property.value", category);
        map.put("property.operation", "like");
        map.put("p.limit", String.valueOf(limit));
        map.put("p.offset", String.valueOf(offset));
        map.put("orderby", "@jcr:content/jcr:created");
        map.put("orderby.sort", "desc");

        if (search != null && !search.isEmpty()) {
            map.put("fulltext", search);
        }

        if (category != null && !category.isEmpty()) {
            map.put("property", "jcr:content/cq:tags");
            map.put("property.value", category);
            LOG.info("Category filter applied: {}", category);
        }

        Query query = queryBuilder.createQuery(
                com.day.cq.search.PredicateGroup.create(map),
                resourceResolver.adaptTo(Session.class)
        );

        SearchResult result = query.getResult();

        int totalMatches = (int) result.getTotalMatches();
        totalPages = (int) Math.ceil((double) totalMatches / limit);

        LOG.info("Total blog matches: {}, Total pages: {}", totalMatches, totalPages);

        for (Iterator<Resource> iterator = result.getResources(); iterator.hasNext(); ) {
            Resource res = iterator.next();

            Resource content = res.getChild("jcr:content");
            if (content == null) {
                LOG.error("Content resource missing for: {}", res.getPath());
                continue;
            }

            ValueMap valueMap = content.getValueMap();

            String title = valueMap.get("jcr:title", "");
            String description = valueMap.get("jcr:description", "");

            String image = "";
            Resource imageRes = findImageResource(content);
            if (imageRes != null) {
                image = imageRes.getValueMap().get("fileReference", "");
            } else {
                LOG.error("Image not found for: {}", res.getPath());
            }

            Date date = valueMap.get("jcr:created", Date.class);

            String formattedDate = "";
            if (date != null) {
                formattedDate = new SimpleDateFormat("MMM dd, yyyy").format(date);
            }

            paginatedBlogs.add(new BlogPageItem(
                    title,
                    description,
                    image,
                    res.getPath() + ".html",
                    formattedDate,
                    date
            ));

            LOG.info("Blog page added: {}", title);
        }
    }

    /**
     * Loads categories with blog count for each category.
     */
    private void loadCategories() {

        if (categoryRootPath == null || blogParentPath == null || queryBuilder == null) {
            LOG.error("Category configuration is missing");
            return;
        }

        Resource root = resourceResolver.getResource(categoryRootPath);
        if (root == null) {
            LOG.error("Category root resource not found: {}", categoryRootPath);
            return;
        }

        Session session = resourceResolver.adaptTo(Session.class);

        for (Resource child : root.getChildren()) {

            ValueMap vm = child.getValueMap();
            String title = vm.get("jcr:title", child.getName());

            String tagId = child.getPath()
                    .replace("/content/cq:tags/", "")
                    .replaceFirst("/", ":");

            Map<String, String> map = new HashMap<>();
            map.put("path", blogParentPath);
            map.put("type", "cq:Page");
            map.put("property", "jcr:content/cq:tags");
            map.put("property.value", tagId);
            map.put("p.limit", "0");

            Query query = queryBuilder.createQuery(
                    com.day.cq.search.PredicateGroup.create(map),
                    session
            );

            SearchResult result = query.getResult();
            int count = (int) result.getTotalMatches();

            categories.put(tagId, new CategoryItem(tagId, title, count));

            LOG.info("Category added: {} with count {}", title, count);
        }
    }

    /**
     * Loads recent blogs based on created date.
     */
    private void loadRecentBlogs() {

        if (resourceResolver == null || blogParentPath == null || queryBuilder == null) {
            LOG.error("Cannot load recent blogs due to missing dependencies");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("path", blogParentPath);
        map.put("type", "cq:Page");
        map.put("p.limit", recentBlogPagesLimit);
        map.put("orderby", "@jcr:content/jcr:created");
        map.put("orderby.sort", "desc");

        Query query = queryBuilder.createQuery(
                com.day.cq.search.PredicateGroup.create(map),
                resourceResolver.adaptTo(Session.class)
        );

        SearchResult result = query.getResult();

        for (Iterator<Resource> it = result.getResources(); it.hasNext();) {

            Resource res = it.next();
            Resource content = res.getChild("jcr:content");

            if (content == null) {
                LOG.error("Content missing for recent blog: {}", res.getPath());
                continue;
            }

            ValueMap vm = content.getValueMap();

            String title = vm.get("jcr:title", "");
            String path = res.getPath() + ".html";

            String image = vm.get("cq:featuredimage/fileReference", String.class);

            if (image == null || image.isEmpty()) {
                Resource featured = content.getChild("cq:featuredimage");
                if (featured != null) {
                    image = featured.getValueMap().get("fileReference", "");
                }
            }

            Date date = vm.get("jcr:created", Date.class);

            String formattedDate = "";
            if (date != null) {
                formattedDate = new SimpleDateFormat("MMM dd, yyyy").format(date);
            }

            recentBlogs.add(new BlogPageItem(
                    title,
                    "",
                    image,
                    path,
                    formattedDate,
                    date
            ));

            LOG.info("Recent blog added: {}", title);
        }
    }

    /**
     * Recursively finds featured image resource.
     */
    private Resource findImageResource(Resource resource) {
        if (resource == null) return null;

        if ("cq:featuredimage".equals(resource.getName())) {
            return resource;
        }

        for (Resource child : resource.getChildren()) {
            Resource found = findImageResource(child);
            if (found != null) return found;
        }
        return null;
    }

    /** Pagination helpers */
    public int getPrevPage() {
        return currentPage > 1 ? currentPage - 1 : 1;
    }

    public int getNextPage() {
        return currentPage < totalPages ? currentPage + 1 : totalPages;
    }

    public List<Integer> getPageNumbers() {
        List<Integer> pages = new ArrayList<>();

        int start = Math.max(1, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 2);

        for (int i = start; i <= end; i++) {
            pages.add(i);
        }
        return pages;
    }

    /** Getters */
    public List<BlogPageItem> getPaginatedBlogs() {
        return paginatedBlogs;
    }

    public Collection<CategoryItem> getCategories() {
        return categories.values();
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getBlogParentPath() {
        return blogParentPath;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public String getCategoryRootPath() {
        return categoryRootPath;
    }

    public List<BlogPageItem> getRecentBlogs() {
        return recentBlogs;
    }

    public String getRecentBlogPagesLimit() {
        return recentBlogPagesLimit;
    }

    public String getSearchPlaceholder() {
        return searchPlaceholder;
    }

    public int getMinSearchLength() {
        return minSearchLength;
    }
}