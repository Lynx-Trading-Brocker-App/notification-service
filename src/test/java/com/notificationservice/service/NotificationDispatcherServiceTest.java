package com.notificationservice.service;

import com.notificationservice.core.NotificationSender;
import com.notificationservice.domain.MarketEvent;
import com.notificationservice.domain.MessageEnvelope;
import com.notificationservice.domain.OrderUpdate;
import com.notificationservice.domain.PriceUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link NotificationDispatcherService}.
 */
@ExtendWith(MockitoExtension.class)
class NotificationDispatcherServiceTest {

    @Mock
    private NotificationSender notificationSender;

    @InjectMocks
    private NotificationDispatcherService notificationDispatcherService;

    @Test
    void dispatchOrderUpdate_shouldSendToUser() {
        // Given
        String userId = "user123";
        OrderUpdate orderUpdate = OrderUpdate.builder().order_id("ord-1").status("FILLED").build();
        ArgumentCaptor<MessageEnvelope<OrderUpdate>> envelopeCaptor = ArgumentCaptor.forClass(MessageEnvelope.class);

        // When
        notificationDispatcherService.dispatchOrderUpdate(userId, orderUpdate);

        // Then
        verify(notificationSender).sendToUser(eq(userId), envelopeCaptor.capture());
        MessageEnvelope<OrderUpdate> capturedEnvelope = envelopeCaptor.getValue();
        assertThat(capturedEnvelope.getType()).isEqualTo("ORDER_UPDATE");
        assertThat(capturedEnvelope.getPayload()).isEqualTo(orderUpdate);
    }

    @Test
    void dispatchPriceUpdate_shouldBroadcast() {
        // Given
        PriceUpdate priceUpdate = PriceUpdate.builder().ticker("AAPL").price(new BigDecimal("150.00")).build();
        ArgumentCaptor<MessageEnvelope<PriceUpdate>> envelopeCaptor = ArgumentCaptor.forClass(MessageEnvelope.class);

        // When
        notificationDispatcherService.dispatchPriceUpdate(priceUpdate);

        // Then
        verify(notificationSender).broadcast(envelopeCaptor.capture());
        MessageEnvelope<PriceUpdate> capturedEnvelope = envelopeCaptor.getValue();
        assertThat(capturedEnvelope.getType()).isEqualTo("PRICE_UPDATE");
        assertThat(capturedEnvelope.getPayload()).isEqualTo(priceUpdate);
    }

    @Test
    void dispatchMarketEvent_shouldBroadcast() {
        // Given
        MarketEvent marketEvent = MarketEvent.builder().event_id("evt-1").headline("Market closing soon").build();
        ArgumentCaptor<MessageEnvelope<MarketEvent>> envelopeCaptor = ArgumentCaptor.forClass(MessageEnvelope.class);

        // When
        notificationDispatcherService.dispatchMarketEvent(marketEvent);

        // Then
        verify(notificationSender).broadcast(envelopeCaptor.capture());
        MessageEnvelope<MarketEvent> capturedEnvelope = envelopeCaptor.getValue();
        assertThat(capturedEnvelope.getType()).isEqualTo("MARKET_EVENT");
        assertThat(capturedEnvelope.getPayload()).isEqualTo(marketEvent);
    }
}
