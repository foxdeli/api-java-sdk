package com.foxdeli.model.common;

import lombok.Builder;

@Builder(toBuilder = true)
public record Customer(
        String name,
        String email,
        String phone
) {
}
