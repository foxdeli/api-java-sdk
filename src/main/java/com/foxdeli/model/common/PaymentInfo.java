package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.PaymentMethod;
import com.foxdeli.orders.api.model.PaymentService;
import lombok.Builder;

@Builder(toBuilder = true)
public record PaymentInfo(
        PaymentMethod method,
        PaymentService service
) {
}
