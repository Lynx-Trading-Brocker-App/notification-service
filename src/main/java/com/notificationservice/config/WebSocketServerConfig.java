package com.notificationservice.config;

import com.notificationservice.frontend.FrontendWebSocketHandler;
import com.notificationservice.frontend.WebSocketNotificationSender;
import com.notificationservice.service.SessionRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Spring configuration class for WebSocket server setup.
 * Registers the frontend WebSocket handler and configures CORS policies.
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketServerConfig implements WebSocketConfigurer {

    private final WebSocketNotificationSender webSocketNotificationSender;
    private final SessionRegistryService sessionRegistryService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        FrontendWebSocketHandler handler = new FrontendWebSocketHandler(
                webSocketNotificationSender,
                sessionRegistryService
        );

        registry.addHandler(handler, "/ws/notifications")
                .setAllowedOrigins("*");
    }
}

