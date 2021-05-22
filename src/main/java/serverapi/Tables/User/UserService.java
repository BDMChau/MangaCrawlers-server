package serverapi.Tables.User;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.DTO.FollowingDTO;
import serverapi.Query.Repository.FollowingRepos;
import serverapi.Query.Repository.MangaRepos;
import serverapi.Query.Repository.UserRepos;
import serverapi.Tables.FollowingManga.FollowingManga;
import serverapi.Tables.Manga.Manga;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UserService {
    private final MangaRepos mangaRepository;
    private final FollowingRepos followingRepos;
    private final UserRepos userRepos;

    public UserService(MangaRepos mangaRepository, FollowingRepos followingRepos, UserRepos userRepos) {
        this.mangaRepository = mangaRepository;
        this.followingRepos = followingRepos;
        this.userRepos = userRepos;
    }

    public ResponseEntity getFollowManga(Long UserId) {

        List<FollowingDTO> Follow = followingRepos.FindByUserId (UserId);

        if (Follow.isEmpty ()) {
            Map<String, Object> msg = Map.of ("msg", "No mangas!");
            return new ResponseEntity<> (new Response (204, HttpStatus.NO_CONTENT, msg).toJSON (), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of (
                "msg", "Get all mangas successfully!",
                "Following Info", Follow,
                "UserID", UserId

        );
        return new ResponseEntity<> (new Response (200, HttpStatus.OK, msg).toJSON (), HttpStatus.OK);
    }


    public ResponseEntity deleteFollowManga(Long mangaId, Long userId) {
        List<FollowingDTO> Follow = followingRepos.FindByUserId (userId);
        System.out.println ("mangaID" + mangaId);
        System.out.println ("userID" + userId);

        AtomicBoolean atomicBoolean = new AtomicBoolean (false);
        if (Follow.isEmpty ()) {
            Map<String, Object> msg = Map.of ("msg", "No mangas!");
            return new ResponseEntity<> (new Response (204, HttpStatus.NO_CONTENT, msg).toJSON (), HttpStatus.NO_CONTENT);

        } else {

            Follow.forEach (item -> {
                System.out.println (item.getManga_id ());
                System.out.println (item.getManga_id ().equals (mangaId));
                if (item.getManga_id ().equals (mangaId)) {
                    Long FollowId = item.getFollowId ();

                    System.out.println ("follow id: " + FollowId);
                    followingRepos.deleteById (FollowId);
                    atomicBoolean.set (true);
                }
            });
            if (atomicBoolean.get () == true) {
                Map<String, Object> msg = Map.of (
                        "msg", "delete follow successfully!"
                );
                return new ResponseEntity<> (new Response (200, HttpStatus.OK, msg).toJSON (), HttpStatus.OK);
            }

            Map<String, Object> msg = Map.of ("msg", "Cannot delete");
            return new ResponseEntity<> (new Response (204, HttpStatus.NO_CONTENT, msg).toJSON (), HttpStatus.NO_CONTENT);
        }
    }



    public ResponseEntity addFollowManga(Long mangaId, Long userId) {
        AtomicBoolean atomicBoolean = new AtomicBoolean (false);
        List<FollowingDTO> follows = followingRepos.FindByUserId (userId);

        if (follows.isEmpty ()) {

            Optional<User> user = userRepos.findById (userId);
            User user1 = user.get ();

            Optional<Manga> manga = mangaRepository.findById (mangaId);
            Manga manga1 = manga.get ();

            FollowingManga followingManga = new FollowingManga ();
            followingManga.setUser (user1);
            followingManga.setManga (manga1);

            followingRepos.save (followingManga);

            Map<String, Object> msg = Map.of (
                    "msg", "add Follow successfully!"

            );
            return new ResponseEntity<> (new Response (200, HttpStatus.OK, msg).toJSON (), HttpStatus.OK);

        } else {
            follows.forEach (item -> {
                if (item.getManga_id ().equals (mangaId)) {

                    atomicBoolean.set (true);
                }
            });
            if (atomicBoolean.get () == true) {
                Map<String, Object> msg = Map.of ("msg", "Cannot ADD to database!");
                return new ResponseEntity<> (new Response (204, HttpStatus.NO_CONTENT, msg).toJSON (), HttpStatus.NO_CONTENT);

            } else {
                Optional<User> user = userRepos.findById (userId);
                User user1 = user.get ();

                Optional<Manga> manga = mangaRepository.findById (mangaId);
                Manga manga1 = manga.get ();

                FollowingManga followingManga = new FollowingManga ();
                followingManga.setUser (user1);
                followingManga.setManga (manga1);

                followingRepos.save (followingManga);

                Map<String, Object> msg = Map.of (
                        "msg", "add Follow successfully!"
                );
                return new ResponseEntity<> (new Response (200, HttpStatus.OK, msg).toJSON (), HttpStatus.OK);

            }

        }
    }



}
