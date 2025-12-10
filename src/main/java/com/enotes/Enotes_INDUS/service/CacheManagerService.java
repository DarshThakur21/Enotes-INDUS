package com.enotes.Enotes_INDUS.service;

import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.List;

public interface CacheManagerService {

    Collection<String> getCache();

    Cache getCacheFromName(String cacheName);

    void removeAllCache();
}
