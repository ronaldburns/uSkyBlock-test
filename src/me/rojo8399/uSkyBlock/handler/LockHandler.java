package me.rojo8399.uSkyBlock.handler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows programmatic locking of access to the USB (used during initialization).
 */
public class LockHandler {
    private final Map<UUID, Long> locks = new ConcurrentHashMap<>();
}
