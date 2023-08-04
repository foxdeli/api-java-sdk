package com.foxdeli.model.response;

import com.foxdeli.model.common.DeliveryWindow;
import com.foxdeli.model.common.Dimensions;
import com.foxdeli.model.common.ParcelState;
import com.foxdeli.model.common.ParcelTimeline;
import com.foxdeli.model.common.ParcelTracking;
import com.foxdeli.orders.api.model.TrackingState;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record Parcel(
        UUID parcelId,
        UUID orderId,
        Dimensions dimensions,
        ParcelState state,
        TrackingState trackingState,
        Instant maxStoreDate,
        DeliveryWindow deliveryWindow,
        ParcelTracking activeTracking,
        List<ParcelTimeline> timeline,
        List<String> products,
        String carrierTrackingUrl
) {
}
