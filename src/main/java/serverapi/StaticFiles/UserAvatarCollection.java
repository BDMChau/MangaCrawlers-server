package serverapi.StaticFiles;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAvatarCollection {
    private String avatar_member = "https://res.cloudinary.com/mangacrawlers/image/upload/v1627542120/users_avatar/default/360_F_362562495_Gau0POzcwR8JCfQuikVUTqzMFTo78vkF.jpg";
    private String avatar_admin = "https://res.cloudinary.com/mangacrawlers/image/upload/v1627542113/users_avatar/default/128-1280822_check-mark-box-clip-art-blue-admin-icon.png";
    private String avatar;


    public UserAvatarCollection(String avatar) {
        this.avatar = avatar;
    }

    public Boolean isExited() {
        if (avatar == null || avatar.equals("")) {
            return false;
        } else {
            return true;
        }
    }
}
