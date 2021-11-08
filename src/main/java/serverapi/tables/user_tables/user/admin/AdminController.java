package serverapi.tables.user_tables.user.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.query.repository.user.UserRepos;


import serverapi.tables.manga_tables.manga.pojo.MangaPOJO;
import serverapi.tables.user_tables.user.admin.AdminService;
import serverapi.tables.user_tables.user.pojo.MangaChapterPOJO;
import serverapi.tables.user_tables.user.pojo.TransGroupPOJO;
import serverapi.tables.user_tables.user.pojo.UserPOJO;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);

        return adminService.getAllMangas(userId);
    }


    @Cacheable(value = "allusers", key = "#request.getAttribute(\"user\").get(\"user_id\")")
    @GetMapping("/getallusers")
    public ResponseEntity getAllUsers(ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);

        return adminService.getAllUsers(userId);
    }

    @GetMapping("/getallposts")
    public ResponseEntity getAllPosts(ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);

        return adminService.getAllPosts(userId);
    }

    @GetMapping("/getalltransgroup")
    public ResponseEntity getAllTransGroup(ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);

        return adminService.getAllTransGroup(userId);
    }


    ///////////////// interact
    @CacheEvict(allEntries = true, value = {"allusers"})
    @PutMapping("/deprecateuser")
    public ResponseEntity deprecateUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {

        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(strUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return adminService.deprecateUser(userId, adminId);
    }


    @CacheEvict(allEntries = true, value = {"allusers"})
    @DeleteMapping("/deleteuser")
    public ResponseEntity deleteUser(@RequestBody UserPOJO userPOJO, ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(strUserId);

        Long userId = Long.parseLong(userPOJO.getUser_id());

        return adminService.deleteUser(userId, adminId);
    }


    @CacheEvict(allEntries = true, value = {"allmangas"})
    @DeleteMapping("/deletemanga")
    public ResponseEntity deleteManga(@RequestBody MangaPOJO mangaPOJO, ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(strUserId);

        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return adminService.deleteManga(adminId, mangaId);
    }


    @DeleteMapping("/deletetransgroup")
    public ResponseEntity deleteTransGroup(@RequestBody TransGroupPOJO transGroupPOJO, ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(strUserId);

        Long transGroupId = Long.parseLong(transGroupPOJO.getTransgroup_id());

        return adminService.deletetransGroup(adminId, transGroupId);
    }

    @CacheEvict(allEntries = true, value = {"mangaPage"})
    @PutMapping("/editmanga")
    public ResponseEntity editManga(@RequestBody MangaChapterPOJO mangaChapterPOJO, ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(strUserId);

        Long mangaId = mangaChapterPOJO.getManga_id();
        String mangaName = mangaChapterPOJO.getManga_name();
        Long authorId = mangaChapterPOJO.getAuthor_id();
        String authorName = mangaChapterPOJO.getAuthor_name();

        return adminService.editManga(adminId, mangaId, mangaName, authorId, authorName);
    }

    @CacheEvict(allEntries = true, value = {"mangaPage"})
    @PutMapping("/editchapter")
    public ResponseEntity editChapter(@RequestBody MangaChapterPOJO mangaChapterPOJO, ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(strUserId);

        Long chapterId = mangaChapterPOJO.getChapter_id();
        String chapterName = mangaChapterPOJO.getChapter_name();

        return adminService.editChapter(adminId, chapterId, chapterName);
    }

    @CacheEvict(allEntries = true, value = {"mangaPage"})
    @DeleteMapping("/deletechapter")
    public ResponseEntity deleteChapter(@RequestBody MangaChapterPOJO mangaChapterPOJO, ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long adminId = Long.parseLong(strUserId);

        Long chapterId = mangaChapterPOJO.getChapter_id();

        return adminService.deleteChapter(adminId, chapterId);
    }

    /////////////////////////// chart report
    @GetMapping("/reporttransgroup")
    public ResponseEntity reportTransGroup(ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);

        return adminService.reportTransGroup(userId);
    }


    @GetMapping("/reportmanga")
    public ResponseEntity reportManga(ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);

        return adminService.reportManga(userId);
    }


    @GetMapping("/reportuser")
    public ResponseEntity reportUser(ServletRequest request) {
        String strUserId = getUserAttribute(request).get("user_id").toString();
        Long userId = Long.parseLong(strUserId);

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
