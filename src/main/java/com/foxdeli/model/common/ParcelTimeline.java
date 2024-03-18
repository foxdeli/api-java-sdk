package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.TimelineType;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Builder(toBuilder = true)
public record ParcelTimeline(
        UUID timelineId,
        TimelineType type,
        String text,
        Instant created,
        String author,
        Map<String, Object> additionalParams
) {
}
