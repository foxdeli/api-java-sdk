package com.foxdeli.model.common;

import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record Snooze(
        Instant until,
        String author,
        String note,
        Boolean snoozed
) {
}
