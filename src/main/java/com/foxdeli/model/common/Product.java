package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.ProductType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record Product(
        ProductType type,
        String sku,
        String name,
        String description,
        String url,
        String image,
        Money price,
        BigDecimal vat,
        Integer quantity,
        String referencedSku
) {
}
