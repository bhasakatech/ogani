package com.bhasaka.ogani.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FilterModel {

    @ValueMapValue
    private int minPrice;

    @ValueMapValue
    private int maxPrice;

    @ValueMapValue
    private List<String> colors;

    @ValueMapValue
    private List<String> sizes;

    @ValueMapValue
    private String tagRootPath;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<String> departmentTags = new ArrayList<>();


    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public List<String> getDepartmentTags() {
        return departmentTags;
    }


    @PostConstruct
    protected void init(){

        if (StringUtils.isNotBlank(tagRootPath)) {
            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
            if (tagManager != null) {
                Tag rootTag = tagManager.resolve(tagRootPath);
                if (rootTag != null) {
                    Iterator<Tag> childTags = rootTag.listChildren();
                    while (childTags.hasNext()) {
                        Tag child = childTags.next();
                        departmentTags.add(child.getTitle());
                    }
                }
            }
        }

    }
}