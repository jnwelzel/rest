package com.jonwelzel.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Singleton EJB that holds the connection pool for Redis.
 * 
 * @author jwelzel
 * 
 */
@Singleton
@Startup
public class RedisFactory {

    private JedisPool pool;
    private JedisPoolConfig cfg;
    private RedisFactory factory;

    @PostConstruct
    public void init() {
        pool = new JedisPool(getConfig(), "localhost");
    }

    public JedisPoolConfig getConfig() {
        if (cfg == null) {
            cfg = new JedisPoolConfig();
            // TODO Research what useful settings could be made use of here
        }
        return cfg;
    }

    public Jedis getResource() {
        if (factory == null) {
            factory = new RedisFactory();
        }
        return pool.getResource();
    }

    public void returnResource(Jedis resource) {
        pool.returnResource(resource);
    }

}
