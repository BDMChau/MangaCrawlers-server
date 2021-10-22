package serverapi.tables.user_tables.user.friend;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.helpers.OffsetBasedPageRequest;
import serverapi.query.dtos.features.FriendDTO;
import serverapi.query.repository.user.FriendRequestRepos;
import serverapi.query.repository.user.UserRelationsRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.tables.user_tables.user.User;
import serverapi.tables.user_tables.user_relations.UserRelations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class FriendService {
    private final UserRepos userRepos;
    private final FriendRequestRepos friendRequestRepos;
    private final UserRelationsRepos userRelationsRepos;

    public FriendService(UserRepos userRepos, FriendRequestRepos friendRequestRepos, UserRelationsRepos userRelationsRepos) {
        this.userRepos = userRepos;
        this.friendRequestRepos = friendRequestRepos;
        this.userRelationsRepos = userRelationsRepos;
    }

    public ResponseEntity getListFriends(Long userID, int from, int amount) {

        Pageable pageable = new OffsetBasedPageRequest(from, amount);
        List<FriendDTO> getListFriends = friendRequestRepos.getListByUserId(userID, pageable);
        Optional<User> userOptional = userRepos.findById(userID);
        if (userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "err", "User not found!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        if (getListFriends.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "err", "Empty list friend!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        ////////////////////////////////////////////////
        User user = userOptional.get();

        FriendDTO exportUser = new FriendDTO();
        exportUser.setUser_id(user.getUser_id());
        exportUser.setUser_name(user.getUser_name());
        exportUser.setUser_avatar(user.getUser_avatar());
        exportUser.setUser_email(user.getUser_email());
        exportUser.setStatus(true);

        List<FriendDTO> exportListFriends = filterListFriends(getListFriends, userID);
        if (exportListFriends.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty list friends!"
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(),
                    HttpStatus.ACCEPTED);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get list Friend successfully!",
                "don't_use_these_param", "status, list_friends",
                "user_info", exportUser,
                "list_friends", exportListFriends
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


    public ResponseEntity unFriend(Long userID, Long toUserID, List<FriendDTO> listFriends) {

        Optional<User> toUserOptional = userRepos.findById(toUserID);
        User toUser = toUserOptional.get();
        FriendDTO exportUser = new FriendDTO();
        exportUser.setUser_id(toUser.getUser_id());
        exportUser.setUser_name(toUser.getUser_name());
        exportUser.setUser_avatar(toUser.getUser_avatar());
        exportUser.setUser_email(toUser.getUser_email());
        exportUser.setStatus(true);

        Optional<FriendDTO> targetUser = friendRequestRepos.findFriendByUserId(userID, toUserID);
        if (targetUser.isEmpty() || listFriends.isEmpty() || targetUser.get().getUser_relations_id() != null) {
            Map<String, Object> msg = Map.of(
                    "msg", "Cannot unfriend!"
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        Optional<UserRelations> userRelations = userRelationsRepos.findById(targetUser.get().getUser_relations_id());
        UserRelations targetRelation = userRelations.get();
        targetRelation.setFriendRequest(null);
        userRelationsRepos.saveAndFlush(targetRelation);

        List<FriendDTO> exportListFriends = filterListFriends(listFriends, userID);
        if (!listFriends.isEmpty()) {
            AtomicBoolean flag = new AtomicBoolean(false);
            exportListFriends.forEach(friend -> {
                if (flag.get() == false) {
                    if (friend.getUser_id().equals(toUserID)) {
                        exportListFriends.remove(friend);
                        flag.set(true);
                    }
                }
            });
        }
        Map<String, Object> msg = Map.of(
                "msg", "Un Friend successfully!",
                "target_user", exportUser,
                "list_friends_after_delete", exportListFriends
                );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }

    /////////////////////////////Helper///////////////////////////////
    private List<FriendDTO> filterListFriends(List<FriendDTO> getListFriends, Long userID) {
        List<FriendDTO> exportListFriends = new ArrayList<>();
        getListFriends.forEach(friend -> {
            if (friend.isStatus()) {
                if (!friend.getParent_user_id().equals(userID)) {
                    Optional<User> userOptional1 = userRepos.findById(friend.getParent_user_id());
                    if (!userOptional1.isEmpty()) {
                        User user1 = userOptional1.get();

                        FriendDTO friendDTO = new FriendDTO();
                        friendDTO.setUser_id(user1.getUser_id());
                        friendDTO.setUser_name(user1.getUser_name());
                        friendDTO.setUser_avatar(user1.getUser_avatar());
                        friendDTO.setUser_email(user1.getUser_email());
                        friendDTO.setStatus(true);

                        exportListFriends.add(friendDTO);
                    }
                } else if (!friend.getChild_user_id().equals(userID)) {
                    Optional<User> userOptional1 = userRepos.findById(friend.getChild_user_id());
                    if (!userOptional1.isEmpty()) {
                        User user1 = userOptional1.get();

                        FriendDTO friendDTO = new FriendDTO();
                        friendDTO.setUser_id(user1.getUser_id());
                        friendDTO.setUser_name(user1.getUser_name());
                        friendDTO.setUser_avatar(user1.getUser_avatar());
                        friendDTO.setUser_email(user1.getUser_email());
                        friendDTO.setStatus(true);

                        exportListFriends.add(friendDTO);
                    }
                }
            }
        });
        if (exportListFriends.isEmpty()) {
            return new ArrayList<>();
        }
        return exportListFriends;
    }
}


