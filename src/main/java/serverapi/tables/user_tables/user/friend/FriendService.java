package serverapi.tables.user_tables.user.friend;

import org.springframework.data.domain.Page;
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
import serverapi.tables.user_tables.friend_request_status.FriendRequestStatus;
import serverapi.tables.user_tables.notification.NotificationService;
import serverapi.tables.user_tables.user.User;
import serverapi.tables.user_tables.user_relations.UserRelations;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FriendService {
    private final UserRepos userRepos;
    private final FriendRequestRepos friendRequestRepos;
    private final UserRelationsRepos userRelationsRepos;
    private final NotificationService notificationService;

    public FriendService(UserRepos userRepos, FriendRequestRepos friendRequestRepos, UserRelationsRepos userRelationsRepos, NotificationService notificationService) {
        this.userRepos = userRepos;
        this.friendRequestRepos = friendRequestRepos;
        this.userRelationsRepos = userRelationsRepos;
        this.notificationService = notificationService;
    }

    public ResponseEntity getTotalFriend(Long userID) {

        Optional<User> userOptional = userRepos.findById(userID);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }

        Optional<FriendDTO> friendDTOOptional = friendRequestRepos.getTotalFriend(userID);
        if (friendDTOOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Empty friend!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        String sTotalFriend = friendDTOOptional.get().getCount_friend().toString();
        int totalFriend = Integer.parseInt(sTotalFriend);

        Map<String, Object> err = Map.of(
                "msg", "Get total friend successfully!",
                "total_friends", totalFriend
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getListFriends(Long userID, int from, int amount) {
        Pageable pageable = new OffsetBasedPageRequest(from, amount);
        Page<FriendDTO> friendsPage = friendRequestRepos.getListByUserId(userID, pageable);
        List<FriendDTO> friends = friendsPage.getContent();
        Long totalFriends = friendsPage.getTotalElements();


        Optional<User> userOptional = userRepos.findById(userID);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "User not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }

        if (friends.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "List friends empty!",
                    "list_friends", new ArrayList<>()
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        User user = userOptional.get();

        FriendDTO exportUser = new FriendDTO();
        exportUser.setUser_id(user.getUser_id());
        exportUser.setUser_name(user.getUser_name());
        exportUser.setUser_avatar(user.getUser_avatar());
        exportUser.setUser_email(user.getUser_email());
        exportUser.setStatus(true);

        List<FriendDTO> exportListFriends = filterListFriends(friends, userID);
        if (exportListFriends.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "List friends empty!",
                    "list_friends", new ArrayList<>()
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get list Friend successfully!",
                "don't_use_these_param", "status, list_friends",
                "from", from + amount,
                "user_info", exportUser,
                "list_friends", exportListFriends,
                "total_friends", totalFriends
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity unfriend(Long userID, Long toUserID) {
        Optional<User> toUserOptional = userRepos.findById(toUserID);
        User toUser = toUserOptional.get();
        FriendDTO exportUser = new FriendDTO();
        exportUser.setUser_id(toUser.getUser_id());
        exportUser.setUser_name(toUser.getUser_name());
        exportUser.setUser_avatar(toUser.getUser_avatar());
        exportUser.setUser_email(toUser.getUser_email());
        exportUser.setStatus(true);

        Optional<FriendDTO> targetUser = friendRequestRepos.findFriendByUserId(userID, toUserID);
        if (targetUser.isEmpty() || targetUser.get().getUser_relations_id() == null) {
            Map<String, Object> err = Map.of("err", "Cannot unfriend!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        Optional<UserRelations> userRelations = userRelationsRepos.findById(targetUser.get().getUser_relations_id());
        if (userRelations.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "err", "Something wrong!"
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(),
                    HttpStatus.ACCEPTED);
        }

        UserRelations targetRelation = userRelations.get();

        targetRelation.setFriendRequest(null);
        userRelationsRepos.saveAndFlush(targetRelation);

        Optional<FriendRequestStatus> friendRequestStatus = friendRequestRepos.findById(targetUser.get().getFriend_request_id());
        if (friendRequestStatus.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "err", "Something wrong!"
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(),
                    HttpStatus.ACCEPTED);
        }
        FriendRequestStatus targetRequest = friendRequestStatus.get();
        targetRequest.setStatus(false);
        friendRequestRepos.saveAndFlush(targetRequest);
        Map<String, Object> msg = Map.of(
                "msg", "Unfriend successfully!",
                "target_user", exportUser
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(),
                HttpStatus.OK);
    }


    public ResponseEntity addFriend(Long senderID, Long receiverID) {
        Boolean isExistedReq = notificationService.checkIsExisted("user", receiverID, receiverID, senderID);
        if (isExistedReq == true) {
            Map<String, Object> err = Map.of("err", "Request not found");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Optional<User> senderOptional = userRepos.findById(senderID);
        Optional<User> receiverOptional = userRepos.findById(receiverID);

        Optional<FriendRequestStatus> friendRequestStatusOptional = friendRequestRepos.getFriendStatus(senderID, receiverID);


        if (friendRequestStatusOptional.isEmpty() || senderOptional.isEmpty() || receiverOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Cannot add friend");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        FriendRequestStatus friendRequestStatus = friendRequestStatusOptional.get();
        friendRequestStatus.setTime_accepted(currentTime);
        friendRequestRepos.saveAndFlush(friendRequestStatus);

        Optional<UserRelations> userRelationsOptional = userRelationsRepos.findUserRelations(senderID, receiverID);
        System.err.println("line 162");
        if (userRelationsOptional.isEmpty()) {

            UserRelations userRelations = new UserRelations();
            userRelations.setParent_id(senderOptional.get());
            userRelations.setChild_id(receiverOptional.get());
            userRelations.setFriendRequest(friendRequestStatusOptional.get());
            userRelationsRepos.saveAndFlush(userRelations);

            Map<String, Object> msg = Map.of("msg", "Add friend successfully!");
            return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);
        }

        UserRelations userRelations = userRelationsOptional.get();
        userRelations.setFriendRequest(friendRequestStatusOptional.get());
        userRelationsRepos.saveAndFlush(userRelations);

        Map<String, Object> msg = Map.of(
                "msg", "Add friend successfully!"
        );
        return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(),
                HttpStatus.CREATED);

    }

    public ResponseEntity getMutualFriends(Long userID, Long toUserID) {
        List<FriendDTO> senderFriends = friendRequestRepos.getListByUserId(userID);
        List<FriendDTO> receiverFriends = friendRequestRepos.getListByUserId(toUserID);
        if (senderFriends.isEmpty() || receiverFriends.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "User or to_user doesn't have any friend!",
                    "count_mutual", 0,
                    "list_mutual", new ArrayList<>()
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        List<FriendDTO> listFriendsSender = filterListFriends(senderFriends, userID);
        List<FriendDTO> listFriendsReceiver = filterListFriends(receiverFriends, toUserID);
        if (listFriendsSender.isEmpty() || listFriendsReceiver.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "User or to_user doesn't have any friend!",
                    "count_mutual", 0,
                    "list_mutual", new ArrayList<>()
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        List<FriendDTO> mutualFriends = new ArrayList<>();
        listFriendsSender.forEach(senderFriend -> {
            listFriendsReceiver.forEach(receiverFriend -> {
                if (senderFriend.getUser_id().equals(receiverFriend.getUser_id()))  mutualFriends.add(senderFriend);
            });
        });

        if (mutualFriends.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "no mutual friends!",
                    "count_mutual", 0,
                    "list_mutual", new ArrayList<>()
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }


        Map<String, Object> err = Map.of(
                "msg", "Get mutual friend successfully!",
                "count_mutual", mutualFriends.size(),
                "list_mutual", mutualFriends
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
    }

    /////////////////////////////Helper///////////////////////////////
    public List<FriendDTO> filterListFriends(List<FriendDTO> getListFriends, Long userID) {
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
                        friendDTO.setSocket_session_id(user1.getSocket_session_id());
                        friendDTO.setIs_online(false);
                        if (user1.getSocket_session_id() != null) friendDTO.setIs_online(true);

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
                        friendDTO.setSocket_session_id(user1.getSocket_session_id());
                        friendDTO.setIs_online(false);
                        if (user1.getSocket_session_id() != null) friendDTO.setIs_online(true);

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

    // 0: add friend; 1: pending; 2: friend; 3: accept friend
    public Integer checkStatus(Long senderID, Long receiverID) {
        Optional<FriendRequestStatus> statusOptional = friendRequestRepos.getFriendStatus(senderID, receiverID);
        AtomicInteger iStatus = new AtomicInteger();
        if (statusOptional.isEmpty()) {
            iStatus.set(0);
        } else {
            if (statusOptional.get().getTime_accepted() == null) {
                if (statusOptional.get().getUser().getUser_id().equals(senderID)) {
                    iStatus.set(1);
                } else if (statusOptional.get().getUser().getUser_id().equals(receiverID)) {
                    iStatus.set(3);
                }
            } else {
                iStatus.set(2);
            }
        }
        return iStatus.get();
    }


    public Boolean checkStatusOnlOff(Long userId, Long userIdToCheck) {
        int isFriend = checkStatus(userId, userIdToCheck);
        if (isFriend != 2) return null;

        User user = userRepos.findById(userIdToCheck).get();

        UUID socketSessionId = user.getSocket_session_id();
        return socketSessionId != null;
    }


}


