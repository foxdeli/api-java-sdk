package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.Carrier;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record ParcelTracking(
        UUID trackingId,
        Carrier carrier,
        String number,
        String url,
        String referenceId,
        UUID carrierConfigurationId,
        String courierPhone
) {
}
