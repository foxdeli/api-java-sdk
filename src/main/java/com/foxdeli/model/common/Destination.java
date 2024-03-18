package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.DestinationType;
import lombok.Builder;

@Builder(toBuilder = true)
public record Destination(
        DestinationType type,
        Address address,
        ParcelShopData parcelShop
) {
}
