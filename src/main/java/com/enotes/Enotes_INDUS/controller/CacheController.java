package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.endpoints.CacheEndpoint;
import com.enotes.Enotes_INDUS.service.CacheManagerService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController

public class CacheController implements CacheEndpoint {

    @Autowired
    private CacheManagerService cacheManagerService;

    @Override
    public ResponseEntity<?> getAllCaches() {
        Collection<String> cacheList=cacheManagerService.getCache();
        return CommonUtil.createBuildResponse(cacheList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCache(String cacheName) {
        Cache cache=cacheManagerService.getCacheFromName(cacheName);
        return CommonUtil.createBuildResponse(cache, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> removeCache(String cacheName) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeAll() {
        cacheManagerService.removeAllCache();
        return CommonUtil.createBuildResponseMessage("All clear Success",HttpStatus.OK);
    }



}
