package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.beans.BlogItem;
import com.bhasaka.ogani.core.models.beans.BlogPageItem;
import com.bhasaka.ogani.core.models.beans.CategoryItem;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import javax.annotation.PostConstruct;
import javax.jcr.Session;
import java.text.SimpleDateFormat;
import java.util.*;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BlogPagesModel {

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

    @PostConstruct
    protected void init() {
        loadCategories();
        loadBlogs();
        loadRecentBlogs();
    }

    // LOAD BLOGS
    private void loadBlogs() {

        Map<String, String> map = new HashMap<>();
        if (resourceResolver == null || blogParentPath == null) return;

        String search = request.getParameter("search");
        if (search != null && !search.isEmpty() && search.length() >= minSearchLength) {
            map.put("fulltext", search);
        }

        String category = request.getParameter("category");

        int limit = itemsPerPage > 0 ? itemsPerPage : 4;

        currentPage = 1;
        try {
            currentPage = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {}

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

        // SEARCH
        if (search != null && !search.isEmpty()) {
            map.put("fulltext", search);
        }

        // CATEGORY FILTER
        if (category != null && !category.isEmpty()) {
            map.put("property", "jcr:content/cq:tags");
            map.put("property.value", category);
        }

        Query query = queryBuilder.createQuery(
                com.day.cq.search.PredicateGroup.create(map),
                resourceResolver.adaptTo(Session.class)
        );

        SearchResult result = query.getResult();

        int totalMatches = (int) result.getTotalMatches();
        totalPages = (int) Math.ceil((double) totalMatches / limit);

        for (Iterator<Resource> it = result.getResources(); it.hasNext(); ) {
            Resource res = it.next();

            Resource content = res.getChild("jcr:content");
            if (content == null) continue;

            ValueMap vm = content.getValueMap();

            String title = vm.get("jcr:title", "");
            String description = vm.get("jcr:description", "");

            String image = "";
            Resource imageRes = findImageResource(content);
            if (imageRes != null) {
                image = imageRes.getValueMap().get("fileReference", "");
            }

            Date date = vm.get("jcr:created", Date.class);

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
        }
    }

    // LOAD CATEGORIES
    private void loadCategories() {

        if (categoryRootPath == null || blogParentPath == null || queryBuilder == null) return;

        Resource root = resourceResolver.getResource(categoryRootPath);
        if (root == null) return;

        Iterator<Resource> children = root.listChildren();

        // Step 1: Navigate to actual tags
        if (children.hasNext()) {
            Resource firstChild = children.next();
            if (firstChild.hasChildren()) {
                root = firstChild;
            }
        }

        Session session = resourceResolver.adaptTo(Session.class);

        // Step 2: Iterate tags
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
            map.put("p.limit", "0"); // only count, no results

            Query query = queryBuilder.createQuery(
                    com.day.cq.search.PredicateGroup.create(map),
                    session
            );

            SearchResult result = query.getResult();
            int count = (int) result.getTotalMatches();

            categories.put(tagId, new CategoryItem(tagId, title, count));
        }
    }

    // Recent Blogs
    private void loadRecentBlogs() {

        if (resourceResolver == null || blogParentPath == null || queryBuilder == null) {
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

            if (content == null) continue;

            ValueMap vm = content.getValueMap();

            // Title
            String title = vm.get("jcr:title", "");

            // Path
            String path = res.getPath() + ".html";

            // Image
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
        }
    }

    // IMAGE FINDER Method
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

    // PAGINATION
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

    // GETTERS
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