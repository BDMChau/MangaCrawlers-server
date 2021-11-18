package serverapi.tables.manga_tables.chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.dtos.tables.ChapterDTO;
import serverapi.query.dtos.tables.ChapterImgDTO;
import serverapi.query.repository.manga.ChapterRepos;
import serverapi.query.repository.manga.ImgChapterRepos;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.query.repository.manga.comment.CommentRepos;
import serverapi.tables.manga_tables.manga.MangaService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChapterService {

    private final MangaRepos mangaRepository;
    private final ChapterRepos chapterRepos;
    private final ImgChapterRepos imgChapterRepos;
    private final CommentRepos commentRepos;
    private final MangaService mangaService;


    @Autowired
    public ChapterService(MangaRepos mangaRepository, ChapterRepos chapterRepos, ImgChapterRepos imgChapterRepos, CommentRepos commentRepos, MangaService mangaService) {

        this.mangaRepository = mangaRepository;
        this.chapterRepos = chapterRepos;
        this.imgChapterRepos = imgChapterRepos;
        this.commentRepos = commentRepos;
        this.mangaService = mangaService;
    }


    protected ResponseEntity getAllChapter() {
        List<Chapter> chapters = chapterRepos.findAllChapter();

        Map<String, Object> msg = Map.of(
                "msg", "Get all chapters successfully!",
                "data", chapters
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    protected ResponseEntity findImgByChapter(Long chapterId, Long mangaId) {
        Optional<Chapter> chapterInfo = chapterRepos.findById(chapterId);
        if (chapterInfo.isEmpty()) {
            Map<String, Object> err = Map.of("err", "No chapter to present!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        Chapter chapter = chapterInfo.get();

        List<ChapterImgDTO> listImgs = imgChapterRepos.findImgsByChapterId(chapterId);

        if (mangaIdOfChapter.equals(mangaId)) {
            List<ChapterDTO> listChapter = chapterRepos.findChaptersbyMangaId(mangaId);
            listChapter.forEach(item -> {
                System.err.println(item.getChapter_name());
            });

        Map<String, Object> msg = Map.of(
                "msg", "Get all chapters successfully!",
                "chapterInfo", chapter,
                "listImg", listImgs
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }



    protected ResponseEntity getTotalChapters(Long mangaId) {
        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
        if (mangaOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "No manga!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }

        Long totalChapters = chapterRepos.getTotalChaptersByMangaId(mangaId);
        List<ChapterDTO> listChapter = chapterRepos.findChaptersbyMangaId(mangaId);


        Map<String, Object> msg = Map.of(
                "msg", "get total chapters OK!",
                "manga", mangaOptional.get(),
                "chapters", listChapter,
                "total", totalChapters
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
