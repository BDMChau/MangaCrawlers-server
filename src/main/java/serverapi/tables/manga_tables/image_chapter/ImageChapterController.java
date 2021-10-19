package serverapi.tables.manga_tables.image_chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.api.Response;

import java.util.Map;

@RestController
@RequestMapping("/api/image_chapter")
@CacheConfig(cacheNames = {"image_chapter"})
public class ImageChapterController {

    private final ImageChapterService imageChapterService;

    @Autowired
    public ImageChapterController(ImageChapterService imageChapterService) {
        this.imageChapterService = imageChapterService;
    }

    @GetMapping("/add_images")
    public ResponseEntity addImages(@RequestParam String chapter_id) {

        if(chapter_id.isEmpty()){
            Map<String, Object> msg = Map.of("err", "missing credential");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        return imageChapterService.addImages(Long.parseLong(chapter_id));
    }
}
