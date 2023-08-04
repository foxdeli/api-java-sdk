package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.Carrier;
import com.foxdeli.orders.api.model.CountryCode;
import lombok.Builder;

@Builder(toBuilder = true)
public record ParcelShopData(
        Carrier carrier,
        CountryCode countryCode,
        String parcelShopId
) {
}
