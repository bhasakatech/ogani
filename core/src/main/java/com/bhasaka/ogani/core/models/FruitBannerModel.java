package com.bhasaka.ogani.core.models;
import java.util.Collections;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FruitBannerModel {

    @ChildResource(name = "banners")
    private List<BannerItemModel> banners;

    public List<BannerItemModel> getBanners() {
        return banners != null ? banners : Collections.emptyList();
    }
}
