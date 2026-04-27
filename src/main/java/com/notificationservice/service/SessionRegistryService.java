package com.notificationservice.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages a thread-safe, in-memory registry of active user sessions.
 */
@Service
public class SessionRegistryService {

    private final ConcurrentHashMap<String, Set<String>> userSessions = new ConcurrentHashMap<>();

    /**
     * Registers a new session for a user.
     *
     * @param platformUserId The user's unique identifier.
     * @param sessionId      The new session identifier.
     */
    public void registerSession(String platformUserId, String sessionId) {
        userSessions.computeIfAbsent(platformUserId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
    }

    /**
     * Removes a session for a user.
     *
     * @param platformUserId The user's unique identifier.
     * @param sessionId      The session identifier to remove.
     */
    public void removeSession(String platformUserId, String sessionId) {
        userSessions.computeIfPresent(platformUserId, (k, v) -> {
            v.remove(sessionId);
            return v.isEmpty() ? null : v;
        });
    }

    /**
     * Retrieves all active session IDs for a given user.
     *
     * @param platformUserId The user's unique identifier.
     * @return A set of active session IDs, or an empty set if the user has no sessions.
     */
    public Set<String> getActiveSessions(String platformUserId) {
        return userSessions.getOrDefault(platformUserId, Collections.emptySet());
    }
}
