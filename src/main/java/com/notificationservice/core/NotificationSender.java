package com.notificationservice.core;

import com.notificationservice.domain.MessageEnvelope;

/**
 * Defines the contract for sending notifications to users or broadcasting messages.
 * The implementation will be provided by the transport layer (e.g., WebSocket, messaging queue).
 */
public interface NotificationSender {

    /**
     * Sends a message to a specific user.
     *
     * @param platformUserId The unique identifier of the user.
     * @param message        The message envelope to send.
     */
    void sendToUser(String platformUserId, MessageEnvelope<?> message);

    /**
     * Broadcasts a message to all active users.
     *
     * @param message The message envelope to broadcast.
     */
    void broadcast(MessageEnvelope<?> message);
}
