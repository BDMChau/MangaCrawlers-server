package Helpers;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class Response {
    private int httpCode;
    private HttpStatus httpStatus;
    private Object content;

    public Response(int httpCode, HttpStatus httpStatus, Object content) {
        this.httpCode = httpCode;
        this.httpStatus = httpStatus;
        this.content = content;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Object getContent() {
        return content;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setContent(Object content) {
        this.content = content;
    }


    public HashMap jsonObject() {
        HashMap hashMap = new HashMap();
        hashMap.put("http_code", this.httpCode);
        hashMap.put("http_status", this.httpStatus);
        hashMap.put("content", this.content);

        return hashMap;
    }
}
