package com.bhasaka.ogani.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class MapLocationModel {

    @ValueMapValue
    private String latitude;

    @ValueMapValue
    private String longitude;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String phone;

    @ValueMapValue
    private String address;

    private boolean hasWidgetDetails;

    @PostConstruct
    protected void init() {
        if (StringUtils.isBlank(latitude)) {
            latitude = "17.387140";
        }
        if (StringUtils.isBlank(longitude)) {
            longitude = "78.491684";
        }

        hasWidgetDetails = StringUtils.isNotBlank(title) ||
                StringUtils.isNotBlank(phone) ||
                StringUtils.isNotBlank(address);
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public boolean isHasWidgetDetails() {
        return hasWidgetDetails;
    }
}
