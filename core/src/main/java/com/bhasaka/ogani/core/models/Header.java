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
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Header.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Header {

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

    @PostConstruct
    protected void init() {
        if (resource != null) {
            Resource parentResource = resource.getParent();
            if (parentResource != null) {
                Resource topBarResource = parentResource.getChild("headertopbar");
                if (topBarResource != null) {
                    topHeader = topBarResource.adaptTo(HeaderTopBar.class);
                }
            }
        }

        if (topHeader != null) {
            email = topHeader.getEmail();
            loginLink = topHeader.getLoginLink();
            loginIcon = topHeader.getLoginIcon();
            promotionalText = topHeader.getPromotionalText();
            languages = topHeader.getLanguages();
            socialIcons = topHeader.getSocialIcons();
        }


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

    public String getPhoneNumber() {
        return StringUtils.isNotBlank(phoneNumber) ? phoneNumber : "+65 11.188.888";
    }

    public List<String> getDepartmentTags() {
        return departmentTags;
    }

    public HeaderTopBar getTopHeader() {
        return topHeader;
    }

    public String getEmail() {
        return email;
    }

    public String getPromotionalText() {
        return promotionalText;
    }

    public String getLoginIcon() {
        return loginIcon;
    }

    public String getLoginLink() {
        return loginLink;
    }

    public List<SocialIcon> getSocialIcons() {
        return socialIcons;
    }

    public String[] getLanguages() {
        return languages;
    }

    public String getSupportText() {
        return supportText;
    }

    public boolean isEnableCart() {
        return enableCart;
    }
}