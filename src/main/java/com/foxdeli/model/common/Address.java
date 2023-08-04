package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.CountryCode;
import lombok.Builder;

@Builder(toBuilder = true)
public record Address(
        String line1,
        String line2,
        String city,
        String postalCode,
        CountryCode countryCode,
        String state,
        String region,
        Double longitude,
        Double latitude
) {
}
