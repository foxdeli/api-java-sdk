package com.foxdeli.mapper;

import com.foxdeli.model.request.OrderRequest;
import com.foxdeli.model.response.Order;
import com.foxdeli.orders.api.model.OrderRegistration;
import com.foxdeli.orders.api.model.OrderUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(uses = {ParcelMapper.class, CommonMapper.class})
public interface OrderMapper {

    OrderUpdate map(OrderRequest request);

    OrderRegistration map(OrderRequest request, UUID marketId, UUID eshopId);

    @Mapping(target = "orderId", source = "id")
    Order map(com.foxdeli.orders.api.model.Order response);
}
