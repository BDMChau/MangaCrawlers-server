package serverapi.Auth;

import Security.HashSHA512;
import Helpers.Response;
import StaticFiles.UserAvatar;
import serverapi.Services.Mailer;
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
    private Mailer mailer;

    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }


    public ResponseEntity signUp(SignDto signDto) throws NoSuchAlgorithmException {
        Optional<User> isExistEmail = authRepository.findByEmail(signDto.getUser_email());
        if (isExistEmail.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).jsonObject(), HttpStatus.ACCEPTED);
        }

        HashSHA512 hashingSHA512 = new HashSHA512();
        String hashedPassword = hashingSHA512.hash(signDto.getUser_password());
        signDto.setUser_password(hashedPassword);


        User newUser = new User();
        newUser.setUser_name(signDto.getUser_name());
        newUser.setUser_email(signDto.getUser_email());
        newUser.setUser_password(signDto.getUser_password());
        newUser.setUser_isAdmin(false);
        UserAvatar userAvatar = new UserAvatar();
        if (signDto.isNullAvatar() && !signDto.getIsAdmin()) {
            newUser.setUser_avatar(userAvatar.getAvatar_member());
        }

        authRepository.save(newUser);

        Map<String, String> msg = Map.of("msg", "Sign up success!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).jsonObject(), HttpStatus.OK);
    }


    public ResponseEntity signIn(SignDto signDto) {
        Optional<User> optionalUser = authRepository.findByEmail(signDto.getUser_email());
        if (!optionalUser.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is not existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).jsonObject(), HttpStatus.ACCEPTED);
        }
        User user = optionalUser.get();

        HashSHA512 hashingSHA512 = new HashSHA512();
        Boolean comparePass = hashingSHA512.compare(signDto.getUser_password(), user.getUser_password());
        if (!comparePass) {
            Map<String, String> error = Map.of("err", "Password does not match!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).jsonObject(), HttpStatus.ACCEPTED);
        }


        Long id = user.getUser_id();
        String name = user.getUser_name();
        String email = user.getUser_email();
        String avatar = user.getUser_avatar();
        Boolean isAdmin = user.getUser_isAdmin();
        Map<String, Serializable> userData = Map.of(
                "user_id", id,
                "user_name", name,
                "user_email", email,
                "user_avatar", avatar,
                "user_isAdmin", isAdmin);
        System.out.println("hey lo");
        String token = Jwts.builder()
                .claim("user", userData)
                .signWith(SignatureAlgorithm.HS256, System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8))
                //.setExpiration(new Date(System.currentTimeMillis() + 600000))
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();


        mailer.sendMail("bdmchau105@gmail.com");

        Map<String, Object> msg = Map.of(
                "msg", "Sign in success",
                "token", token,
                "user", userData
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).jsonObject(), HttpStatus.OK);
    }
}
