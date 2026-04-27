package com.notificationservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SessionRegistryService}.
 */
class SessionRegistryServiceTest {

    private SessionRegistryService sessionRegistryService;

    @BeforeEach
    void setUp() {
        sessionRegistryService = new SessionRegistryService();
    }

    @Test
    void registerSession_shouldAddNewUserAndSession() {
        sessionRegistryService.registerSession("user1", "session1");
        Set<String> sessions = sessionRegistryService.getActiveSessions("user1");
        assertThat(sessions).containsExactly("session1");
    }

    @Test
    void registerSession_shouldAddSessionToExistingUser() {
        sessionRegistryService.registerSession("user1", "session1");
        sessionRegistryService.registerSession("user1", "session2");
        Set<String> sessions = sessionRegistryService.getActiveSessions("user1");
        assertThat(sessions).containsExactlyInAnyOrder("session1", "session2");
    }

    @Test
    void removeSession_shouldRemoveSessionFromUser() {
        sessionRegistryService.registerSession("user1", "session1");
        sessionRegistryService.registerSession("user1", "session2");
        sessionRegistryService.removeSession("user1", "session1");
        Set<String> sessions = sessionRegistryService.getActiveSessions("user1");
        assertThat(sessions).containsExactly("session2");
    }

    @Test
    void removeSession_shouldRemoveUserIfLastSessionIsRemoved() {
        sessionRegistryService.registerSession("user1", "session1");
        sessionRegistryService.removeSession("user1", "session1");
        Set<String> sessions = sessionRegistryService.getActiveSessions("user1");
        assertThat(sessions).isEmpty();
    }

    @Test
    void getActiveSessions_shouldReturnEmptySetForUnknownUser() {
        Set<String> sessions = sessionRegistryService.getActiveSessions("unknown_user");
        assertThat(sessions).isEmpty();
    }

    @Test
    void testConcurrency() throws InterruptedException {
        final int numThreads = 10;
        final int operationsPerThread = 1000;
        final String userId = "concurrentUser";
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        sessionRegistryService.registerSession(userId, "session-" + threadId + "-" + j);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        Set<String> sessions = sessionRegistryService.getActiveSessions(userId);
        assertThat(sessions).hasSize(numThreads * operationsPerThread);
    }
}
