package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model for the Hero Banner component.
 * <p>
 * This model adapts from a {@link Resource} and exposes authored fields
 * such as background image, along with contextual information like the current page.
 * </p>
 *
 * Example usage in HTL:
 * <pre>
 *     ${heroBanner.backgroundImage}
 * </pre>
 *
 * Default injection strategy is OPTIONAL, so missing fields will not break the model.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroBanner {

    private static final Logger LOG = LoggerFactory.getLogger(HeroBanner.class);

    /** Background image path or reference for the hero banner. */
    @ValueMapValue
    private String backgroundImage;

    /** The current page context where this component is authored. */
    @ScriptVariable
    private Page currentPage;

    /**
     * Returns the background image path for the hero banner.
     *
     * @return background image path, or null if not authored
     */
    public String getBackgroundImage() {
        if (backgroundImage == null || backgroundImage.isEmpty()) {
            LOG.warn("Background image is missing or empty on page: {}",
                    currentPage != null ? currentPage.getPath() : "unknown");
        } else {
            LOG.debug("Background image retrieved: {} on page: {}",
                    backgroundImage,
                    currentPage != null ? currentPage.getPath() : "unknown");
        }
        return backgroundImage;
    }
}
