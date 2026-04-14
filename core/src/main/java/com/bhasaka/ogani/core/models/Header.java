package com.bhasaka.ogani.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Header.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
/**
 * Sling model for the header component.
 *
 * @author Chandraprakash
 * <p>Resolves top bar data from the sibling {@code headertopbar} resource and
 * optionally loads department names from the configured tag root path.</p>
 */
public class Header {

    private static final Logger LOGGER = LoggerFactory.getLogger(Header.class);

    @ValueMapValue
    private String phoneNumber;
    
    @ValueMapValue
    private String supportText;

    @ValueMapValue
    private String tagRootPath;

    @ValueMapValue
    private boolean enableCart;

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    private HeaderTopBar topHeader;
    private List<String> departmentTags = new ArrayList<>();

    private String email;
    private String loginLink;
    private String loginIcon;
    private String promotionalText;
    private String[] languages;
    private List<SocialIcon> socialIcons;

    /**
     * Initializes derived header data after Sling injections are completed.
     */
    @PostConstruct
    protected void init() {
        try {
            if (resource != null) {
                Resource parentResource = resource.getParent();
                if (parentResource != null) {
                    Resource topBarResource = parentResource.getChild("headertopBar");
                    if (topBarResource != null) {
                        topHeader = topBarResource.adaptTo(HeaderTopBar.class);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to initialize top header from resource tree", e);
        }

        if (topHeader != null) {
            email = topHeader.getEmail();
            loginLink = topHeader.getLoginLink();
            loginIcon = topHeader.getLoginIcon();
            promotionalText = topHeader.getPromotionalText();
            languages = topHeader.getLanguages();
            socialIcons = topHeader.getSocialIcons();
        }


        try {
            if (StringUtils.isNotBlank(tagRootPath) && resourceResolver != null) {
                TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
                if (tagManager != null) {
                    Tag rootTag = tagManager.resolve(tagRootPath);
                    if (rootTag != null) {
                        Iterator<Tag> childTags = rootTag.listChildren();
                        while (childTags.hasNext()) {
                            Tag child = childTags.next();
                            departmentTags.add(child.getTitle());
                        }
                    } else {
                        LOGGER.warn("No root tag found for path: {}", tagRootPath);
                    }
                } else {
                    LOGGER.warn("TagManager adaptation returned null");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load department tags for path: {}", tagRootPath, e);
        }
    }

    /**
     * Returns the configured phone number or a default fallback value.
     *
     * @return the phone number to display in the header
     */
    public String getPhoneNumber() {
        return StringUtils.isNotBlank(phoneNumber) ? phoneNumber : "+65 11.188.888";
    }

    /**
     * Returns department tag titles resolved from the configured root tag.
     *
     * @return list of department names for navigation
     */
    public List<String> getDepartmentTags() {
        return departmentTags;
    }

    /**
     * Returns the adapted top header model.
     *
     * @return top header model, or {@code null} when unavailable
     */
    public HeaderTopBar getTopHeader() {
        return topHeader;
    }

    /**
     * Returns the support email from the top bar.
     *
     * @return support email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the promotional message shown in the top bar.
     *
     * @return promotional text
     */
    public String getPromotionalText() {
        return promotionalText;
    }

    /**
     * Returns the icon class or path used for login action.
     *
     * @return login icon representation
     */
    public String getLoginIcon() {
        return loginIcon;
    }

    /**
     * Returns the login link URL.
     *
     * @return login link
     */
    public String getLoginLink() {
        return loginLink;
    }

    /**
     * Returns social icons configured in the top bar.
     *
     * @return list of social icons
     */
    public List<SocialIcon> getSocialIcons() {
        return socialIcons;
    }

    /**
     * Returns available language labels configured in the top bar.
     *
     * @return language array
     */
    public String[] getLanguages() {
        return languages;
    }

    /**
     * Returns support text configured on the header component.
     *
     * @return support text
     */
    public String getSupportText() {
        return supportText;
    }

    /**
     * Indicates whether cart UI should be enabled in the header.
     *
     * @return {@code true} if cart is enabled; otherwise {@code false}
     */
    public boolean isEnableCart() {
        return enableCart;
    }
}