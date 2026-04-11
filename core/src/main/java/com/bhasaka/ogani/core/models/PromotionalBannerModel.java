
package com.bhasaka.ogani.core.models;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PromotionalBannerModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String bgImage;

    @ValueMapValue
    private String alignment;



    public String getTitle() {
        return title != null ? title : "";
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public String getBgImage() {
        return bgImage != null ? bgImage : "";
    }

    public String getAlignment() {
        return alignment != null ? alignment.trim().toLowerCase() : "left";
    }
}
