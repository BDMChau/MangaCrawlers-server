package serverapi.Auth;

import Helpers.HashSHA512;
import Helpers.Response;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Auth.dto.SignDto;
import serverapi.Tables.User.User;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;


@Service
public class AuthService {


    private final AuthRepository authRepository;

    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }


    public ResponseEntity signUp(SignDto signDto) throws NoSuchAlgorithmException {
        Optional<User> isExistEmail = authRepository.findByEmail(signDto.getEmail());
        if (isExistEmail.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).jsonObject(), HttpStatus.ACCEPTED);
        }

        HashSHA512 hashingSHA512 = new HashSHA512();
        String hashedPassword = hashingSHA512.hash(signDto.getPassword());
        signDto.setPassword(hashedPassword);

        User newUser = new User(
                signDto.getName(),
                signDto.getEmail(),
                signDto.getPassword(),
                null,
                false
        );
        authRepository.save(newUser);

        Map<String, String> msg = Map.of("msg", "Sign up success!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).jsonObject(), HttpStatus.OK);
    }


    public ResponseEntity signIn(SignDto signDto) {
        Optional<User> optionalUser = authRepository.findByEmail(signDto.getEmail());
        if (!optionalUser.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is not existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).jsonObject(), HttpStatus.ACCEPTED);
        }
        User user = optionalUser.get();

        HashSHA512 hashingSHA512 = new HashSHA512();
        Boolean comparePass = hashingSHA512.compare(signDto.getPassword(), user.getUser_password());
        if (!comparePass) {
            Map<String, String> error = Map.of("err", "Password does not match!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).jsonObject(), HttpStatus.ACCEPTED);
        }


        Long id = user.getUser_id();
        String name = user.getUser_name();
        String email = user.getUser_email();
        Map<String, Serializable> userData = Map.of("id", id, "name", name,"email", email);

        String token = Jwts.builder()
                .claim("user", userData)
                .signWith(SignatureAlgorithm.HS256, System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8))
                //.setExpiration(new Date(System.currentTimeMillis() + 600000))
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();


        Map<String, Object> msg = Map.of(
                "msg", "Sign in success",
                "token", token,
                "user", userData
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).jsonObject(), HttpStatus.OK);
    }
}
