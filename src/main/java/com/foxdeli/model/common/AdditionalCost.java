package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.AdditionalCostType;
import lombok.Builder;

@Builder(toBuilder = true)
public record AdditionalCost(
        AdditionalCostType type,
        Money price,
        String name
) {
}
