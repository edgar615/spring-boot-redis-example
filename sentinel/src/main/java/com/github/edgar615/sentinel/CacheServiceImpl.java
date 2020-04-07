package com.github.edgar615.sentinel;

import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by Edgar on 2018/6/20.
 *
 * @author Edgar  Date 2018/6/20
 */
@Service
public class CacheServiceImpl implements CacheService {

  @Override
  @Cacheable(cacheNames = "serviceOne_cache", key = "#p0")
  public String cache(int i) {
    System.out.println("cache");
    return i + "";
  }

  @Override
  @CacheEvict(cacheNames = "serviceOne_cache", key = "#p0")
  public void clearCache(int i) {
    System.out.println("clearCache");
  }

}
