package Security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@NoArgsConstructor
public class TokenService {

    public String genHS256() {
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8))
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();

        return token;
    }

    public String genHS256(int milisecondExpired) {
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8))
                .setExpiration(new Date(System.currentTimeMillis() + milisecondExpired))
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();

        return token;
    }

    public String genHS256(Object payload) {
        String token = Jwts.builder()
                .claim("payload", payload)
                .signWith(SignatureAlgorithm.HS256, System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8))
                //.setExpiration(new Date(System.currentTimeMillis() + 600000))
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();

        return token;
    }

    public String genHS256(Object payload, int milisecondExpired) {
        String token = Jwts.builder()
                .claim("payload", payload)
                .signWith(SignatureAlgorithm.HS256, System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8))
                .setExpiration(new Date(System.currentTimeMillis() + milisecondExpired))
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();

        return token;
    }


}
