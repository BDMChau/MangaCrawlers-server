package serverapi.tables.user_tables.trans_group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.dtos.tables.ChapterImgDTO;
import serverapi.query.repository.manga.AuthorRepos;
import serverapi.query.repository.manga.ChapterRepos;
import serverapi.query.repository.manga.ImgChapterRepos;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.query.repository.user.TransGroupRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.tables.manga_tables.author.Author;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.image_chapter.ImageChapter;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.manga_tables.manga.pojo.MangaInfoPOJO;
import serverapi.tables.user_tables.user.user.UserService;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TransGroupService {
    private final UserRepos userRepos;
    private final TransGroupRepos transGroupRepos;
    private final MangaRepos mangaRepos;
    private final AuthorRepos authorRepos;
    private final UserService userService;
    private final ChapterRepos chapterRepos;
    private final ImgChapterRepos imgChapterRepos;

    @Autowired
    public TransGroupService(UserRepos userRepos, TransGroupRepos transGroupRepos, MangaRepos mangaRepos, AuthorRepos authorRepos, UserService userService, ChapterRepos chapterRepos, ImgChapterRepos imgChapterRepos) {
        this.userRepos = userRepos;
        this.transGroupRepos = transGroupRepos;
        this.mangaRepos = mangaRepos;
        this.authorRepos = authorRepos;
        this.userService = userService;
        this.chapterRepos = chapterRepos;
        this.imgChapterRepos = imgChapterRepos;
    }

    public ResponseEntity updateManga(MangaInfoPOJO mangaInfoPOJO) {
        String mangaName = mangaInfoPOJO.getManga_name();
        String thumbnail = mangaInfoPOJO.getThumbnail();
        String description = mangaInfoPOJO.getDescription();
        String status = mangaInfoPOJO.getStatus();
        String authorName = mangaInfoPOJO.getManga_authorName();
        if (!authorName.equals(mangaInfoPOJO.getAuthor().getAuthor_name())) {
            Author newAuthor = new Author();
            newAuthor.setAuthor_name(authorName);
            authorRepos.saveAndFlush(newAuthor);
        } else {
            authorName = mangaInfoPOJO.getAuthor().getAuthor_name();
        }
        Optional<Author> authorOptional = authorRepos.findAuthorByName(authorName);
        if (mangaName.isEmpty() || thumbnail.isEmpty()
            || description.isEmpty() || status.isEmpty() || authorOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Missing credential!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        Author author = authorOptional.get();
        Long mangaId = Long.valueOf(mangaInfoPOJO.getManga_id());
        Long transGroupId = mangaInfoPOJO.getTransGroup().getTransgroup_id();
        Optional<Manga> mangaOptional = transGroupRepos.findMangaByTransIdaAndMangaId(transGroupId, mangaId);
        if (mangaOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Manga not found!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        ////////////////////////
        Manga manga = mangaOptional.get();
        manga.setAuthor(author);
        manga.setThumbnail(thumbnail);
        manga.setManga_name(mangaName);
        manga.setDescription(description);
        manga.setStatus(status);
        mangaRepos.save(manga);

        return userService.getMangaInfoUploadPage(transGroupId, mangaId);
    }

    @Transactional
    public ResponseEntity updateChapter(Map chapter, Long mangaId, List listImg) {
        if (chapter != null && mangaId != null) {
            Long chapterId = Long.parseLong(String.valueOf(chapter.get("chapter_id")));
            String newChapterName = (String) chapter.get("chapter_name");
            Optional<Chapter> chapterOptional = chapterRepos.findChapterByMangaIdAndChapterId(chapterId, mangaId);
            if (chapterOptional.isPresent()) {
                Chapter updateChapter = chapterOptional.get();
                updateChapter.setChapter_name(newChapterName);
                chapterRepos.saveAndFlush(updateChapter);

                List<ImageChapter> imageChapterList = imgChapterRepos.findImagesByChapterId(chapterId);
                if(!imageChapterList.isEmpty()){
                    int i = 0;
                    while (i <listImg.size()) {
                        for (ImageChapter image : imageChapterList) {
                            HashMap img = (HashMap) listImg.get(i);
                            image.setImgchapter_url((String) img.get("img_url"));
                            imgChapterRepos.saveAndFlush(image);
                            i++;
                            break;
                        }
                    }
                }
                List<ChapterImgDTO> exportImages = imgChapterRepos.findImgsByChapterId(chapterId);
                if (!imageChapterList.isEmpty()) {
                    Map<String, Object> msg = Map.of(
                            "msg", "Update chapter successfully!",
                            "list_img", exportImages
                    );
                    return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
                }
            }
        }
        Map<String, Object> err = Map.of("err", "Chapter not found!");
        return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
    }
}
