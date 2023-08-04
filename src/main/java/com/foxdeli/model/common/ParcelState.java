package com.foxdeli.model.common;

import com.foxdeli.orders.api.model.DeliveryState;
import com.foxdeli.orders.api.model.ImportantState;
import com.foxdeli.orders.api.model.UrgentState;
import lombok.Builder;

@Builder(toBuilder = true)
public record ParcelState(
        DeliveryState delivery,
        ImportantState important,
        UrgentState urgent,
        Boolean deliverToday,
        Boolean returning
) {
}
