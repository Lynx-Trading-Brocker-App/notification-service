package com.notificationservice.domain;

import lombok.Value;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Represents a real-time price update for a financial instrument.
 * This DTO is immutable and created using the Builder pattern.
 */
@Value
@Builder
public class PriceUpdate {

    /**
     * The ticker symbol of the financial instrument (e.g., "AAPL", "GOOGL").
     */
    String ticker;

    /**
     * The latest trading price of the instrument.
     */
    BigDecimal price;

    /**
     * The absolute change in price from the previous trading day's close.
     */
    BigDecimal change;

    /**
     * The percentage change in price from the previous trading day's close.
     */
    BigDecimal change_pct;

    /**
     * The trading volume for the current session.
     */
    Integer volume;

    /**
     * The timestamp of when the market data was last updated, in ISO-8601 format.
     */
    String market_time;
}
