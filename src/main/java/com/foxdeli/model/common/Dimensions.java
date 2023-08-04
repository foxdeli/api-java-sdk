package com.foxdeli.model.common;

import lombok.Builder;

@Builder(toBuilder = true)
public record Dimensions(
        Double weight,
        Integer height,
        Integer length,
        Integer width
) {
}
