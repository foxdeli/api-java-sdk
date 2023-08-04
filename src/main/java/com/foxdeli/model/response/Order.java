package com.foxdeli.model.response;

import com.foxdeli.model.common.AdditionalCost;
import com.foxdeli.model.common.Customer;
import com.foxdeli.model.common.Destination;
import com.foxdeli.model.common.Money;
import com.foxdeli.model.common.PaymentInfo;
import com.foxdeli.model.common.Product;
import com.foxdeli.model.common.Snooze;
import com.foxdeli.orders.api.model.OrderState;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record Order(
        UUID orderId,
        String platform,
        String orderNumber,
        OrderState orderState,
        UUID marketId,
        UUID eshopId,
        String externalIdentifier,
        Destination destination,
        Money price,
        List<AdditionalCost> additionalCosts,
        Money cashOnDelivery,
        PaymentInfo payment,
        Customer customer,
        List<Parcel> parcels,
        List<Product> products,
        Instant cancelled,
        Boolean inImportantState,
        Boolean inUrgentState,
        Snooze snooze
) {
}
