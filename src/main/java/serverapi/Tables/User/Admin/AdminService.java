package serverapi.Tables.User.Admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.DTO.ReportTopMangaDTO;
import serverapi.Query.DTO.ReportUserDTO;
import serverapi.Query.DTO.ReportUserFollowMangaDTO;
import serverapi.Query.Repository.FollowingRepos;
import serverapi.Query.Repository.MangaRepos;
import serverapi.Query.Repository.UserRepos;

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


        Calendar time = Calendar.getInstance();
        time.get(Calendar.JUNE);
        System.out.println("thoi gian"+time);

//        time.set(Calendar.MONTH,Calendar.MAY + 1) ;
        List<ReportUserDTO> reportUserDTOS = userRepos.getUser();
        reportUserDTOS.forEach(item->{
            if(item.getCreatedAt().equals(time.get(Calendar.JUNE))){
                item.getUser_isVerified();
                item.getUser_avatar();
                item.getUser_email();
                item.getUser_name();
                item.getCreatedAt();
            }
        });

        Map<String, Object> msg = Map.of("msg", "Report top five mangas successfully!", "data",reportUserDTOS);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }

}
