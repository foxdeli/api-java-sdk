package com.foxdeli.model.request;

import com.foxdeli.model.common.AdditionalCost;
import com.foxdeli.model.common.Customer;
import com.foxdeli.model.common.Destination;
import com.foxdeli.model.common.Money;
import com.foxdeli.model.common.PaymentInfo;
import com.foxdeli.model.common.Product;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record OrderRequest(

        String platform,
        String orderNumber,
        String externalIdentifier,
        Money price,
        Money cashOnDelivery,
        List<AdditionalCost> additionalCosts,
        PaymentInfo payment,
        Customer customer,
        Destination destination,
        List<Product> products,
        List<ParcelRequest> parcels
) {
}
