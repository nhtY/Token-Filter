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

    private static final int EXPIRATION_IN_MINUTES = 1;

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

}
