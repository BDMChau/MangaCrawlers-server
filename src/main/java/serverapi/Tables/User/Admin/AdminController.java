package serverapi.Tables.User.Admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.Tables.User.POJO.UserPOJO;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CacheConfig(cacheNames = {"admin"})
public class AdminController {


    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    public Map getUserAttribute(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        Map user = (HashMap) req.getAttribute("user");
        return user;
    }


    ////////////////////////////////////////////////

    @GetMapping("/reporttotaluserfollowmanga")
    public ResponseEntity reportUserFollowManga() {


        return adminService.reportUserFollowManga();

    }

    @GetMapping("/reporttopviewsmanga")
    public ResponseEntity reportTopViewsManga() {

        return adminService.reportTopViewManga();

    }

    @GetMapping("/reportuser")
    public ResponseEntity reportUser() {


        return adminService.reportUser();
    }


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


    @CacheEvict(allEntries = true, value = {"allusers"})
    @PutMapping("/deprecateuser")
    public ResponseEntity deprecateUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {

        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return adminService.deprecateUser(userId, adminId);
    }


    // Delete User by userId
    @CacheEvict(allEntries = true, value = {"allusers"})
    @DeleteMapping("/deleteuser")
    public ResponseEntity deleteUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {
        String StrUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(StrUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return adminService.deleteUser(userId, adminId);
    }


}
