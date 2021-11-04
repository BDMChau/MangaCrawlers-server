package serverapi.tables.manga_tables.chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.helpers.OffsetBasedPageRequest;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentTreesDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs;
import serverapi.query.dtos.tables.AuthorMangaDTO;
import serverapi.query.dtos.tables.ChapterDTO;
import serverapi.query.dtos.tables.ChapterImgDTO;
import serverapi.query.repository.manga.ChapterRepos;
import serverapi.query.repository.manga.ImgChapterRepos;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.query.repository.manga.comment.MangaCommentsRepos;
import serverapi.tables.manga_tables.manga.MangaService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChapterService {

    private final MangaRepos mangaRepository;
    private final ChapterRepos chapterRepos;
    private final ImgChapterRepos imgChapterRepos;
    private final MangaCommentsRepos mangaCommentsRepos;
    private final MangaService mangaService;


    @Autowired
    public ChapterService(MangaRepos mangaRepository, ChapterRepos chapterRepos, ImgChapterRepos imgChapterRepos, MangaCommentsRepos mangaCommentsRepos, MangaService mangaService) {

        this.mangaRepository = mangaRepository;
        this.chapterRepos = chapterRepos;
        this.imgChapterRepos = imgChapterRepos;
        this.mangaCommentsRepos = mangaCommentsRepos;
        this.mangaService = mangaService;
    }


    public ResponseEntity getAllChapter() {
        List<Chapter> chapters = chapterRepos.findAllChapter();

        Map<String, Object> msg = Map.of(
                "msg", "Get all chapters successfully!",
                "data", chapters
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity findImgByChapter(Long chapterId, Long mangaId) {
        Optional<Chapter> chapterInfo = chapterRepos.findById(chapterId);
        if (chapterInfo.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "No chapter to present!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        Long mangaIdOfChapter = chapterInfo.get().getManga().getManga_id();
        chapterInfo.get().getManga().getManga_name();


        if (mangaIdOfChapter.equals(mangaId)) {
            List<ChapterDTO> listChapter = chapterRepos.findChaptersbyMangaId(mangaId);
            listChapter.forEach(item ->{
                System.err.println(item.getChapter_name());
            });

            List<ChapterImgDTO> listImgs = imgChapterRepos.findImgsByChapterId(chapterId);
            Map<String, Object> msg = Map.of(
                    "msg", "Get all chapters successfully!",
                    "chapterInfo", chapterInfo,
                    "listChapter", listChapter,
                    "listImg", listImgs
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

        } else {
            Map<String, Object> err = Map.of(
                    "err", "No chapter to present!"
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(),
                    HttpStatus.ACCEPTED);
        }
    }

    public ResponseEntity getCommentsChapter(Long chapterId, int from, int amount) {

        // Initialize variable
        final String level1 = "1";
        final String level2 = "2";

        final Pageable pageable = new OffsetBasedPageRequest(from, amount);
        final Pageable childPageable = new OffsetBasedPageRequest(0, 5);

        // Check condition
        Optional<Chapter> chapterOptional = chapterRepos.findById(chapterId);
        if (chapterOptional.isEmpty()) {

            Map<String, Object> err = Map.of("err", "Chapter not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        // get manga comments in each level
        List<MangaCommentDTOs> cmtsLv0 = mangaCommentsRepos.getChapterCommentsLevel0(chapterId, pageable);
        if (cmtsLv0.isEmpty()) {

            Map<String, Object> msg = Map.of("msg", "No comments found!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }

        // Get comment
        //set tags for each comment
        List<MangaCommentDTOs> comments;
        cmtsLv0.forEach(lv0 ->{

            lv0 = mangaService.setListTags(lv0);

            //get child comments
            List<CommentTreesDTO> cmtsLv1 = mangaCommentsRepos.getCommentsChild(lv0.getManga_comment_id(), level1,childPageable);
            List<CommentTreesDTO> cmtsLv2 = mangaCommentsRepos.getCommentsChild(lv0.getManga_comment_id(), level2, childPageable);

            MangaCommentDTOs finalLv0 = lv0;
            cmtsLv1.forEach(lv01 ->{

                CommentTreesDTO finalLv01 = lv01;
                cmtsLv2.forEach(lv02 ->{

                    lv02 = mangaService.setListTags(lv02);

                    if(finalLv0.getManga_comment_id() == lv02.getParent_id()){

                        finalLv01.getComments_level_02().add(lv02);
                    }
                });

                lv01 = mangaService.setListTags(lv01);

                if(finalLv0.getManga_comment_id() == lv01.getParent_id()){

                    finalLv0.getComments_level_01().add(lv01);
                }
            });
        });
        comments = cmtsLv0;

        Map<String, Object> msg = Map.of(
                "msg", "Get chapter's comments successfully!",
                "chapter_info", chapterOptional,
                "don't use these param","manga_comment_relation_id, parent_id, child_id, level, manga_comment_tag_id",
                "comments", comments
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }

}
