package serverapi.SharedServices.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@EnableCaching
@Service
public class CacheService {

    @Autowired
    CacheManager cacheManager;


    public void evictSingleCacheValue(String cacheName, String cacheKey) {
        cacheManager.getCache(cacheName).evict(cacheKey);
    }


    public void evictAllCacheValues(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }
}
