package authentication.utils;

import authentication.error.TokenNotFoundError;
import authentication.model.User;
import authentication.repository.JdbcTokenRepository;
import authentication.repository.JdbcUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class TokenUtil {


    private static final int EXPIRATION_IN_MINUTES = 30;

//    private final JdbcTokenRepository tokenRepo;
//    private final JdbcUserRepository userRepo;
//
//    public TokenUtil(JdbcTokenRepository tokenRepo, JdbcUserRepository userRepo) {
//        this.tokenRepo = tokenRepo;
//        this.userRepo = userRepo;
//    }

//    @Transactional
//    public boolean isTokenExists(String token) {
//        return tokenRepo.findToken(token).orElse(null) == null;
//    }

    // TODO: sadece bu method kalsın.... diğerlerini ise repo'lar üzerinden doğrudan ilgili yerlerde kullan
    public boolean isTokenExpired(String token) {
        if (token== null || token.isEmpty()) throw new TokenNotFoundError();

        try {
            Date date = new Date();
            return date.getTime() > Long.valueOf(token.split("-")[0]);
        }catch (Exception e){
            log.error("Token expired check error: {}", e);
            throw new TokenNotFoundError();
        }
    }

    public static String createUserToken() {
        StringBuilder token = new StringBuilder();
        Long tokenExpirationDuration = EXPIRATION_IN_MINUTES * 60 * 1000L;  // 2 minutes
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + tokenExpirationDuration);
        long currentTimeInMillisecond = timestamp.getTime();
        return token.append(currentTimeInMillisecond).append("-")
                .append(UUID.randomUUID()).toString();
    }

//    @Transactional
//    public User getUserForToken(String token) {
//        return userRepo.getUserIfTokenExists(token).orElse(null);
//    }
//
//    @Transactional
//    public Boolean isThisAdmin(String token) {
//        return userRepo.isAdmin(token);
//    }

}
