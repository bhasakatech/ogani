package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.beans.BlogItem;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Model class for Blog Listing component.
 * <p>
 * This class fetches child pages from a configured parent path
 * and prepares blog data such as title, description, image, and date.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BlogListingModel {

    private static final Logger LOG = LoggerFactory.getLogger(BlogListingModel.class);

    @ValueMapValue
    private String blogTitle;

    @ValueMapValue
    private String blogParentPath;

    @SlingObject
    private ResourceResolver resourceResolver;

    /** List to store blog items */
    private List<BlogItem> blogs = new ArrayList<>();

    /**
     * Initializes the model and loads blog data.
     */
    @PostConstruct
    protected void init() {

        LOG.info("Initializing BlogListingModel");

        if (resourceResolver == null || blogParentPath == null) {
            LOG.error("ResourceResolver or blogParentPath is null");
            return;
        }

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            LOG.error("PageManager is null");
            return;
        }

        Page parentPage = pageManager.getPage(blogParentPath);

        if (parentPage == null) {
            LOG.error("Parent page not found for path: {}", blogParentPath);
            return;
        }

        Iterator<Page> childPages = parentPage.listChildren();
        int count = 0;

        while (childPages.hasNext() && count < 3) {

            Page child = childPages.next();
            Resource contentResource = child.getContentResource();

            if (contentResource == null) {
                LOG.error("Content resource is null for page: {}", child.getPath());
                continue;
            }

            String title = child.getTitle();
            String description = contentResource.getValueMap().get("jcr:description", "");

            // Image
            Resource imageResource = findImageResource(contentResource);
            String image = "";
            if (imageResource != null) {
                image = imageResource.getValueMap().get("fileReference", "");
                LOG.info("Image path for {} : {}", child.getPath(), image);
            } else {
                LOG.error("Image resource not found for page: {}", child.getPath());
            }

            Date date = contentResource.getValueMap().get("jcr:created", Date.class);

            String formattedDate = "";
            if (date != null) {
                formattedDate = new java.text.SimpleDateFormat("MMM dd, yyyy").format(date);
            } else {
                LOG.error("Created date is null for page: {}", child.getPath());
            }

            blogs.add(new BlogItem(
                    title,
                    description,
                    image,
                    child.getPath() + ".html",
                    formattedDate,
                    date
            ));

            LOG.info("Blog added: {}", title);
            count++;
        }

        // Sort latest first
        blogs.sort((a, b) -> {
            if (a.getDate() == null || b.getDate() == null) return 0;
            return b.getDate().compareTo(a.getDate());
        });

        LOG.info("Total blogs processed: {}", blogs.size());
    }

    /**
     * Recursively finds the featured image resource.
     *
     * @param resource the root resource
     * @return image resource if found, otherwise null
     */
    private Resource findImageResource(Resource resource) {
        if (resource == null) return null;

        if ("cq:featuredimage".equals(resource.getName())) {
            return resource;
        }

        for (Resource child : resource.getChildren()) {
            Resource result = findImageResource(child);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns list of blog items.
     *
     * @return list of BlogItem
     */
    public List<BlogItem> getBlogs() {
        return blogs != null ? blogs : Collections.emptyList();
    }

    /**
     * Returns blog section title.
     *
     * @return blog title
     */
    public String getBlogTitle() {
        return blogTitle;
    }
}