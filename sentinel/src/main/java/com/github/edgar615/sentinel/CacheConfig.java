package com.github.edgar615.sentinel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import org.springframework.cache.annotation.EnableCaching;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheConfiguration;

import org.springframework.data.redis.cache.RedisCacheManager;

import org.springframework.data.redis.connection.RedisPassword;

import org.springframework.data.redis.connection.RedisSentinelConfiguration;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Autowired
    private CacheSettings cacheSettings;

    @Autowired
    private AppCacheSettings appCacheSettings;

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(redisProperties.getSentinel().getMaster());
        // 偷懒
        redisProperties.getSentinel().getNodes().forEach(s -> sentinelConfig.sentinel(s.substring(0,12), Integer.valueOf(s.substring(13))));
//        sentinelConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));

        return new LettuceConnectionFactory(sentinelConfig);
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
                .disableCachingNullValues();
    }

    private RedisCacheConfiguration buildRedisCacheConfig(CacheSettingsModel cachesProperties) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(Long.parseLong(cachesProperties.getTimeToLiveSeconds())))
                .disableCachingNullValues();
    }

    @Bean
    public RedisCacheManager cacheManager() {
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        Map<String, CacheSettingsModel> cacheConfigMap = cacheSettings.getCacheConfigAsMap();
        Map<String, String> appCacheMap = appCacheSettings.getAppCacheMap();
        appCacheMap.forEach((key, value) -> cacheConfigs.put(key, buildRedisCacheConfig(cacheConfigMap.get(value))));
        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfiguration())
                .withInitialCacheConfigurations(cacheConfigs)
                .transactionAware()
                .build();
    }

}