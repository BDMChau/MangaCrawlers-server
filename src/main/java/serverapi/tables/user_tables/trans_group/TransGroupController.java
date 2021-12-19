package serverapi.tables.user_tables.trans_group;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.tables.manga_tables.manga.pojo.MangaInfoPOJO;
import serverapi.utils.UserHelpers;

import javax.servlet.ServletRequest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/trans_group")
@CacheConfig(cacheNames = {"trans_group"})
public class TransGroupController {
    private static final String fileNameDefault = "/static/media/8031DF085D7DBABC0F4B3651081CE70ED84622AE9305200F2FC1D789C95CF06F.9960248d.svg";
    private final TransGroupService transGroupService;
    private final UserHelpers userHelpers = new UserHelpers();

    public TransGroupController(TransGroupService transGroupService) {
        this.transGroupService = transGroupService;
    }


    @PutMapping("/update_manga")
    public ResponseEntity updateManga(ServletRequest request ,@RequestBody MangaInfoPOJO mangaInfoPOJO) throws NoSuchAlgorithmException {
        String StrUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);
        return transGroupService.updateManga(userId, mangaInfoPOJO);
    }


}