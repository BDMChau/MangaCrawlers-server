package serverapi.Tables.MangaTables.Chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Helpers.OffsetBasedPageRequest;
import serverapi.Query.DTO.*;
import serverapi.Query.Repository.ChapterCommentsRepos;
import serverapi.Query.Repository.ChapterRepos;
import serverapi.Query.Repository.ImgChapterRepos;
import serverapi.Query.Repository.MangaRepos;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChapterService {

    private final ChapterRepos chapterRepos;
    private final ImgChapterRepos imgChapterRepos;
    private final MangaRepos mangaRepos;
    private final ChapterCommentsRepos chapterCommentsRepos;

    @Autowired
    public ChapterService(ChapterRepos chapterRepos, ImgChapterRepos imgChapterRepos, MangaRepos mangaRepos, ChapterCommentsRepos chapterCommentsRepos) {
        this.chapterRepos = chapterRepos;
        this.imgChapterRepos = imgChapterRepos;
        this.mangaRepos = mangaRepos;
        this.chapterCommentsRepos= chapterCommentsRepos;
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

//    public ResponseEntity getCommentsChapter(Long chapterId, int amount, int from) {
//
//        //get list comments in 1 chapter
//        List<ChapterCommentsDTO> chapterCommentsDTOList = chapterCommentsRepos.getCommentsChapter (chapterId, amount, from);
//
//        if (chapterCommentsDTOList.isEmpty()) {
//            Map<String, Object> msg = Map.of("msg", "No comment found!");
//            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
//        }
//
//
//        Map<String, Object> msg = Map.of(
//                "msg", "Get chapter comment successfully!",
//                "Info", chapterCommentsDTOList
//
//        );
//        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
//    }

    public ResponseEntity getCommentsChapter(Long mangaId, Long chapterId, int amount, int from) {


        //check chapterId is empty
        Optional<Chapter> chapterOptional =chapterRepos.findById (chapterId);
        if(chapterOptional.isEmpty ()){
            Map<String, Object> msg = Map.of("msg", "Chapter not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        //check mangaId of this chapter != mangaId input
        Long mangaIdofChapter = chapterOptional.get ().getManga ().getManga_id ();
        if(mangaIdofChapter != mangaId){
            Map<String, Object> msg = Map.of("msg", "This manga doesn't have this chapter!");
            return new ResponseEntity<>(new Response(404, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        //get list comments in 1 chapter
        Pageable pageable = new OffsetBasedPageRequest (from, amount);
        System.out.println ("pageable "+pageable.getPageNumber ());
        List<CommentExportDTO> commentExportDTOS = chapterCommentsRepos.getCommentsChapter (chapterId, pageable);

        if (commentExportDTOS.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No comments found!");
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get chapter comment successfully!",
                "comments", commentExportDTOS

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

}
