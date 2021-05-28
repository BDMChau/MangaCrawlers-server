package serverapi.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Authentication.POJO.SignPOJO;
import serverapi.Security.HashSHA512;
import serverapi.Security.RandomBytes;
import serverapi.Security.TokenService;
import serverapi.SharedServices.Mailer;
import serverapi.StaticFiles.UserAvatarCollection;
import serverapi.Tables.TransGroup.TransGroup;
import serverapi.Tables.User.User;

import javax.mail.MessagingException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class AuthService {
    private final AuthRepository authRepository;

    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Autowired
    Mailer mailer;


    public ResponseEntity signUp(SignPOJO signPOJO) throws NoSuchAlgorithmException, MessagingException {
        Optional<User> userOptional = authRepository.findByEmail(signPOJO.getUser_email());
        if (userOptional.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }

        HashSHA512 hashingSHA512 = new HashSHA512();
        String hashedPassword = hashingSHA512.hash(signPOJO.getUser_password());
        signPOJO.setUser_password(hashedPassword);


        User newUser = new User();
        newUser.setUser_name(signPOJO.getUser_name());
        newUser.setUser_email(signPOJO.getUser_email());
        newUser.setUser_password(signPOJO.getUser_password());
        newUser.setUser_isAdmin(false);
        newUser.setUser_isVerified(false);

        UserAvatarCollection userAvatarCollection = new UserAvatarCollection();
        if (signPOJO.isNullAvatar()) {
            newUser.setUser_avatar(userAvatarCollection.getAvatar_member());
        } else {
            newUser.setUser_avatar(signPOJO.getUser_avatar());
        }

        // send mail to verify account
        Integer ThreeDays = (1000 * 60 * 60 * 24) * 3; // 3 days in miliseconds
        String timeExpired =
                String.valueOf(Calendar.getInstance().getTimeInMillis() + Long.parseLong(String.valueOf(ThreeDays)));
        String token = new RandomBytes().randomBytes(32);
        String hashedToken = new HashSHA512().hash(token);
        newUser.setToken_verify(hashedToken);
        newUser.setToken_verify_createdAt(timeExpired);


        String userName = signPOJO.getUser_name();
        String userEmail = signPOJO.getUser_email();
        mailer.sendMailToVerifyAccount(userName, userEmail, hashedToken);


        authRepository.save(newUser);

        Map<String, String> msg = Map.of(
                "msg", "Sign up success!",
                "msg2", "Check email to verify the account!"
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity signIn(SignPOJO signPOJO) {
        Optional<User> optionalUser = authRepository.findByEmail(signPOJO.getUser_email());
        if (!optionalUser.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is not existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }
        User user = optionalUser.get();

        HashSHA512 hashingSHA512 = new HashSHA512();
        Boolean comparePass = hashingSHA512.compare(signPOJO.getUser_password(), user.getUser_password());
        if (!comparePass) {
            Map<String, String> error = Map.of("err", "Password does not match!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }

        Boolean isVerified = user.getUser_isVerified();
        if (Boolean.FALSE.equals(isVerified)) {
            Map<String, String> error = Map.of("err", "Check email to verify the account!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }


        Long id = user.getUser_id();
        String name = user.getUser_name();
        String email = user.getUser_email();
        String avatar = user.getUser_avatar();
        Boolean isAdmin = user.getUser_isAdmin();
        TransGroup transGroup = user.getTransgroup();

        Map<String, Serializable> userData = new HashMap<>();
        if (transGroup != null) {
            userData.put("user_id", id);
            userData.put("user_name", name);
            userData.put("user_email", email);
            userData.put("user_avatar", avatar);
            userData.put("user_isAdmin", isAdmin);
            userData.put("user_isVerified", isVerified);
            userData.put("user_transgroup_id", transGroup.getTransgroup_id());

        } else {
            userData.put("user_id", id);
            userData.put("user_name", name);
            userData.put("user_email", email);
            userData.put("user_avatar", avatar);
            userData.put("user_isAdmin", isAdmin);
            userData.put("user_isVerified", isVerified);
        }


        TokenService tokenService = new TokenService();
        String token = tokenService.genHS256(userData);

//        String token = Jwts.builder()
//                .claim("user", userData)
//                .signWith(SignatureAlgorithm.HS256, System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8))
//                //.setExpiration(new Date(System.currentTimeMillis() + 600000))
//                .compressWith(CompressionCodecs.DEFLATE)
//                .compact();

        Map<String, Object> msg = Map.of(
                "msg", "Sign in success",
                "token", token,
                "user", userData
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity requestChangePassword(SignPOJO signPOJO) throws MessagingException, NoSuchAlgorithmException {
        Optional<User> userOptional = authRepository.findByEmail(signPOJO.getUser_email());
        if (!userOptional.isPresent()) {
            Map<String, String> error = Map.of("err", "Email is not existed!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
        }

        User user = userOptional.get();

        String timeExpired = String.valueOf(Calendar.getInstance().getTimeInMillis() + 600000); // 10 minutes

        String token = new RandomBytes().randomBytes(32);
        String hashedToken = new HashSHA512().hash(token);
        user.setToken_reset_pass(hashedToken);
        user.setToken_reset_pass_createdAt(timeExpired);

        String userEmail = user.getUser_email();
        String userName = user.getUser_name();
        mailer.sendMailToChangePass(userName, userEmail, hashedToken);

        authRepository.save(user);

        Map<String, String> msg = Map.of("msg", "A mail is sent, please check your email!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity changePassword(SignPOJO signPOJO) throws NoSuchAlgorithmException {
        Long currentTime = Calendar.getInstance().getTimeInMillis(); // by miliseconds
        String token = signPOJO.getUser_change_pass_token();

        Optional<User> userOptional = authRepository.findByTokenResetPass(token);
        if (userOptional.isEmpty()) {
            Map<String, String> err = Map.of("err", "Token verification is failed!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        Long tokenExpiredAt = Long.parseLong(user.getToken_reset_pass_createdAt());
        if (currentTime >= tokenExpiredAt || tokenExpiredAt == null) {
            Map<String, String> err = Map.of("err", "Token has expired!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        String newPasswrod = signPOJO.getUser_password();
        String hashedNewPassword = new HashSHA512().hash(newPasswrod);
        user.setUser_password(hashedNewPassword);

        user.setToken_reset_pass_createdAt(null);
        user.setToken_reset_pass(null);
        authRepository.save(user);

        Map<String, String> msg = Map.of("msg", "Change password successfully");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity confirmVerification(SignPOJO signPOJO) {
        long currentTime = Calendar.getInstance().getTimeInMillis(); // by miliseconds
        String token = signPOJO.getUser_verify_token();

        Optional<User> userOptional = authRepository.findByTokenVerify(token);
        if (userOptional.isEmpty()) {
            Map<String, String> err = Map.of("err", "Token verification is failed!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();


        Long tokenExpiredAt = Long.parseLong(user.getToken_verify_createdAt());
        if (currentTime >= tokenExpiredAt || tokenExpiredAt == null) {
            user.setToken_verify(null);
            user.setToken_verify_createdAt(null);

            Map<String, String> err = Map.of(
                    "err", "Token has expired!",
                    "err2", "Contact the admin for more information!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }


        user.setUser_isVerified(true);
        user.setToken_verify(null);
        user.setToken_verify_createdAt(null);
        authRepository.save(user);

        Map<String, String> msg = Map.of("msg", "Verify account successfully!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
