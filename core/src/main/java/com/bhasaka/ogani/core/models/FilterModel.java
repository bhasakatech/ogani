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

/**
 * Sling Model for Product Filter component.
 *
 * This model provides filtering data such as price range, colors, sizes,
 * and department categories for product listing pages.
 *
 * Data is read from:
 * - Component properties for price, colors, sizes
 * - Tag structure for department categories
 *
 * Responsibilities:
 * - Expose filter values like minPrice, maxPrice, colors, and sizes
 * - Resolve tag hierarchy using TagManager
 * - Extract child tag titles as department filters
 *
 * Flow:
 * - HTL adapts resource to this model
 * - ValueMapValue injects component properties
 * - SlingObject injects ResourceResolver
 * - PostConstruct initializes department tags
 * - TagManager resolves tagRootPath
 * - Child tags are iterated and titles are collected
 *
 * Behavior:
 * - If tagRootPath is empty or invalid, department list remains empty
 * - Handles null checks for TagManager and Tag safely
 *
 * Injection Strategy:
 * Uses DefaultInjectionStrategy.OPTIONAL to avoid failures when properties are missing.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FilterModel {

    /**
     * Minimum price for filtering.
     */
    @ValueMapValue
    private int minPrice;

    /**
     * Maximum price for filtering.
     */
    @ValueMapValue
    private int maxPrice;

    /**
     * List of available colors.
     */
    @ValueMapValue
    private List<String> colors;

    /**
     * List of available sizes.
     */
    @ValueMapValue
    private List<String> sizes;

    /**
     * Root path of tags used for department filtering.
     */
    @ValueMapValue
    private String tagRootPath;

    /**
     * ResourceResolver used to access repository and TagManager.
     */
    @SlingObject
    private ResourceResolver resourceResolver;

    /**
     * List of department tag titles derived from tag hierarchy.
     */
    private List<String> departmentTags = new ArrayList<>();

    /**
     * Returns minimum price.
     *
     * @return min price
     */
    public int getMinPrice() {
        return minPrice;
    }

    /**
     * Returns maximum price.
     *
     * @return max price
     */
    public int getMaxPrice() {
        return maxPrice;
    }

    /**
     * Returns list of colors.
     *
     * @return list of colors
     */
    public List<String> getColors() {
        return colors;
    }

    /**
     * Returns list of sizes.
     *
     * @return list of sizes
     */
    public List<String> getSizes() {
        return sizes;
    }

    /**
     * Returns department tags.
     *
     * @return list of department tag titles
     */
    public List<String> getDepartmentTags() {
        return departmentTags;
    }

    /**
     * Initializes department tags after dependency injection.
     *
     * Logic:
     * - Checks if tagRootPath is not blank
     * - Adapts ResourceResolver to TagManager
     * - Resolves root tag using tagRootPath
     * - Iterates child tags
     * - Adds each child tag title to departmentTags list
     *
     * Safe Handling:
     * - Skips processing if TagManager or root tag is null
     * - Prevents exceptions from breaking execution
     */
    @PostConstruct
    protected void init() {

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