package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.Carrier;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record ParcelTrackingConfig(
        Carrier carrier,
        String number,
        String referenceId,
        UUID carrierConfigurationId
) {
}
