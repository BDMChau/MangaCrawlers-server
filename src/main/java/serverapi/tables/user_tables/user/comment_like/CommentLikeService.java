package serverapi.tables.user_tables.user.comment_like;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.repository.manga.comment.CommentLikesRepos;
import serverapi.query.repository.manga.comment.MangaCommentsRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.tables.manga_tables.manga_comment.manga_comment_likes.CommentLikes;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;
import serverapi.tables.user_tables.user.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentLikeService {

    private final CommentLikesRepos commentLikesRepos;
    private final MangaCommentsRepos mangaCommentsRepos;
    private final UserRepos userRepos;

    public CommentLikeService(CommentLikesRepos commentLikesRepos, MangaCommentsRepos mangaCommentsRepos, UserRepos userRepos) {
        this.commentLikesRepos = commentLikesRepos;
        this.mangaCommentsRepos = mangaCommentsRepos;
        this.userRepos = userRepos;
    }

    public ResponseEntity checkUserLike(Long userID, Long commentID) {
        int likeStatus = 0;
        String sLikeStatus = "";
        Optional<CommentLikes> commentLikeOptional = commentLikesRepos.getCommentLike(commentID, userID);

        if (commentLikeOptional.isEmpty()) {
            likeStatus = 1;
        }
        switch (likeStatus) {
            case 0 -> sLikeStatus = "user have liked in this comment!";
            case 1 -> sLikeStatus = "user still not like in this comment!";
        }

        Map<String, Object> msg = Map.of(
                "msg", "Check like successfully!",
                "status_number",likeStatus,
                "status",sLikeStatus
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getTotalLike(Long userID, Long commentID) {

        Optional<MangaComments> mangaCommentOptional = mangaCommentsRepos.findById(commentID);
        if (mangaCommentOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Comment not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get total like successfully!",
                "total_like",mangaCommentOptional.get().getCount_like()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity addLike(Long userID, Long commentID) {

        Optional<MangaComments> mangaCommentsOptional = mangaCommentsRepos.findById(commentID);
        Optional<User> userOptional = userRepos.findById(userID);
        if (mangaCommentsOptional.isEmpty() || userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Comment or user is not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        CommentLikes commentLikes = new CommentLikes();
        commentLikes.setManga_comment(mangaCommentsOptional.get());
        commentLikes.setUser(userOptional.get());
        commentLikesRepos.saveAndFlush(commentLikes);

        Map<String, Object> msg = Map.of(
                "msg", "Like successfully!"
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity unLike(Long userID, Long commentID) {

        Optional<CommentLikes> commentLikesOptional = commentLikesRepos.getCommentLike(userID,commentID);
        if (commentLikesOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Like is not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        commentLikesRepos.delete(commentLikesOptional.get());

        Map<String, Object> msg = Map.of(
                "msg", "Unlike successfully!"
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}