package serverapi.tables.manga_tables.manga.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.query.dtos.features.FriendDTO;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FriendPOJO {
    private String to_user_id;
    private List<FriendDTO> listFriends = new ArrayList<>();
    private  String status_id;
}
