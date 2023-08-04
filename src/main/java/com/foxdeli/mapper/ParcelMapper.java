package com.foxdeli.mapper;

import com.foxdeli.model.request.ParcelRequest;
import com.foxdeli.model.response.Parcel;
import com.foxdeli.orders.api.model.DeliveryState;
import com.foxdeli.orders.api.model.InputDeliveryState;
import com.foxdeli.orders.api.model.ParcelRegistration;
import com.foxdeli.orders.api.model.ParcelStateUpdate;
import com.foxdeli.orders.api.model.ParcelUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

@Mapper(uses = CommonMapper.class)
public interface ParcelMapper {

    ParcelRegistration mapToCreate(ParcelRequest request);

    ParcelUpdate mapToUpdate(ParcelRequest request);

    @Mapping(source = "deliveryState", target = "state")
    ParcelStateUpdate map(DeliveryState deliveryState);

    @Mapping(target = "parcelId", source = "id")
    Parcel map(com.foxdeli.orders.api.model.Parcel parcel);

    @ValueMapping(source = "EXPIRED", target = MappingConstants.THROW_EXCEPTION)
    InputDeliveryState mapDeliveryState(DeliveryState state);

}
