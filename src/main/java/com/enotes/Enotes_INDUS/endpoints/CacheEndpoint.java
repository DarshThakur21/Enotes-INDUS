package com.enotes.Enotes_INDUS.endpoints;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Cache List",description = "Info about all the cache related apis")
@RequestMapping("/api/v1/caches")
public interface CacheEndpoint {

    @GetMapping("/")
     ResponseEntity<?> getAllCaches();

    @GetMapping("/{cacheName}")
     ResponseEntity<?> getCache(@PathVariable  String cacheName);

    @DeleteMapping("/remove/{cacheName}")
     ResponseEntity<?> removeCache(@PathVariable  String cacheName);

    @DeleteMapping("/removeAll")
    ResponseEntity<?> removeAll();




}
