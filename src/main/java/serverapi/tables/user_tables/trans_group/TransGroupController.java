package serverapi.tables.user_tables.trans_group;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.api.Response;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.manga_tables.manga.pojo.MangaInfoPOJO;
import serverapi.utils.UserHelpers;

import javax.servlet.ServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (userHelpers.getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page|");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        return transGroupService.updateManga(mangaInfoPOJO);
    }

    @PutMapping("/update_chapter")
    public ResponseEntity updateChapter(ServletRequest request ,@RequestBody Map data) throws NoSuchAlgorithmException {
        if (userHelpers.getUserAttribute(request).get("user_transgroup_id") == null) {
            Map<String, String> error = Map.of("err", "Login again before visit this page|");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Chapter chapter = (Chapter) data.get("chapter");
        Manga manga = (Manga) data.get("manga");
        List listImg = (List) data.get("list_img");

        return transGroupService.updateChapter(chapter, manga, listImg);
    }


}