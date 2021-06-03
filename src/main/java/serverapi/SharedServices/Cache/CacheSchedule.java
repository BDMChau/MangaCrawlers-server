package serverapi.SharedServices.Cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;


@Configuration
@EnableScheduling
public class CacheSchedule {


//    @CacheEvict(allEntries = true, value = {"weeklyMangas"})
//    @Scheduled(cron = "0 0 0 */7 * ?")
//    public void evictCacheWeeklyMangas() {
//        Calendar calendar = Calendar.getInstance();
//        System.err.println("Evict Cache weeklyMangas every 7 days: " + calendar.getTime());
//    }

//    @CacheEvict(allEntries = true, value = {"topMangas"})
//    @Scheduled(cron = "* * */23 * * ?")
//    public void evictCacheTopMangas() {
//        Calendar calendar = Calendar.getInstance();
//        System.err.println("Evict Cache topMangas every 23 hours: " + calendar.getTime());
//    }
}