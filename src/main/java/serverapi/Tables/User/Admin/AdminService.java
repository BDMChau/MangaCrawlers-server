package serverapi.Tables.User.Admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverapi.Query.Repository.UserRepos;

@Service
public class AdminService {

    private final UserRepos userRepos;

    @Autowired
    public AdminService(UserRepos userRepos) {
        this.userRepos = userRepos;
    }

}
