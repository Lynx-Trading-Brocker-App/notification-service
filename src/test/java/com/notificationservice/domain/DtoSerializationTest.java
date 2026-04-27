package com.notificationservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests JSON serialization and deserialization for all DTOs.
 */
class DtoSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testPriceUpdateSerialization() throws Exception {
        PriceUpdate dto = PriceUpdate.builder()
                .ticker("AAPL")
                .price(new BigDecimal("150.00"))
                .change(new BigDecimal("1.50"))
                .change_pct(new BigDecimal("0.01"))
                .volume(1000000)
                .market_time("2023-10-27T10:00:00Z")
                .build();

        String json = objectMapper.writeValueAsString(dto);
        PriceUpdate deserializedDto = objectMapper.readValue(json, PriceUpdate.class);

        assertThat(deserializedDto).isEqualTo(dto);
    }

    @Test
    void testOrderUpdateSerialization() throws Exception {
        OrderUpdate dto = OrderUpdate.builder()
                .order_id("12345")
                .status("FILLED")
                .filled_quantity(100)
                .average_fill_price(new BigDecimal("149.50"))
                .exchange_fee(new BigDecimal("1.00"))
                .market_time("2023-10-27T10:05:00Z")
                .build();

        String json = objectMapper.writeValueAsString(dto);
        OrderUpdate deserializedDto = objectMapper.readValue(json, OrderUpdate.class);

        assertThat(deserializedDto).isEqualTo(dto);
    }

    @Test
    void testMarketEventSerialization() throws Exception {
        MarketEvent dto = MarketEvent.builder()
                .event_id("evt-001")
                .event_type("FED_ANNOUNCEMENT")
                .headline("Fed raises interest rates by 25 bps")
                .scope("MARKET_WIDE")
                .target(null)
                .magnitude(0.75)
                .duration_ticks(60)
                .market_time("2023-10-27T14:00:00Z")
                .build();

        String json = objectMapper.writeValueAsString(dto);
        MarketEvent deserializedDto = objectMapper.readValue(json, MarketEvent.class);

        assertThat(deserializedDto).isEqualTo(dto);
    }

    @Test
    void testMessageEnvelopeSerialization() throws Exception {
        PriceUpdate priceUpdate = PriceUpdate.builder().ticker("GOOGL").price(new BigDecimal("2800.00")).build();
        MessageEnvelope<PriceUpdate> envelope = MessageEnvelope.<PriceUpdate>builder()
                .type("PRICE_UPDATE")
                .payload(priceUpdate)
                .build();

        String json = objectMapper.writeValueAsString(envelope);
        MessageEnvelope deserializedEnvelope = objectMapper.readValue(json, MessageEnvelope.class);

        assertThat(deserializedEnvelope.getType()).isEqualTo("PRICE_UPDATE");
        // Note: Payload is deserialized as a LinkedHashMap, which is expected behavior
        assertThat(deserializedEnvelope.getPayload()).isInstanceOf(java.util.LinkedHashMap.class);
    }
}
