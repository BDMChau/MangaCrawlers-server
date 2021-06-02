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
import serverapi.Query.DTO.UserDTO;
import serverapi.Query.Repository.FollowingRepos;
import serverapi.Query.Repository.MangaRepos;
import serverapi.Query.Repository.UserRepos;
import serverapi.Tables.User.User;

import javax.sound.midi.Soundbank;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.logging.SimpleFormatter;

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

}
