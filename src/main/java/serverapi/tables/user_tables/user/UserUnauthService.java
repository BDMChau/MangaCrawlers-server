package serverapi.tables.user_tables.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.helpers.OffsetBasedPageRequest;
import serverapi.query.dtos.tables.PostUserDTO;
import serverapi.query.dtos.tables.UserDTO;
import serverapi.query.repository.forum.PostRepos;
import serverapi.query.repository.manga.*;
import serverapi.query.repository.manga.comment.*;
import serverapi.query.repository.user.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserUnauthService {
    private final MangaRepos mangaRepository;
    private final FollowingRepos followingRepos;
    private final UserRepos userRepos;
    private final ReadingHistoryRepos readingHistoryRepos;
    private final ChapterRepos chapterRepos;
    private final MangaCommentsRepos mangaCommentsRepos;
    private final RatingMangaRepos ratingMangaRepos;
    private final TransGroupRepos transGroupRepos;
    private final GenreRepos genreRepos;
    private final MangaGenreRepos mangaGenreRepos;
    private final AuthorRepos authorRepos;
    private final ImgChapterRepos imgChapterRepos;
    private final CommentRelationRepos commentRelationRepos;
    private final CommentImageRepos commentImageRepos;
    private final CommentTagsRepos commentTagsRepos;
    private final CommentLikesRepos commentLikesRepos;
    private final PostRepos postRepos;
    private final NotificationRepos notificationRepos;

    @Autowired
    public UserUnauthService(MangaRepos mangaRepository, FollowingRepos followingRepos, UserRepos userRepos,
                       ReadingHistoryRepos readingHistoryRepos, ChapterRepos chapterRepos,
                       MangaCommentsRepos mangaCommentsRepos, RatingMangaRepos ratingMangaRepos,
                       TransGroupRepos transGroupRepos, GenreRepos genreRepos, MangaGenreRepos mangaGenreRepos,
                       AuthorRepos authorRepos, ImgChapterRepos imgChapterRepos, CommentRelationRepos commentRelationRepos,
                       CommentImageRepos commentImageRepos, PostRepos postRepos,CommentTagsRepos commentTagsRepos, CommentLikesRepos commentLikesRepos, NotificationRepos notificationRepos) {
        this.mangaRepository = mangaRepository;
        this.followingRepos = followingRepos;
        this.userRepos = userRepos;
        this.readingHistoryRepos = readingHistoryRepos;
        this.chapterRepos = chapterRepos;
        this.mangaCommentsRepos = mangaCommentsRepos;
        this.ratingMangaRepos = ratingMangaRepos;
        this.transGroupRepos = transGroupRepos;
        this.genreRepos = genreRepos;
        this.mangaGenreRepos = mangaGenreRepos;
        this.authorRepos = authorRepos;
        this.imgChapterRepos = imgChapterRepos;
        this.commentRelationRepos = commentRelationRepos;
        this.commentImageRepos = commentImageRepos;
        this.commentTagsRepos = commentTagsRepos;
        this.commentLikesRepos = commentLikesRepos;
        this.postRepos = postRepos;
        this.notificationRepos = notificationRepos;
    }



    ////////////////////////////////////// unauthenticated parts //////////////////////////////////////
    protected ResponseEntity getUserInfo(Long userId) {
        Optional<UserDTO> userOptional = userRepos.findByUserId(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User does not exist!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        UserDTO user = userOptional.get();

        // user's posts

        Map<String, Object> msg = Map.of(
                "msg", "get user info OK!",
                "user", user
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    protected ResponseEntity getPostOfUser(Long userId, int from, int amount) {
        Optional<UserDTO> userOptional = userRepos.findByUserId(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User does not exist!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        UserDTO user = userOptional.get();

        // user's posts
        Pageable pageable = new OffsetBasedPageRequest(from, amount);
        List<PostUserDTO> posts = postRepos.getPostsByUserId(userId, pageable);


        Map<String, Object> msg = Map.of(
                "msg", "get user info OK!",
                "from", from + amount,
                "posts", posts
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


}
