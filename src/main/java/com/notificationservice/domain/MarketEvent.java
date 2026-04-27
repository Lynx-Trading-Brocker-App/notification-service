package com.notificationservice.domain;

import lombok.Value;
import lombok.Builder;

/**
 * Represents a significant market event that may impact trading.
 * This DTO is immutable and created using the Builder pattern.
 */
@Value
@Builder
public class MarketEvent {

    /**
     * A unique identifier for the event.
     */
    String event_id;

    /**
     * The type of the event (e.g., "EARNINGS_REPORT", "FED_ANNOUNCEMENT").
     */
    String event_type;

    /**
     * A brief, human-readable headline for the event.
     */
    String headline;

    /**
     * The scope of the event's impact (e.g., "MARKET_WIDE", "SECTOR_SPECIFIC", "SINGLE_STOCK").
     */
    String scope;

    /**
     * The specific target of the event, if applicable (e.g., a ticker symbol for a single stock event).
     */
    String target;

    /**
     * The magnitude of the event's expected impact, on a scale (e.g., 0.0 to 1.0).
     */
    Double magnitude;

    /**
     * The expected duration of the event's impact, in trading ticks or minutes.
     */
    Integer duration_ticks;

    /**
     * The timestamp when the event occurred or was announced, in ISO-8601 format.
     */
    String market_time;
}
