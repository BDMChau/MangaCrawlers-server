package serverapi.Tables.User.Admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {


    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/reporttotaluserfollowmanga")
    public ResponseEntity reportUserFollowManga() {


      return  adminService.reportUserFollowManga();

    }
    @GetMapping("/reporttopviewsmanga")
    public ResponseEntity reportTopViewsManga(){

        return adminService.reportTopViewManga();

    }




}
