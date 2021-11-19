package serverapi.tables.comment.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import serverapi.api.Response;
import serverapi.helpers.OffsetBasedPageRequest;
import serverapi.query.dtos.features.CommentDTOs.CommentDTO;
import serverapi.query.dtos.features.CommentDTOs.CommentTagsDTO;
import serverapi.query.repository.forum.PostRepos;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.query.repository.manga.comment.*;
import serverapi.query.repository.user.UserRepos;
import serverapi.sharing_services.CloudinaryUploader;
import serverapi.tables.comment.comment_image.CommentImage;
import serverapi.tables.comment.comment_like.CommentLike;
import serverapi.tables.comment.comment_relation.CommentRelation;
import serverapi.tables.comment.comment_tag.CommentTag;
import serverapi.tables.forum.post.Post;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
public class CommentService {
    private final CloudinaryUploader cloudinaryUploader = CloudinaryUploader.getInstance();

    private final MangaRepos mangaRepos;
    private final CommentRepos commentRepos;
    private final CommentTagsRepos commentTagsRepos;
    private final PostRepos postRepos;
    private final UserRepos userRepos;
    private final CommentRelationRepos commentRelationRepos;
    private final CommentImageRepos commentImageRepos;
    private final CommentLikesRepos commentLikesRepos;

    public CommentService(MangaRepos mangaRepos, CommentRepos commentRepos, CommentTagsRepos commentTagsRepos, PostRepos postRepos, UserRepos userRepos, CommentRelationRepos commentRelationRepos, CommentImageRepos commentImageRepos, CommentLikesRepos commentLikesRepos) {
        this.mangaRepos = mangaRepos;
        this.commentRepos = commentRepos;
        this.commentTagsRepos = commentTagsRepos;
        this.postRepos = postRepos;
        this.userRepos = userRepos;
        this.commentRelationRepos = commentRelationRepos;
        this.commentImageRepos = commentImageRepos;
        this.commentLikesRepos = commentLikesRepos;
    }


    public ResponseEntity getComments(int from, int amount, String targetTitle, Long targetID, Long userID) {
        final Pageable pageable = new OffsetBasedPageRequest(from, amount);
        Optional<Manga> mangaOptional = Optional.empty();
        Optional<Post> postOptional = Optional.empty();
        if (targetTitle.equals("manga")) {
            mangaOptional = mangaRepos.findById(targetID);
        } else {
            postOptional = postRepos.findById(targetID);
        }
        List<CommentDTO> cmtsLv0 = commentRepos.getComments(targetTitle, targetID, pageable);
        if (cmtsLv0.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "No comments found!",
                    "comments", new ArrayList<>()
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        Optional<User> userOptional = userRepos.findById(userID);
        if (userOptional.isPresent()) {
            cmtsLv0.forEach(item -> {
                setUserIsLike(item.getComment_id(), userID, item);
            });
        }
        cmtsLv0.forEach(comment -> {
            if (comment.getCount_comments_child() >= 1) {
                comment.setCount_comments_child(comment.getCount_comments_child() - 1L);
            }
            setListTags(comment);
        });

        if (mangaOptional.isPresent()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Get comments level 0 successfully!",
                    "manga_info", mangaOptional.get(),
                    "from", from + amount,
                    "comments", cmtsLv0
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        Map<String, Object> msg = Map.of(
                "msg", "Get comments level 0 successfully!",
                "post_info", postOptional.get(),
                "from", from + amount,
                "comments", cmtsLv0
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity getCommentsChild(int from, int amount, Long commentID, Long userID) {
        boolean isEnd = false;
        int fromFromServer = from + amount;
        Pageable pageable = new OffsetBasedPageRequest(from, amount);
        Optional<Comment> commentOptional = commentRepos.findById(commentID);
        if (commentOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Invalid comment!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        List<CommentDTO> commentsChild = commentRepos.getCommentsChild(commentID, pageable);
        Optional<User> userOptional = userRepos.findById(userID);
        if (userOptional.isPresent()) {
            commentsChild.forEach(item -> {
                setUserIsLike(item.getComment_id(), userID, item);
            });
        }
        commentsChild.forEach(this::setListTags);
        if (commentsChild.isEmpty() || commentsChild.size() < amount) {
            isEnd = true;
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get comments child successfully!",
                "comment_info", commentOptional.get(),
                "from", fromFromServer,
                "is_end", isEnd,
                "comments_child", commentsChild);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity addComment(String targetTitle, Long targetID,
                                     Long parentID, Long userID, List<Long> toUsersID,
                                     String content, MultipartFile image, String stickerUrl) throws IOException {
        System.err.println("line123");
        Calendar timeUpdated = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Optional<Manga> mangaOptional = Optional.empty();
        Optional<Post> postOptional = Optional.empty();
        if (targetTitle.equals("manga")) {
            mangaOptional = mangaRepos.findById(targetID);
        } else {
            postOptional = postRepos.findById(targetID);
        }
        if (mangaOptional.isEmpty() && postOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Invalid credential!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOptional = userRepos.findById(userID);
        if (userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "User not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        User toUser = user;
        System.err.println(!toUsersID.isEmpty());
        if (!toUsersID.isEmpty()) {
            Optional<User> toUserOptional = userRepos.findById(toUsersID.get(0));
            if (toUserOptional.isPresent()) toUser = toUserOptional.get();
        } else {
            toUsersID.add(toUser.getUser_id());
        }
        /*---------------------------------------------------------------------------------------------------------*/
        // Add Comment
        Comment comment = new Comment();
        mangaOptional.ifPresent(comment::setManga);
        postOptional.ifPresent(comment::setPost);
        comment.setUser(user);
        comment.setComment_content(content);
        comment.setComment_time(timeUpdated);
        comment.setIs_deprecated(false);
        commentRepos.saveAndFlush(comment);

        // Add comment_relation
        if (parentID == 0L) {
            parentID = comment.getComment_id();
        }
        Optional<Comment> parentOptional = commentRepos.findById(parentID);
        Comment parent = parentOptional.get();
        // check level
        String level = "0";
        if (comment.getComment_id().equals(parent.getComment_id())) level = "0";
        else {
            level = "1";
        }
        CommentRelation commentRelation = new CommentRelation();
        commentRelation.setChild_id(comment);
        commentRelation.setParent_id(parent);
        commentRelation.setLevel(level);
        commentRelationRepos.saveAndFlush(commentRelation);

        // Add tags
        int offSet = 0;
        List<CommentTagsDTO> commentTags = new ArrayList<>();
        for (Long toUserID : toUsersID) {
            Optional<User> userTagOptional = userRepos.findById(toUserID);
            if (userTagOptional.isPresent()) {
                User userTag = userTagOptional.get();
                CommentTag commentTag = new CommentTag();
                commentTag.setComment(comment);
                commentTag.setUser(userTag);
                commentTag.setOff_set(offSet);
                commentTagsRepos.saveAndFlush(commentTag);

                // for export
                CommentTagsDTO commentTagsDTO = new CommentTagsDTO();
                commentTagsDTO.setComment_id(comment.getComment_id());
                commentTagsDTO.setComment_tag_id(commentTag.getComment_tag_id());
                commentTagsDTO.setUser_id(commentTag.getUser().getUser_id());
                commentTagsDTO.setUser_avatar(commentTag.getUser().getUser_avatar());
                commentTagsDTO.setUser_name(commentTag.getUser().getUser_name());

                commentTags.add(commentTagsDTO);
                offSet++;
            }
        }
        // Add image
        String image_url = null;
        if (image != null) {
            Map cloudinaryResponse = cloudinaryUploader.uploadImg(
                    image.getBytes(),
                    "",
                    "user_comment_images",
                    false
            );
            String securedUrl = (String) cloudinaryResponse.get("secure_url");
            CommentImage commentImage = new CommentImage();
            commentImage.setImage_url(securedUrl);
            commentImage.setComment(comment);
            commentImageRepos.saveAndFlush(commentImage);
            image_url = securedUrl;
        } else {
            if (!stickerUrl.equals("")) {
                CommentImage commentImage = new CommentImage();
                commentImage.setImage_url(stickerUrl);
                commentImage.setComment(comment);
                commentImageRepos.saveAndFlush(commentImage);
                image_url = stickerUrl;
            }
        }
        // Response
        CommentDTO exportComment = new CommentDTO();
        exportComment.setTo_users(commentTags);
        exportComment.setUser_id(user.getUser_id());
        exportComment.setUser_name(user.getUser_name());
        exportComment.setUser_avatar(user.getUser_avatar());
        exportComment.setComment_id(comment.getComment_id());
        exportComment.setComment_time(comment.getComment_time());
        exportComment.setComment_content(comment.getComment_content());
        exportComment.setCount_like(comment.getCount_like());
        exportComment.setLevel(level);
        exportComment.setParent_id(parent.getComment_id());
        exportComment.setImage_url(image_url);

        Map<String, Object> msg = Map.of(
                "msg", "Add comment successfully!",
                "comment_info", exportComment
        );
        return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);
    }


    public ResponseEntity updateComment(Long userID, List<Long> toUsersID, Long commentID,
                                        String content, MultipartFile image, String stickerUrl) throws IOException {
        Calendar timeUpdated = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        Optional<User> userOptional = userRepos.findById(userID);
        Optional<Comment> mangaCommentsOptional = commentRepos.findById(commentID);
        if (userOptional.isEmpty() || mangaCommentsOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Invalid credential!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        Comment comment = mangaCommentsOptional.get();
        User user = userOptional.get();

        // check allow
        if (!user.getUser_id().equals(comment.getUser().getUser_id())) {
            Map<String, Object> msg = Map.of("err", "You don't have permission!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        // if content, image, toUsersID is empty or null, update comment will change to delete comment
        if (content.isEmpty() && stickerUrl.isEmpty() && image == null) {
            comment.setIs_deprecated(true);
            commentRepos.save(comment);
            Map<String, Object> msg = Map.of("msg", "Delete comment successfully!");
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        // comment table's part (update content)
        comment.setComment_content(content);
        comment.setComment_time(timeUpdated);
        commentRepos.saveAndFlush(comment);
        // comment_image table's part (update image or stickerUrl)
        // check current image of comment
        Optional<CommentImage> commentImageOptional = commentImageRepos.getCommentImageByCommentID(commentID);

        if (image != null) {
            Map cloudinaryResponse = cloudinaryUploader.uploadImg(
                    image.getBytes(),
                    "",
                    "user_comment_images",
                    false
            );
            String securedUrl = (String) cloudinaryResponse.get("secure_url");
            CommentImage commentImage;
            commentImage = commentImageOptional.orElseGet(CommentImage::new);
            addCommentImage(commentImage, comment, securedUrl);
        }
        if (!stickerUrl.equals("")) {
            CommentImage commentImage;
            commentImage = commentImageOptional.orElseGet(CommentImage::new);
            addCommentImage(commentImage, comment, stickerUrl);
        }
        // comment_tag table's part (update toUserID)
        int offSet = 0;
        for (Long toUserID : toUsersID) {
            Optional<User> userTagOptional = userRepos.findById(toUserID);
            if (userTagOptional.isPresent()) {
                User userTag = userTagOptional.get();
                CommentTag commentTag = new CommentTag();
                commentTag.setComment(comment);
                commentTag.setUser(userTag);
                commentTag.setOff_set(offSet);
                commentTagsRepos.saveAndFlush(commentTag);
                offSet++;
            }
        }
        // Response
        Optional<CommentDTO> responseCmt = commentRepos.getCommentByID(commentID);
        responseCmt.ifPresent(this::setListTags);

        Map<String, Object> msg = Map.of(
                "msg", "Update comment successfully!",
                "comment_info", responseCmt
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity deleteComment(Long userID, Long commentID) {
        Optional<User> userOptional = userRepos.findById(userID);
        Optional<Comment> commentOptional = commentRepos.findById(commentID);
        if (userOptional.isEmpty() || commentOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Invalid credential!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        Comment comment = commentOptional.get();
        if (comment.getIs_deprecated().equals(true)) {
            Map<String, Object> msg = Map.of(
                    "err", "Comment is already delete!",
                    "comment", comment
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        User user = userOptional.get();
        if (!user.getUser_id().equals(comment.getUser().getUser_id())) {
            Map<String, Object> msg = Map.of("err", "You don't have permission!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        // Delete comment
        comment.setIs_deprecated(true);
        commentRepos.saveAndFlush(comment);
        //Response
        Optional<CommentDTO> responseCmt = commentRepos.getCommentByID(commentID);
        responseCmt.ifPresent(this::setListTags);
        Map<String, Object> msg = Map.of(
                "msg", "Delete comment successfully!",
                "comment", responseCmt
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
    ///////////////////////////////// HELPERS ///////////////////////////////////////////////////////////////////

    private void setListTags(CommentDTO commentDTO) {
        List<CommentTagsDTO> tags = commentTagsRepos.getListTags(commentDTO.getComment_id());
        if (!tags.isEmpty()) {
            commentDTO.setTo_users(tags);
        }
    }

    private void addCommentImage(CommentImage commentImage, Comment comment, String url) {
        commentImage.setImage_url(url);
        commentImage.setComment(comment);
        commentImageRepos.saveAndFlush(commentImage);
    }

    private void setUserIsLike(Long commentID, Long userID, CommentDTO commentDTO) {
        int likeStatus = 1;
        Optional<CommentLike> commentLikeOptional = commentLikesRepos.getCommentLike(commentID, userID);
        if (commentLikeOptional.isEmpty()) {
            likeStatus = 0;
        }
        if (likeStatus == 1) {
            commentDTO.setUser_id_is_liked(userID);
            commentDTO.setIs_liked("liked");
        }
    }
}
