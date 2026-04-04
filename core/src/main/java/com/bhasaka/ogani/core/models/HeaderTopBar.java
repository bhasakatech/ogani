package com.bhasaka.ogani.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(adaptables = Resource.class,
        adapters = HeaderTopBar.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderTopBar {

    @ValueMapValue
    private String email;
    @ValueMapValue
    private String promotionalText;
    @ValueMapValue
    private String loginIcon;
    @ValueMapValue
    private String loginLink;
    @ChildResource(name = "iconList")
    private List<SocialIcon> socialIcons;
    @ValueMapValue
    private String[] languages;

    public String getEmail() { return email; }
    public String getPromotionalText() { return promotionalText; }
    public String getLoginIcon() { return loginIcon; }
    public String getLoginLink() { return loginLink; }
    public List<SocialIcon> getSocialIcons() { return socialIcons; }
    public String[] getLanguages() { return languages; }
}

