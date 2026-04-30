package com.notificationservice.exchange;

import com.notificationservice.domain.OrderUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * REST client for forwarding order status updates to the internal order-service.
 * Receives OrderUpdate objects from the exchange and makes PUT requests to the order-service API.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InternalOrderForwarder {

    private final RestTemplate restTemplate;

    /**
     * Forwards an order status update to the order-service.
     *
     * @param orderUpdate The order update received from the exchange.
     */
    public void forwardOrderUpdate(OrderUpdate orderUpdate) {
        try {
            String url = "http://order-service/api/v1/orders/{orderId}/status";
            restTemplate.put(url, orderUpdate, orderUpdate.getOrder_id());

            log.info("Order update forwarded successfully for order: {}", orderUpdate.getOrder_id());
        } catch (Exception e) {
            log.error("Failed to forward order update for order: {}", orderUpdate.getOrder_id(), e);
        }
    }
}

