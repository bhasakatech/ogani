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
/**
 * Sling model representing header top bar content.
 *
 * <p>Provides author-configured values such as support email, promotional text,
 * login metadata, social icons, and available languages.</p>
 */
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

    /**
     * Returns the configured support email.
     *
     * @return support email address
     */
    public String getEmail() { return email; }

    /**
     * Returns promotional text shown in the top bar.
     *
     * @return promotional text
     */
    public String getPromotionalText() { return promotionalText; }

    /**
     * Returns the login icon class or identifier.
     *
     * @return login icon value
     */
    public String getLoginIcon() { return loginIcon; }

    /**
     * Returns the login link URL.
     *
     * @return login link
     */
    public String getLoginLink() { return loginLink; }

    /**
     * Returns configured social icons from the {@code iconList} child resource.
     *
     * @return list of social icons
     */
    public List<SocialIcon> getSocialIcons() { return socialIcons; }

    /**
     * Returns the language options configured for the top bar.
     *
     * @return language array
     */
    public String[] getLanguages() { return languages; }
}

