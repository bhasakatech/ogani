package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model for the Hero Banner component.
 * <p>
 * This model adapts from a {@link Resource} and exposes authored fields
 * such as eyebrow text, hero description, and subtext for rendering in HTL.
 * </p>
 *
 * Example usage in HTL:
 * <pre>
 *     ${heroBanner.eyebrowText}
 *     ${heroBanner.heroDesc}
 *     ${heroBanner.subText}
 * </pre>
 *
 * Default injection strategy is OPTIONAL, so missing fields will not break the model.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeroBannerModel {

    private static final Logger LOG = LoggerFactory.getLogger(HeroBannerModel.class);

    /** Eyebrow text displayed above the main banner heading. */
    @ValueMapValue
    private String eyebrowText;

    /** Main hero description text. */
    @ValueMapValue
    private String heroDesc;

    /** Supporting subtext displayed below the hero description. */
    @ValueMapValue
    private String subText;

    /**
     * Returns the eyebrow text for the hero banner.
     *
     * @return eyebrow text, or null if not authored
     */
    public String getEyebrowText() {
        if (eyebrowText == null || eyebrowText.isEmpty()) {
            LOG.warn("Eyebrow text is missing or empty.");
        } else {
            LOG.debug("Eyebrow text retrieved: {}", eyebrowText);
        }
        return eyebrowText;
    }

    /**
     * Returns the hero description text.
     *
     * @return hero description, or null if not authored
     */
    public String getHeroDesc() {
        if (heroDesc == null || heroDesc.isEmpty()) {
            LOG.warn("Hero description is missing or empty.");
        } else {
            LOG.debug("Hero description retrieved: {}", heroDesc);
        }
        return heroDesc;
    }

    /**
     * Returns the supporting subtext for the hero banner.
     *
     * @return subtext, or null if not authored
     */
    public String getSubText() {
        if (subText == null || subText.isEmpty()) {
            LOG.warn("Subtext is missing or empty.");
        } else {
            LOG.debug("Subtext retrieved: {}", subText);
        }
        return subText;
    }
}
