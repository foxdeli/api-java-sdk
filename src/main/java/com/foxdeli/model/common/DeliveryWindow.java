package com.foxdeli.model.common;

import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record DeliveryWindow(
        Instant from,
        Instant to
) {
}
