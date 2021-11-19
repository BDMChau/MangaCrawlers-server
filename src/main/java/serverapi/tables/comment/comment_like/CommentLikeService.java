package serverapi.tables.comment.comment_like;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.repository.manga.comment.CommentLikesRepos;
import serverapi.query.repository.manga.comment.CommentRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.tables.comment.comment.Comment;
import serverapi.tables.user_tables.user.User;

import java.util.Map;
import java.util.Optional;

@Service
public class CommentLikeService {

    private final CommentLikesRepos commentLikesRepos;
    private final CommentRepos commentRepos;
    private final UserRepos userRepos;

    public CommentLikeService(CommentLikesRepos commentLikesRepos, CommentRepos commentRepos, UserRepos userRepos) {
        this.commentLikesRepos = commentLikesRepos;
        this.commentRepos = commentRepos;
        this.userRepos = userRepos;
    }

    public ResponseEntity checkUserLike(Long userID, Long commentID) {
        int likeStatus = 1;
        String sLikeStatus = "";
        Optional<CommentLike> commentLikeOptional = commentLikesRepos.getCommentLike(commentID, userID);

        if (commentLikeOptional.isEmpty()) {
            likeStatus = 0;
        }
        switch (likeStatus) {
            case 0 -> sLikeStatus = "hasn't liked";
            case 1 -> sLikeStatus = "liked";
        }

        Map<String, Object> msg = Map.of(
                "msg", "Check like successfully!",
                "status_number", likeStatus,
                "status", sLikeStatus
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getTotalLike(Long commentID) {
        Optional<Comment> mangaCommentOptional = commentRepos.findById(commentID);
        if (mangaCommentOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Comment not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get total like successfully!",
                "total_like", mangaCommentOptional.get().getCount_like()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity addLike(Long userID, Long commentID) {
        Optional<Comment> mangaCommentsOptional = commentRepos.findById(commentID);
        Optional<User> userOptional = userRepos.findById(userID);
        Optional<CommentLike> commentLikeOptional = commentLikesRepos.getCommentLike(commentID, userID);
        if (mangaCommentsOptional.isEmpty() || userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Comment or user is not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        if(!commentLikeOptional.isEmpty()){
            Map<String, Object> msg = Map.of("err", "user already liked!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }

        Comment comment = mangaCommentsOptional.get();
        comment.setCount_like(comment.getCount_like() + 1);

        CommentLike commentLike = new CommentLike();
        commentLike.setComment(comment);
        commentLike.setUser(userOptional.get());

        commentRepos.saveAndFlush(comment);
        commentLikesRepos.saveAndFlush(commentLike);

        Map<String, Object> msg = Map.of("msg", "Like successfully!");
        return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);
    }

    public ResponseEntity unLike(Long userID, Long commentID) {
        Optional<CommentLike> commentLikesOptional = commentLikesRepos.getCommentLike(commentID, userID);
        Optional<Comment> mangaCommentsOptional = commentRepos.findById(commentID);
        if (commentLikesOptional.isEmpty() || mangaCommentsOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Like is not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        CommentLike commentLike = commentLikesOptional.get();
        Comment comment = mangaCommentsOptional.get();

        if (comment.getCount_like() > 0) {
            comment.setCount_like(comment.getCount_like() - 1);
        }
        commentLikesRepos.delete(commentLike);

        commentRepos.saveAndFlush(comment);

        Map<String, Object> msg = Map.of("msg", "Unlike successfully!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
