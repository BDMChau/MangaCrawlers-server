package serverapi.api;

import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

    public void ResponseJsonMiddleware(HttpServletResponse res, Gson gson, String contentType, String charSet, int httpCode,
                                       HttpStatus httpStatus, Object content) throws IOException {
        PrintWriter printWriter = res.getWriter();

        res.setContentType(contentType);
        res.setCharacterEncoding(charSet);

        Response responseHelper = new Response(httpCode, httpStatus, content);
        String responseHelperString = gson.toJson(responseHelper);

        printWriter.print(responseHelperString);
        printWriter.flush();
    }
}
