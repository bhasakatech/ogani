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

    private List<BlogItem> blogs = new ArrayList<>();

    @PostConstruct
    protected void init() {

        if (resourceResolver == null || blogParentPath == null) {
            LOG.error("Resource resolver is null");
            return;
        }

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            LOG.error("Page manager is null");
            return;
        }

        Page parentPage = pageManager.getPage(blogParentPath);

        if (parentPage == null) {
            LOG.error("parentPage is null");
            return;
        }

        Iterator<Page> childPages = parentPage.listChildren();

        int count = 0;

        while (childPages.hasNext() && count < 3) {

            Page child = childPages.next();
            Resource contentResource = child.getContentResource();

            if (contentResource == null) continue;

            String title = child.getTitle();
            String description = contentResource.getValueMap().get("jcr:description", "");

            // Image
            Resource imageResource = findImageResource(contentResource);
            String image = "";
            if (imageResource != null) {
                image = imageResource.getValueMap().get("fileReference", "");
                LOG.info("Image path : {}", image);
            } else {
                LOG.error("Image path is null");
            }

            Date date = contentResource.getValueMap().get("jcr:created", Date.class);

            String formattedDate = "";
            if (date != null) {
                formattedDate = new java.text.SimpleDateFormat("MMM dd, yyyy").format(date);
            }

            blogs.add(new BlogItem(
                    title,
                    description,
                    image,
                    child.getPath() + ".html",
                    formattedDate,
                    date
            ));
            count++;
        }

        // Sort latest first
        blogs.sort((a, b) -> {
            if (a.getDate() == null || b.getDate() == null) return 0;
            return b.getDate().compareTo(a.getDate());
        });
    }

    // Find Image Resource (/jcr:content/cq:featuredimage/fileReference)
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

    public List<BlogItem> getBlogs() {
        return blogs != null ? blogs : Collections.emptyList();
    }

    public String getBlogTitle() {
        return blogTitle;
    }
}