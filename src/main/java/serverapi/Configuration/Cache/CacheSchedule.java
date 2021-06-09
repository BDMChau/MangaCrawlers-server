package serverapi.Configuration.Cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;


@Configuration
@EnableScheduling
@EnableAsync
public class CacheSchedule {


//    @Async
//    @CacheEvict(allEntries = true, value = {"weeklyMangas"})
//    @Scheduled(cron = "0 0 0 */7 * ?")
//    public void evictCacheWeeklyMangas() {
//        Calendar calendar = Calendar.getInstance();
//        System.err.println("Evict Cache weeklyMangas every 7 days: " + calendar.getTime());
//    }


//    @Async
//    @CacheEvict(allEntries = true, value = {"topMangas"})
//    @Scheduled(fixedRate = 86400000)
//    public void evictCacheTopMangas() {
//        Calendar calendar = Calendar.getInstance();
//        System.err.println("Evict Cache topMangas every 24 hours: " + calendar.getTime());
//    }


//    @Async
//    @CacheEvict(allEntries = true, value = {"dailyMangas"})
//    @Scheduled(fixedRate = 86400000)
//    public void evictCacheDailyMangas() {
//        Calendar calendar = Calendar.getInstance();
//        System.err.println("Evict Cache dailyMangas every 24 hours: " + calendar.getTime());
//    }


}