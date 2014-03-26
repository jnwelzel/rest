package com.jonwelzel.ejb.session;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import redis.clients.jedis.Jedis;

import com.jonwelzel.ejb.RedisFactory;

/**
 * Stateless session bean exposing user session-related operations.
 * 
 * @author jwelzel
 * 
 */
@Stateless
@LocalBean
public class SessionBean {

    private final int SESSION_TIMEOUT = 900; // 15 min in seconds

    @Inject
    private Logger log;

    @EJB
    private RedisFactory jedisFactory;

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
