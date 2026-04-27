package com.notificationservice.domain;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * A generic wrapper for all messages sent through the notification service.
 *
 * @param <T> The type of the payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEnvelope<T> {

    /**
     * The type of the message, used by clients to determine how to process the payload.
     */
    private String type;

    /**
     * The message payload.
     */
    private T payload;
}
