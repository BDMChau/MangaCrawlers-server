package serverapi.tables.comment.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import serverapi.api.Response;
import serverapi.helpers.OffsetBasedPageRequest;
import serverapi.query.dtos.features.CommentDTOs.CommentDTO;
import serverapi.query.dtos.features.CommentDTOs.CommentDTOs;
import serverapi.query.dtos.features.CommentDTOs.CommentTagsDTO;
import serverapi.query.repository.forum.PostRepos;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.query.repository.manga.comment.CommentImageRepos;
import serverapi.query.repository.manga.comment.CommentRelationRepos;
import serverapi.query.repository.manga.comment.CommentTagsRepos;
import serverapi.query.repository.manga.comment.CommentRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.sharing_services.CloudinaryUploader;
import serverapi.tables.comment.comment_image.CommentImage;
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

    public CommentService(MangaRepos mangaRepos, CommentRepos commentRepos, CommentTagsRepos commentTagsRepos, PostRepos postRepos, UserRepos userRepos, CommentRelationRepos commentRelationRepos, CommentImageRepos commentImageRepos) {
        this.mangaRepos = mangaRepos;
        this.commentRepos = commentRepos;
        this.commentTagsRepos = commentTagsRepos;
        this.postRepos = postRepos;
        this.userRepos = userRepos;
        this.commentRelationRepos = commentRelationRepos;
        this.commentImageRepos = commentImageRepos;
    }


    public ResponseEntity getComments(int from, int amount, String targetTitle, Long targetID) {
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
            Map<String, Object> msg = Map.of("err", "No comments found!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
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
                    "comments", cmtsLv0
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        Map<String, Object> msg = Map.of(
                "msg", "Get comments level 0 successfully!",
                "post_info", postOptional.get(),
                "comments", cmtsLv0
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity getCommentsChild(int from, int amount, Long commentID) {
        boolean isEnd = false;
        int fromFromServer = from + amount;
        Pageable pageable = new OffsetBasedPageRequest(from, amount);
        Optional<Comment> commentOptional = commentRepos.findById(commentID);
        if (commentOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Invalid comment!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        List<CommentDTO> commentsChild = commentRepos.getCommentsChild(commentID, pageable);
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
            Map<String, Object> msg = Map.of("err", "Missing credential!");
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
        if (!image.isEmpty()) {
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
        CommentDTOs exportComment = new CommentDTOs();
        exportComment.setTo_users(commentTags);
        exportComment.setUser_id(user.getUser_id());
        exportComment.setUser_name(user.getUser_name());
        exportComment.setUser_avatar(user.getUser_avatar());
        if (!level.equals("0")) {exportComment.setLevel(level);}
        exportComment.setComment_id(comment.getComment_id());
        exportComment.setComment_time(comment.getComment_time());
        exportComment.setComment_content(comment.getComment_content());
        exportComment.setLevel(level);
        exportComment.setParent_id(parent.getComment_id());
        exportComment.setImage_url(image_url);

        Map<String, Object> msg = Map.of(
                "msg", "Add comment successfully!",
                "comment_information", exportComment
        );
        return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);
    }
    ///////////////////////////////// HELPERS ///////////////////////////////////////////////////////////////////

    private void setListTags(CommentDTO commentDTO) {
        List<CommentTagsDTO> tags = commentTagsRepos.getListTags(commentDTO.getComment_id());
        if (!tags.isEmpty()) {
            commentDTO.setTo_users(tags);
        }
    }


}
