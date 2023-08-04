package com.foxdeli.mapper;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;

@Mapper
public interface CommonMapper {

    default Instant map(OffsetDateTime time) {
        if (time == null) {
            return null;
        }
        return time.toInstant();
    }
}
