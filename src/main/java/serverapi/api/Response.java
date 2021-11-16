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
    private int http_code;
    private HttpStatus http_status;
    private Object content;

    public Response(int http_code, HttpStatus http_status, Object content) {
        this.http_code = http_code;
        this.http_status = http_status;
        this.content = content;
    }

    public HashMap toJSON() {
        HashMap hashMap = new HashMap();
        hashMap.put("http_code", this.http_code);
        hashMap.put("http_status", this.http_status);
        hashMap.put("content", this.content);

        return hashMap;
    }

    public void ResponseJsonMiddleware(HttpServletResponse res, Gson gson, String contentType, String charSet, int http_code,
                                       HttpStatus http_status, Object content) throws IOException {
        PrintWriter printWriter = res.getWriter();

        res.setContentType(contentType);
        res.setCharacterEncoding(charSet);

        Response responseHelper = new Response(http_code, http_status, content);
        String responseHelperString = gson.toJson(responseHelper);

        printWriter.print(responseHelperString);
        printWriter.flush();
    }
}
