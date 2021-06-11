package serverapi.Api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class Response {
    private int httpCode;
    private HttpStatus httpStatus;
    private Object content;

    public Response(int httpCode, HttpStatus httpStatus, Object content) {
        this.httpCode = httpCode;
        this.httpStatus = httpStatus;
        this.content = content;
    }

    public HashMap toJSON() {
        HashMap hashMap = new HashMap();
        hashMap.put("http_code", this.httpCode);
        hashMap.put("http_status", this.httpStatus);
        hashMap.put("content", this.content);

        return hashMap;
    }
}
