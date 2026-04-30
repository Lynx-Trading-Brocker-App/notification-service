package com.notificationservice.frontend;

import com.notificationservice.service.SessionRegistryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;

/**
 * WebSocket handler for managing frontend client connections.
 * Handles session establishment and closure, registering/unregistering users and their sessions.
 */
@Slf4j
@RequiredArgsConstructor
public class FrontendWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketNotificationSender webSocketNotificationSender;
    private final SessionRegistryService sessionRegistryService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = extractUserIdFromUri(session.getUri());

        if (userId != null) {
            String sessionId = session.getId();
            sessionRegistryService.registerSession(userId, sessionId);
            webSocketNotificationSender.addSession(sessionId, session);
            log.info("Frontend client connected. User ID: {}, Session ID: {}", userId, sessionId);
        } else {
            session.close();
            log.warn("Closing session due to missing userId parameter");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userId = extractUserIdFromSession(session);

        if (userId != null) {
            String sessionId = session.getId();
            sessionRegistryService.removeSession(userId, sessionId);
            webSocketNotificationSender.removeSession(sessionId);
            log.info("Frontend client disconnected. User ID: {}, Session ID: {}", userId, sessionId);
        }
    }

    private String extractUserIdFromUri(URI uri) {
        if (uri == null || uri.getQuery() == null) {
            return null;
        }

        String query = uri.getQuery();
        for (String param : query.split("&")) {
            if (param.startsWith("userId=")) {
                return param.substring(7);
            }
        }

        return null;
    }

    private String extractUserIdFromSession(WebSocketSession session) {
        URI uri = session.getUri();
        return extractUserIdFromUri(uri);
    }
}

