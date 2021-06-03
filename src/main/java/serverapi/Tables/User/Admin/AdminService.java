package serverapi.Tables.User.Admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.DTO.*;
import serverapi.Query.Repository.FollowingRepos;
import serverapi.Query.Repository.MangaRepos;
import serverapi.Query.Repository.TransGroupRepos;
import serverapi.Query.Repository.UserRepos;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.TransGroup.TransGroup;
import serverapi.Tables.User.User;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminService {

    private final UserRepos userRepos;
    private final FollowingRepos followingRepos;
    private final MangaRepos mangaRepos;
    private final TransGroupRepos transGroupRepos;


    @Autowired
    public AdminService(UserRepos userRepos, FollowingRepos followingRepos, MangaRepos mangaRepos, TransGroupRepos transGroupRepos) {
        this.userRepos = userRepos;
        this.followingRepos = followingRepos;
        this.mangaRepos = mangaRepos;
        this.transGroupRepos = transGroupRepos;
    }

    private Boolean isUserAdmin(Long userId) {
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();

        Boolean isAdmin = user.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
            return false;
        }

        return true;
    }


    ////////////////////////////////////////////////////////

    public ResponseEntity reportUserFollowManga() {

        List<ReportUserFollowMangaDTO> reportUserFollowMangaDTOS = userRepos.findAllFollwingManga(PageRequest.of(0, 1));

        if (reportUserFollowMangaDTOS.isEmpty()) {
            Map<String, Object> err = Map.of("msg", "Nothing user follow mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }
        // reportUserFollowMangaDTOS.sort(Comparator.comparing(ReportUserFollowMangaDTO::getTotal_user).reversed());

        Map<String, Object> msg = Map.of(
                "msg", "Report user follow  mangas successfully!",
                "mangas", reportUserFollowMangaDTOS
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }



    public ResponseEntity reportTopViewManga() {
        List<ReportTopMangaDTO> reportTopMangaDTOS = userRepos.findTopManga(PageRequest.of(0, 5));

        if (reportTopMangaDTOS.isEmpty()) {
            Map<String, Object> err = Map.of("msg", "Nothing of top mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of("msg", "Report top five mangas successfully!", "data", reportTopMangaDTOS);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }



    public ResponseEntity reportUser(Long userId) {
        Boolean isAdmin = isUserAdmin(userId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        List<UserDTO> getUserInfo = userRepos.getAllUser();
        List<ReportsDTO> listReportUser = new ArrayList<>();

        for (int i = 0; i < 12; i++) {

            ReportsDTO reportsDTO = new ReportsDTO();
            int finalI = i + 1;
            System.err.println("lỗi" + finalI);
            List<UserRDTO> userDTOList = new ArrayList<>();

            getUserInfo.forEach(item -> {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
                Integer monthOfUser = Integer.parseInt(simpleDateFormat.format(item.getCreatedAt ().getTime()));

                if (monthOfUser == finalI) {

                    userDTOList.add(item);
                    System.out.println("them " + finalI);

                }


            });
            System.out.println("reportuserdtolist" + userDTOList);

            System.out.println("report user dtos" + listReportUser.size());


            reportsDTO.setValues(userDTOList.size());
            reportsDTO.setMonth(finalI);
            System.out.println("dieu kien dung" + finalI);
            listReportUser.add(reportsDTO);


        }

        Map<String, Object> msg = Map.of(
                "msg", "Get report of users successfully!",
                "users_report", listReportUser);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }

    public ResponseEntity reportManga(Long userId) {
        Boolean isAdmin = isUserAdmin(userId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        List<Manga> mangasInfo = mangaRepos.findAll();
        List<ReportsDTO> listReportManga = new ArrayList<>();

        for (int i = 0; i < 12; i++) {

            ReportsDTO reportsDTO = new ReportsDTO();
            int finalI = i + 1;
            System.err.println("lỗi" + finalI);
            List<Manga> mangaList = new ArrayList<>();

            mangasInfo.forEach(item -> {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
                Integer monthOfUser = Integer.parseInt(simpleDateFormat.format(item.getCreatedAt().getTime()));

                if (monthOfUser == finalI) {

                    mangaList.add(item);
                    System.out.println("them " + finalI);

                }


            });
            System.out.println("reportmangadtolist" + mangaList);

            System.out.println("report manga dtos" + listReportManga.size());


            reportsDTO.setValues(mangaList.size());
            reportsDTO.setMonth(finalI);
            System.out.println("dieu kien dung" + finalI);
            listReportManga.add(reportsDTO);


        }

        Map<String, Object> msg = Map.of(
                "msg", "Get report of mangas successfully!",
                "mangas_report",listReportManga );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }



    public ResponseEntity deleteUser(Long userId, Long adminId) {
        Boolean isAdmin = isUserAdmin(userId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        //get user info
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> err = Map.of(
                    "err", "User not found!"

            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();
        userRepos.delete(user);

        List<User> users = userRepos.findAll();

        Comparator<User> compareById = (User u1, User u2) -> u1.getUser_id().compareTo(u2.getUser_id());
        Collections.sort(users, compareById); // sort users by id

        Map<String, Object> msg = Map.of(
                "msg", "delete user successfully!",
                "users", users
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }

    public ResponseEntity reportTransGroup(Long userId) {
        Boolean isAdmin = isUserAdmin(userId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        List<TransGroup> getTransGroupInfo = transGroupRepos.findAll();
        List<ReportsDTO> listReportTransGroup = new ArrayList<>();

        for (int i = 0; i < 12; i++) {

            ReportsDTO reportsDTO = new ReportsDTO();
            int finalI = i + 1;
            System.err.println("lỗi" + finalI);
            List<TransGroup> transGroupList = new ArrayList<>();

            getTransGroupInfo.forEach(item -> {


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
                Integer monthOfUser = Integer.parseInt(simpleDateFormat.format(item.getCreatedAt().getTime()));

                if (monthOfUser == finalI) {

                    transGroupList.add(item);
                    System.out.println("them " + finalI);

                }

            });
            System.out.println("reportuserdtolist" + transGroupList);

            System.out.println("report user dtos" + listReportTransGroup.size());


            reportsDTO.setValues(transGroupList.size());
            reportsDTO.setMonth(finalI);
            System.out.println("dieu kien dung" + finalI);
            listReportTransGroup.add(reportsDTO);

        }

        Map<String, Object> msg = Map.of(
                "msg", "Get report of trans_group successfully!",
                "trans_group_report", listReportTransGroup,
                "trans_group_info",getTransGroupInfo
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity deprecateUser(Long userId, Long adminId) {
        Boolean isAdmin = isUserAdmin(userId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        //get user info
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> err = Map.of(
                    "err", "User not found!"

            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();
        user.setUser_isVerified(false);
        userRepos.save(user);

        List<User> users = userRepos.findAll();

        Comparator<User> compareById = (User u1, User u2) -> u1.getUser_id().compareTo(u2.getUser_id());
        Collections.sort(users, compareById); // sort users by id

        Map<String, Object> msg = Map.of(
                "msg", "Deprecate user successfully!",
                "users", users
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }



    public ResponseEntity getAllUsers(Long userId) {
        Boolean isAdmin = isUserAdmin(userId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        List<User> users = userRepos.findAll();
        if (users.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty users!",
                    "users", users
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get all users successfully!",
                "users", users
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);


    }



    public ResponseEntity getAllMangas(Long userId) {
        Boolean isAdmin = isUserAdmin(userId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        List<AuthorMangaDTO> mangas = mangaRepos.getAllMangasInfo();

        if (mangas.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty mangas!",
                    "mangas", mangas
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "mangas", mangas
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

}
