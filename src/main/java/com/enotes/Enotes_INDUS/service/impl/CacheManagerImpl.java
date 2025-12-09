package com.enotes.Enotes_INDUS.service.impl;


import com.enotes.Enotes_INDUS.service.CacheManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class CacheManagerImpl implements CacheManagerService {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Collection<String> getCache(){
        Collection<String> cacheNamesList= cacheManager.getCacheNames();
        for(String cacheName: cacheNamesList){
            Cache cache= cacheManager.getCache(cacheName);
            log.info("Cache Name : {}",cache);
        }
        return cacheNamesList;
    }

    @Override
    public Cache getCacheFromName(String cacheName) {
       Cache cache=cacheManager.getCache(cacheName);
        log.info("Cache Name : {}",cache);
       return cache;

    }
    @Override
    public void removeAllCache(){

        Collection<String> cacheNamesList=  cacheManager.getCacheNames();
        for(String cacheName: cacheNamesList){
        Cache cache= cacheManager.getCache(cacheName);
        log.info("Cache Name : {}",cache);
        cache.clear();
        }
    }

}
