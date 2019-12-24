package com.ning.service;

/**
 * author JayNing
 * created by 2019/12/24 11:59
 **/
public interface CacheService {

    public String cacheData(Long id);

    void flushCacheByAnnotation1(Long l);
}
