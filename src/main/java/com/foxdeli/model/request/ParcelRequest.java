package com.foxdeli.model.request;

import com.foxdeli.model.common.Dimensions;
import com.foxdeli.model.common.ParcelTrackingConfig;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ParcelRequest(
        List<String> products,
        Dimensions dimensions,
        List<ParcelTrackingConfig> tracking
) {
}
