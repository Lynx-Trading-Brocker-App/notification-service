package com.notificationservice.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.service.NotificationDispatcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

/**
 * Startup component for establishing and maintaining the WebSocket connection to the exchange.
 * Initializes the connection during application startup using CommandLineRunner.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ExchangeConnectionManager implements CommandLineRunner {

    private final NotificationDispatcherService notificationDispatcherService;
    private final InternalOrderForwarder internalOrderForwarder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing exchange WebSocket connection...");

        new Thread(() -> {
            try {
                WebSocketClient client = new StandardWebSocketClient();
                String exchangeUri = "wss://mock-exchange-host/ws?api_key=mock_key&api_secret=mock_secret";

                ExchangeWebSocketHandler handler = new ExchangeWebSocketHandler(
                        notificationDispatcherService,
                        internalOrderForwarder,
                        new ObjectMapper()
                );

                client.doHandshake(handler, exchangeUri).get();
                log.info("Successfully connected to exchange at: {}", exchangeUri);
            } catch (Exception e) {
                log.error("Failed to connect to exchange", e);
            }
        }).start();
    }
}

