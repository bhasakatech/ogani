package com.bhasaka.ogani.core.models.bloglisting;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BlogListingModel {

    @ValueMapValue
    private String blogTitle;

    @ValueMapValue
    private String blogParentPath;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<BlogItem> blogs = new ArrayList<>();

    @PostConstruct
    protected void init() {

        if (blogParentPath == null) {
            return;
        }

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            return;
        }

        Page parentPage = pageManager.getPage(blogParentPath);

        if (parentPage == null) {
            return;
        }

        Iterator<Page> childPages = parentPage.listChildren();

        while (childPages.hasNext()) {

            Page child = childPages.next();
            Resource contentResource = child.getContentResource();

            if (contentResource == null) {
                continue;
            }

            // ✅ Fetch properties
            String title = child.getTitle();
            String description = contentResource.getValueMap().get("jcr:description", "");
            String image = contentResource.getValueMap().get("fileReference", "");

            // ✅ jcr:created
            Date date = contentResource.getValueMap().get("jcr:created", Date.class);

            String formattedDate = "";
            if (date != null) {
                formattedDate = new SimpleDateFormat("dd MMM yyyy").format(date);
            }

            blogs.add(new BlogItem(
                    title,
                    description,
                    image,
                    child.getPath() + ".html",
                    formattedDate,
                    date
            ));
        }

        // ✅ Sort by latest (IMPORTANT)
        blogs.sort((a, b) -> {
            if (a.getDate() == null || b.getDate() == null) return 0;
            return b.getDate().compareTo(a.getDate());
        });
    }

    public List<BlogItem> getBlogs() {
        return blogs;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public String getBlogParentPath() {
        return blogParentPath;
    }
}