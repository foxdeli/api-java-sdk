package com.foxdeli.model.common;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record Money(
        BigDecimal amount,
        String currency
) {
}
