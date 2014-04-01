package com.jonwelzel.ejb.session;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import redis.clients.jedis.Jedis;

import com.jonwelzel.ejb.RedisFactory;
import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.persistence.entities.AuthToken;
import com.jonwelzel.persistence.entities.User;

/**
 * Stateless session bean exposing HTTP session-related operations. The session information is all stored in Redis.
 * 
 * @author jwelzel
 * 
 */
@Stateless
@LocalBean
public class HttpSessionBean {

    // private final int SESSION_TIMEOUT = 900; // 15 min in seconds
    private final int SESSION_TIMEOUT = 180;

    @Inject
    @Log
    private Logger log;

    @EJB
    private RedisFactory jedisFactory;

    /**
     * Find the {@link User}'s {@link AuthToken} ID value using the session key associated to it.
     * 
     * @param sessionKey
     *            The session token stored in Redis that was generated when the user logged in.
     * @return The user's auth token id value associated to this session or null if the session expired (doesn't exist).
     */
    public String getUserId(String sessionKey) {
        Jedis jedis = jedisFactory.getResource();
        String result = null;
        try {
            result = jedis.get(sessionKey); // first check if exists
            if (result != null) {
                int ttl = jedis.ttl(sessionKey).intValue();
                if (ttl != -1) { // ttl will be the remaining seconds to live which means it expires
                    jedis.expire(sessionKey, SESSION_TIMEOUT); // renew timeout
                }
            }
        } catch (Throwable e) {
            log.error("Redis error!", e);
        } finally {
            if (jedis != null) {
                jedisFactory.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * Checks the validity of a given session. If the session has a TTL updates it with the value of
     * {@link #SESSION_TIMEOUT}.
     * 
     * @param key
     *            Session unique identifier.
     * @return -1 if the session with given key doesn't exist, 0 if session timed out or 1 if all is good.
     */
    public int validateSession(String key) {
        Jedis jedis = jedisFactory.getResource();
        int result = 1;
        try {
            String userId = jedis.get(key); // first check if exists
            if (userId == null) {
                result = -1; // does not exist
            } else {
                int ttl = jedis.ttl(key).intValue();
                if (ttl == -2) {
                    result = 0; // expired
                } else if (ttl != -1) { // ttl will be the remaining seconds to live which means it expires
                    jedis.expire(key, SESSION_TIMEOUT); // renew timeout
                }
            }
        } catch (Throwable e) {
            log.error("Redis error!", e);
        } finally {
            if (jedis != null) {
                jedisFactory.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * Create a new session for the user. The key is a secure random hash and the value is the secure random hash that
     * identifies the user, which is created automatically when his account is created.
     * 
     * @param key
     *            Secure random hash value that will be used to identify this session.
     * @param userId
     *            Secure random hash value that will be used to identify the user.
     * @param remember
     *            If true the session will have no expire date, if false it will have a {@link #SESSION_TIMEOUT}.
     */
    public void newSession(String key, String userId, boolean remember) {
        Jedis jedis = jedisFactory.getResource();
        try {
            jedis.set(key, userId);
            if (!remember) {
                jedis.expire(key, SESSION_TIMEOUT);
            }
        } catch (Throwable e) {
            log.error("Redis error!", e);
        } finally {
            if (jedis != null) {
                jedisFactory.returnResource(jedis);
            }
        }
    }

    /**
     * Remove a session entry from the cache.
     * 
     * @param key
     *            Session unique identifier.
     * @return -1 if error occurred, 0 if no session was expired or 1 if all went well.
     */
    public int destroySession(String key) {
        Jedis jedis = jedisFactory.getResource();
        int result = -1;
        try {
            result = jedis.del(key).intValue();
        } catch (Throwable e) {
            log.error("Redis error!", e);
        } finally {
            if (jedis != null) {
                jedisFactory.returnResource(jedis);
            }
        }
        return result;
    }

}
