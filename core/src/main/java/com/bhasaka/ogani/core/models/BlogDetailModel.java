package com.bhasaka.ogani.core.models;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.jcr.Session;
import com.bhasaka.ogani.core.models.beans.BlogDetailItem;
import com.bhasaka.ogani.core.models.beans.CategoryItem;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import com.day.cq.search.*;
import com.day.cq.search.result.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model for Blog Detail Component.
 */
@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BlogDetailModel {

    private static final Logger LOG = LoggerFactory.getLogger(BlogDetailModel.class);

    @ValueMapValue
    private String blogParentPath;

    @ValueMapValue(name = "categoryRootPath")
    private String categoryRootPath;

    @ValueMapValue
    private String recentBlogPagesLimit;

    @ValueMapValue
    private String searchPlaceholder;

    @ValueMapValue
    private String blogDetailPath;

    private String title;
    private String description;
    private String subDescription;
    private String image;
    private String category;
    private String authorName;
    private String authorImage;

    private List<String> tags = new ArrayList<>();

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private QueryBuilder queryBuilder;

    private List<CategoryItem> categories = new ArrayList<>();
    private List<BlogDetailItem> recentBlogs = new ArrayList<>();

    /**
     * Initializes the model after dependency injection.
     * Loads page data, categories, and recent blogs.
     */
    @PostConstruct
    protected void init() {
        LOG.info("Initializing BlogDetailModel");

        try {
            loadPageData();
            loadCategories();
            loadRecentBlogs();
        } catch (Exception e) {
            LOG.error("Error initializing BlogDetailModel", e);
        }
    }

    /**
     * Loads blog detail data from the current page.
     * Includes title, description, image, author details, and tags.
     */
    private void loadPageData() {
        LOG.info("Loading blog page data for path: {}", blogDetailPath);

        Resource currentPage = resourceResolver.getResource(blogDetailPath);
        if(currentPage == null) {
            LOG.error("Current page resource is null for path: {}", blogDetailPath);
            return;
        }

        Resource content = currentPage.getChild("jcr:content");
        if (content == null) {
            LOG.error("jcr:content not found for path: {}", blogDetailPath);
            return;
        }

        ValueMap pageDataValueMap = content.getValueMap();

        title = pageDataValueMap.get("pageTitle", pageDataValueMap.get("jcr:title", ""));
        description = pageDataValueMap.get("jcr:description", "");
        subDescription = pageDataValueMap.get("subtitle", "");

        LOG.debug("Page title: {}", title);

        Resource imageRes = content.getChild("cq:featuredimage");
        if (imageRes != null) {
            ValueMap imgVm = imageRes.getValueMap();
            image = imgVm.get("fileReference", String.class);

            if (image == null || image.isEmpty()) {
                image = imgVm.get("fileName", String.class);
                LOG.warn("Using fallback image fileName for page: {}", blogDetailPath);
            }
        } else {
            LOG.warn("Featured image not found for page: {}", blogDetailPath);
        }

        authorName = pageDataValueMap.get("authorName", String.class);
        authorImage = pageDataValueMap.get("authorImage", String.class);

        String[] pageTags = pageDataValueMap.get("cq:tags", String[].class);

        if (pageTags != null) {
            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

            if (tagManager == null) {
                LOG.error("TagManager is null");
                return;
            }

            for (String tagId : pageTags) {
                Tag tag = tagManager.resolve(tagId);

                if (tag != null) {
                    tags.add(tag.getTitle());
                } else {
                    LOG.warn("Tag not resolved for tagId: {}", tagId);
                }
            }

            if (!tags.isEmpty()) {
                category = tags.get(0);
            }
        } else {
            LOG.info("No tags found for page: {}", blogDetailPath);
        }
    }

    /**
     * Loads category tags from configured root path.
     */
    private void loadCategories() {
        LOG.info("Loading categories from root path: {}", categoryRootPath);

        if (categoryRootPath == null) {
            LOG.warn("Category root path is null");
            return;
        }

        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        if (tagManager == null) {
            LOG.error("TagManager is null while loading categories");
            return;
        }

        Tag rootTag = tagManager.resolve(categoryRootPath);
        if (rootTag == null) {
            LOG.error("Root tag not found for path: {}", categoryRootPath);
            return;
        }

        Iterator<Tag> childTags = rootTag.listChildren();

        while (childTags.hasNext()) {
            Tag tag = childTags.next();
            categories.add(new CategoryItem(tag.getTagID(), tag.getTitle(), 0));
        }

        LOG.info("Loaded {} categories", categories.size());
    }

    /**
     * Fetches recent blog pages using QueryBuilder.
     * Results are sorted by creation date (latest first).
     */
    private void loadRecentBlogs() {
        LOG.info("Loading recent blogs from path: {}", blogParentPath);

        if (blogParentPath == null || queryBuilder == null) {
            LOG.error("Blog parent path or QueryBuilder is null");
            return;
        }

        try {
            Map<String, String> map = new HashMap<>();
            map.put("path", blogParentPath);
            map.put("type", "cq:Page");
            map.put("orderby", "@jcr:content/jcr:created");
            map.put("orderby.sort", "desc");
            map.put("p.limit", recentBlogPagesLimit != null ? recentBlogPagesLimit : "5");

            Query query = queryBuilder.createQuery(
                    PredicateGroup.create(map),
                    resourceResolver.adaptTo(Session.class)
            );

            SearchResult result = query.getResult();

            for (Iterator<Resource> it = result.getResources(); it.hasNext();) {

                Resource page = it.next();
                Resource content = page.getChild("jcr:content");

                if (content == null) {
                    LOG.warn("Skipping page without jcr:content: {}", page.getPath());
                    continue;
                }

                ValueMap recentBlogsValueMap = content.getValueMap();

                String title = recentBlogsValueMap.get("jcr:title", "");
                String path = page.getPath() + ".html";
                String image = recentBlogsValueMap.get("cq:featuredimage/fileReference", "");

                Date date = recentBlogsValueMap.get("jcr:created", Date.class);
                String formattedDate = "";

                if (date != null) {
                    formattedDate = new SimpleDateFormat("MMM dd, yyyy").format(date);
                }

                recentBlogs.add(new BlogDetailItem(title, path, image, formattedDate));
            }

            LOG.info("Loaded {} recent blogs", recentBlogs.size());

        } catch (Exception e) {
            LOG.error("Error while loading recent blogs", e);
        }
    }

    // Getters
    public List<CategoryItem> getCategories() { return categories; }
    public List<BlogDetailItem> getRecentBlogs() { return recentBlogs; }
    public String getSearchPlaceholder() { return searchPlaceholder; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSubDescription() { return subDescription; }
    public String getImage() { return image; }
    public String getCategory() { return category; }
    public List<String> getTags() { return tags; }
    public String getBlogParentPath() { return blogParentPath; }
    public String getCategoryRootPath() { return categoryRootPath; }
    public String getRecentBlogPagesLimit() { return recentBlogPagesLimit; }
    public String getBlogDetailPath() { return blogDetailPath; }
    public String getAuthorImage() { return authorImage; }
    public String getAuthorName() { return authorName; }
}