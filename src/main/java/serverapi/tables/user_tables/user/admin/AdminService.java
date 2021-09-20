package serverapi.tables.user_tables.user.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.dtos.features.ReportDTOs.ReportTopMangaDTO;
import serverapi.query.dtos.features.ReportDTOs.ReportUserFollowMangaDTO;
import serverapi.query.dtos.features.ReportDTOs.ReportsDTO;
import serverapi.query.dtos.features.ReportDTOs.UserRDTO;
import serverapi.query.dtos.tables.AuthorMangaDTO;
import serverapi.query.repository.manga.ChapterRepos;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.query.repository.user.FollowingRepos;
import serverapi.query.repository.user.TransGroupRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.trans_group.TransGroup;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminService {

    private final UserRepos userRepos;
    private final FollowingRepos followingRepos;
    private final MangaRepos mangaRepos;
    private final TransGroupRepos transGroupRepos;
    private final ChapterRepos chapterRepos;


    @Autowired
    public AdminService(UserRepos userRepos, FollowingRepos followingRepos, MangaRepos mangaRepos,
                        TransGroupRepos transGroupRepos, ChapterRepos chapterRepos) {
        this.userRepos = userRepos;
        this.followingRepos = followingRepos;
        this.mangaRepos = mangaRepos;
        this.transGroupRepos = transGroupRepos;
        this.chapterRepos = chapterRepos;
    }


    private Boolean isUserAdmin(Long userId) {
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();

        System.err.println("isAdmin: " + user.getUser_isAdmin());
        Boolean isAdmin = user.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
            return false;
        }

        return true;
    }


    ////////////////////////////////////////////////////////


    // report chart
    public ResponseEntity reportUser(Long userId) {
        Boolean isAdmin = isUserAdmin(userId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        List<UserRDTO> getUserInfo = userRepos.getAllUser();
        List<ReportsDTO> listReportUser = new ArrayList<>();

        for (int i = 0; i < 12; i++) {

            ReportsDTO reportsDTO = new ReportsDTO();
            int finalI = i + 1;
            System.err.println("lỗi" + finalI);
            List<UserRDTO> userDTOList = new ArrayList<>();

            getUserInfo.forEach(item -> {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
                Integer monthOfUser = Integer.parseInt(simpleDateFormat.format(item.getCreated_at().getTime()));

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
                Integer monthOfUser = Integer.parseInt(simpleDateFormat.format(item.getCreated_at().getTime()));

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
                "mangas_report", listReportManga);
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
                Integer monthOfUser = Integer.parseInt(simpleDateFormat.format(item.getCreated_at().getTime()));

                if (monthOfUser == finalI) {

                    transGroupList.add(item);
                    System.out.println("them " + finalI);

                }

            });
            System.out.println("reportuserdtolist" + transGroupList);

            System.out.println("report user dtos" + listReportTransGroup.size());


            reportsDTO.setValues(transGroupList.size());
            reportsDTO.setMonth(finalI);
            System.out.println("final result" + finalI);
            listReportTransGroup.add(reportsDTO);

        }

        Map<String, Object> msg = Map.of(
                "msg", "Get report of trans_group successfully!",
                "trans_group_report", listReportTransGroup
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    /////////////////// action: delete
    public ResponseEntity deleteUser(Long userId, Long adminId) {
        Boolean isAdmin = isUserAdmin(adminId);
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


        Map<String, Object> msg = Map.of(
                "msg", "Delete user successfully!",
                "user_id", userId
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }


    public ResponseEntity deprecateUser(Long userId, Long adminId) {
        Boolean isAdmin = isUserAdmin(adminId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of("err", "You are not allowed to access this resource!");
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


    public ResponseEntity deleteManga(Long adminId, Long mangaId) {
        Boolean isAdmin = isUserAdmin(adminId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of("err", "You are not allowed to access this resource!");
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        Optional<Manga> mangaOptional = mangaRepos.findById(mangaId);
        if (mangaOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "manga not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        Manga manga = mangaOptional.get();

        mangaRepos.delete(manga);


        Map<String, Object> msg = Map.of(
                "msg", "Delete manga successfully!",
                "manga_id", mangaId
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity deletetransGroup(Long adminId, Long transGroupId) {
        Boolean isAdmin = isUserAdmin(adminId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of("err", "You are not allowed to access this resource!");
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        Optional<TransGroup> transGroupOptional = transGroupRepos.findById(transGroupId);
        if (transGroupOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "transgroup not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        TransGroup transGroup = transGroupOptional.get();


        List<User> userList = (List<User>) transGroup.getUsers();
        List<Manga> mangaList = (List<Manga>) transGroup.getMangas();

        userList.forEach(user1 -> {
            user1.setTransgroup(null);
        });

        mangaList.forEach(manga -> {
            manga.setTransgroup(null);
        });

        transGroupRepos.delete(transGroup);


        Map<String, Object> msg = Map.of(
                "msg", "delete transgroup successfully!",
                "transgroup_id", transGroupId
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    ////////////////// report
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
//        Boolean isAdmin = isUserAdmin(userId);
//        if (!isAdmin) {
//            Map<String, Object> err = Map.of(
//                    "err", "You are not allowed to access this resource!"
//            );
//            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
//                    HttpStatus.FORBIDDEN);
//        }

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


    public ResponseEntity getAllTransGroup(Long userId) {
        Boolean isAdmin = isUserAdmin(userId);
        if (!isAdmin) {
            Map<String, Object> err = Map.of(
                    "err", "You are not allowed to access this resource!"
            );
            return new ResponseEntity<>(new Response(403, HttpStatus.FORBIDDEN, err).toJSON(),
                    HttpStatus.FORBIDDEN);
        }

        List<TransGroup> getTransGroupInfo = transGroupRepos.findAll();

        if (getTransGroupInfo.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty transgroup!",
                    "mangas", getTransGroupInfo
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        Map<String, Object> msg = Map.of(
                "msg", "Get all transgroup successfully!",
                "list_transgroup", getTransGroupInfo
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


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


}
