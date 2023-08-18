package authentication.service;

import authentication.records.TokenRecord;
import authentication.records.UserRecord;

public interface IAuthService {
    UserRecord register(UserRecord userRecord);

    TokenRecord login(String username, String password);

    void logout(Integer id, String token);
}
