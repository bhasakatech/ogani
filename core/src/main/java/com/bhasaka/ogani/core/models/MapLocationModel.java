package com.bhasaka.ogani.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
/**
 * Sling model for the map location widget.
 *
 * <p>Provides map coordinates and optional widget details such as title,
 * phone number, and address. Missing coordinates are replaced with default
 * fallback values during initialization.</p>
 */
public class MapLocationModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapLocationModel.class);

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

    /**
     * Applies default coordinate values and determines whether widget details are present.
     */
    @PostConstruct
    protected void init() {
        if (StringUtils.isBlank(latitude)) {
            latitude = "17.387140";
            LOGGER.debug("Latitude was blank; using default value {}", latitude);
        }
        if (StringUtils.isBlank(longitude)) {
            longitude = "78.491684";
            LOGGER.debug("Longitude was blank; using default value {}", longitude);
        }

        hasWidgetDetails = StringUtils.isNotBlank(title) ||
                StringUtils.isNotBlank(phone) ||
                StringUtils.isNotBlank(address);

        if (!hasWidgetDetails) {
            LOGGER.debug("Map widget details are empty; title, phone, and address are all blank");
        }
    }

    /**
     * Returns the latitude used by the map widget.
     *
     * @return latitude value
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude used by the map widget.
     *
     * @return longitude value
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Returns the configured location title.
     *
     * @return title value
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the configured contact phone number.
     *
     * @return phone value
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the configured address.
     *
     * @return address value
     */
    public String getAddress() {
        return address;
    }

    /**
     * Indicates whether any widget details are configured.
     *
     * @return {@code true} when title, phone, or address is present
     */
    public boolean isHasWidgetDetails() {
        return hasWidgetDetails;
    }
}
