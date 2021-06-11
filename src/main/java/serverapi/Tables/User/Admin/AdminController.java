package serverapi.Tables.User.Admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.Api.Response;
import serverapi.Query.Repository.UserRepos;
import serverapi.Tables.Manga.POJO.MangaPOJO;
import serverapi.Tables.User.POJO.TransGroupPOJO;
import serverapi.Tables.User.POJO.UserPOJO;
import serverapi.Tables.User.User;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CacheConfig(cacheNames = {"admin"})

public class AdminController {


    private final AdminService adminService;
    private final UserRepos userRepos;

    @Autowired
    public AdminController(AdminService adminService, UserRepos userRepos) {
        this.adminService = adminService;
        this.userRepos = userRepos;
    }


    public Map getUserAttribute(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        Map user = (HashMap) req.getAttribute("user");
        return user;
    }


    /////////////////////////////// get
    @Cacheable(value = "allmangas", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/getallmangas")
    public ResponseEntity getAllMangas(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return adminService.getAllMangas(userId);
    }


    @Cacheable(value = "allusers", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/getallusers")
    public ResponseEntity getAllUsers(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return adminService.getAllUsers(userId);
    }

    @GetMapping("/getalltransgroup")
    public ResponseEntity getAllTransGroup(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return adminService.getAllTransGroup(userId);
    }


    ///////////////// interact
    @CacheEvict(allEntries = true, value = {"allusers"})
    @PutMapping("/deprecateuser")
    public ResponseEntity deprecateUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {

        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return adminService.deprecateUser(userId, adminId);
    }


    @CacheEvict(allEntries = true, value = {"allusers"})
    @DeleteMapping("/deleteuser")
    public ResponseEntity deleteUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return adminService.deleteUser(userId, adminId);
    }



    @CacheEvict(allEntries = true, value = {"allmangas"})
    @DeleteMapping("/deletemanga")
    public ResponseEntity deleteManga(@RequestBody MangaPOJO mangaPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return adminService.deleteManga(adminId, mangaId);
    }



    @DeleteMapping("/deletetransgroup")
    public ResponseEntity deleteTransGroup(@RequestBody TransGroupPOJO transGroupPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long transGroupId = Long.parseLong(transGroupPOJO.getTransgroup_id());

        return adminService.deletetransGroup(adminId, transGroupId);
    }


    /////////////////////////// chart report
    @GetMapping("/reporttransgroup")
    public ResponseEntity reportTransGroup(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return adminService.reportTransGroup(userId);
    }


    @GetMapping("/reportmanga")
    public ResponseEntity reportManga(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return adminService.reportManga(userId);
    }


    @GetMapping("/reportuser")
    public ResponseEntity reportUser(ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(StrUserId);

        return adminService.reportUser(userId);
    }


    @GetMapping("/reporttotaluserfollowmanga")
    public ResponseEntity reportUserFollowManga() {

        return adminService.reportUserFollowManga();
    }


    @GetMapping("/reporttopviewsmanga")
    public ResponseEntity reportTopViewsManga() {

        return adminService.reportTopViewManga();
    }


}
