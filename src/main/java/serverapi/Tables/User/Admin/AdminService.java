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
import serverapi.Query.Repository.UserRepos;
import serverapi.Tables.User.User;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminService {

    private final UserRepos userRepos;
    private final FollowingRepos followingRepos;
    private final MangaRepos mangaRepos;


    @Autowired
    public AdminService(UserRepos userRepos, FollowingRepos followingRepos, MangaRepos mangaRepos) {
        this.userRepos = userRepos;
        this.followingRepos = followingRepos;
        this.mangaRepos = mangaRepos;
    }

    public ResponseEntity reportUserFollowManga(){

        List<ReportUserFollowMangaDTO> reportUserFollowMangaDTOS = userRepos.findAllFollwingManga(PageRequest.of(0,1));

        if(reportUserFollowMangaDTOS.isEmpty()){
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

    public ResponseEntity reportTopViewManga(){
       List<ReportTopMangaDTO> reportTopMangaDTOS = userRepos.findTopManga(PageRequest.of(0,5));

        if (reportTopMangaDTOS.isEmpty()) {
            Map<String, Object> err = Map.of("msg", "Nothing of top mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of("msg", "Report top five mangas successfully!", "data", reportTopMangaDTOS);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity reportUser(){

            List<UserDTO> getUserInfo = userRepos.getAllUser();
             List<ReportUserDTO> listReportUser = new ArrayList<>();

            for(int i =0; i < 12; i++){

                ReportUserDTO reportUserDTO = new ReportUserDTO();
                int finalI = i+1;
                System.err.println("lỗi"+finalI);
                List<UserDTO> userDTOList = new ArrayList<>();

                getUserInfo.forEach(item->{

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
                    Integer monthOfUser =  Integer.parseInt(simpleDateFormat.format(item.getCreated_at().getTime()));

                    if (monthOfUser == finalI) {

                        userDTOList.add(item);
                        System.out.println("them "+finalI);

                    }


                    });
                System.out.println("reportuserdtolist"+userDTOList);

                System.out.println("report user dtos"+listReportUser.size());


                reportUserDTO.setTotal_user(userDTOList.size());
                reportUserDTO.setMonth(finalI);
                System.out.println("dieu kien dung"+finalI);
               listReportUser.add(reportUserDTO);


            }

        Map<String, Object> msg = Map.of(
                "msg", "Report users successfully!",
                "List report users ",listReportUser);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }



    public ResponseEntity deleteUser(Long userId, Long adminId) {
        Optional<User> adminOptional = userRepos.findById(adminId);
        if (adminOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Missing creadential to access this resource"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User admin = adminOptional.get();

        Boolean isAdmin = admin.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
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


    public ResponseEntity deprecateUser(Long userId, Long adminId) {
        Optional<User> adminOptional = userRepos.findById(adminId);
        if (adminOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Missing creadential to access this resource"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User admin = adminOptional.get();

        Boolean isAdmin = admin.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
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
        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Missing creadential to access this resource"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        Boolean isAdmin = user.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
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
        Optional<User> userOptional = userRepos.findById(userId);
        User user = userOptional.get();
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "Missing creadential to access this resource"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        Boolean isAdmin = user.getUser_isAdmin();
        if (Boolean.FALSE.equals(isAdmin)) {
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
