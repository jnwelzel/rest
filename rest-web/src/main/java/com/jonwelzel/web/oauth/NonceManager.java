package com.jonwelzel.web.oauth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Tracks the nonces for a given consumer key and/or token. Automagically ensures timestamp is monotonically increasing
 * and tracks all nonces for a given timestamp.
 * 
 * @author Paul C. Bryan
 * @author Martin Matula (martin.matula at oracle.com)
 * @author Thomas Meire
 */
final class NonceManager {
    /**
     * The maximum valid age of a nonce timestamp, in milliseconds.
     */
    private final long maxAge;

    /**
     * Verifications to perform on average before performing garbage collection.
     */
    private final int gcPeriod;

    /**
     * Counts number of verification requests performed to schedule garbage collection.
     */
    private int gcCounter = 0;

    private final TimeUnit timestampUnit;

    private final long maximumMapSize;

    /**
     * Maps timestamps to key-nonce pairs.
     */
    private final SortedMap<Long, Map<String, Set<String>>> tsToKeyNoncePairs = new TreeMap<Long, Map<String, Set<String>>>();

    private volatile long mapSize = 0;

    /**
     * Create a new nonce manager configured with maximum age, old nonce cleaning period and a time unit of timestamps.
     * 
     * @param maxAge
     *            the maximum valid age of a nonce timestamp, in milliseconds.
     * @param gcPeriod
     *            number of verifications to be performed on average before performing garbage collection of old nonces.
     * @param timestampUnit
     *            unit in which timestamps are passed to {@link #verify(String, String, String)} method.
     * @param maximumCacheSize
     *            maximum size of the cache that keeps nonces. If the cache exceeds the method
     *            {@link #verify(String, String, String)} will return {@code false}.
     */
    public NonceManager(long maxAge, int gcPeriod, TimeUnit timestampUnit, long maximumCacheSize) {
        if (maxAge <= 0 || gcPeriod <= 0) {
            throw new IllegalArgumentException();
        }

        this.maxAge = maxAge;
        this.gcPeriod = gcPeriod;
        this.timestampUnit = timestampUnit;
        this.maximumMapSize = maximumCacheSize;
    }

    public NonceManager() {
        this.maxAge = 300000;
        this.gcPeriod = 100;
        this.timestampUnit = TimeUnit.SECONDS;
        this.maximumMapSize = 2000000;
    }

    /**
     * Evaluates the timestamp/nonce combination for validity, storing and/or clearing nonces as required.
     * <p>
     * The method is package private in order to be used in unit tests only.
     * </p>
     * 
     * @param key
     *            the oauth_consumer_key value for a given consumer request
     * @param timestamp
     *            the oauth_timestamp value for a given consumer request (in milliseconds).
     * @param nonce
     *            the oauth_nonce value for a given consumer request.
     * @param now
     *            current time in milliseconds
     * @return true if the timestamp/nonce are valid.
     */
    synchronized boolean verify(String key, String timestamp, String nonce, long now) {
        // convert timestamp to milliseconds since epoch to deal with uniformly
        long stamp = timestampUnit.toMillis(longValue(timestamp));

        if (mapSize + 1 > maximumMapSize) {
            gc(now);
            if (mapSize + 1 > maximumMapSize) {
                // cannot keep another nonce (prevents exhausting memory)
                return false;
            }
        }

        // invalid timestamp supplied; automatically invalid
        if (stamp + maxAge < now || stamp - maxAge > now) {
            return false;
        }

        Map<String, Set<String>> keyToNonces = tsToKeyNoncePairs.get(stamp);
        if (keyToNonces == null) {
            keyToNonces = new HashMap<String, Set<String>>();
            tsToKeyNoncePairs.put(stamp, keyToNonces);
        }

        Set<String> nonces = keyToNonces.get(key);
        if (nonces == null) {
            nonces = new HashSet<String>();
            keyToNonces.put(key, nonces);
        }

        boolean result = nonces.add(nonce);
        if (result) {
            mapSize++;
        }

        // perform garbage collection if counter is up to established number of passes
        if (++gcCounter >= gcPeriod) {
            gc(now);
        }

        // returns false if nonce already encountered for given timestamp
        return result;
    }

    /**
     * Evaluates the timestamp/nonce combination for validity, storing and/or clearing nonces as required.
     * 
     * @param key
     *            the oauth_consumer_key value for a given consumer request
     * @param timestamp
     *            the oauth_timestamp value for a given consumer request (in milliseconds).
     * @param nonce
     *            the oauth_nonce value for a given consumer request.
     * @return true if the timestamp/nonce are valid.
     */
    public synchronized boolean verify(String key, String timestamp, String nonce) {
        return verify(key, timestamp, nonce, System.currentTimeMillis());
    }

    /**
     * Deletes all nonces older than maxAge. This method is package private (instead of private) for testability
     * purposes.
     * 
     * @param now
     *            milliseconds since epoch representing "now"
     */
    void gc(long now) {
        gcCounter = 0;
        final SortedMap<Long, Map<String, Set<String>>> headMap = tsToKeyNoncePairs.headMap(now - maxAge);
        for (Map.Entry<Long, Map<String, Set<String>>> entry : headMap.entrySet()) {
            for (Map.Entry<String, Set<String>> timeEntry : entry.getValue().entrySet()) {
                mapSize -= timeEntry.getValue().size();
            }
        }

        headMap.clear();
    }

    /**
     * Returns number of currently tracked timestamp-key-nonce tuples. The method should be used by tests only.
     * 
     * @return number of currently tracked timestamp-key-nonce tuples.
     */
    long checkAndGetSize() {
        long size = 0;
        for (Map<String, Set<String>> keyToNonces : tsToKeyNoncePairs.values()) {
            size += keyToNonces.values().size();
        }
        assert mapSize == size;
        return mapSize;
    }

    private static long longValue(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }
}
