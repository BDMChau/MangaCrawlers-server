package serverapi.socket.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessage {
    private Integer type;
    private Long userId;
    private List listTo;
    private Map objData;
    private String message;
}
