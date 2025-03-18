package com.web.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class VideoCache {
    private final Map<String, byte[]> videoCache = new ConcurrentHashMap<>();

    public void addToCache(String key, byte[] data) {
        videoCache.put(key, data);
    }

    public byte[] getFromCache(String key) {
        return videoCache.get(key);
    }

    public boolean isCached(String key) {
        return videoCache.containsKey(key);
    }
}
