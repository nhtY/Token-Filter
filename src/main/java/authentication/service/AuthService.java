package authentication.service;

import authentication.error.DeleteError;
import authentication.model.User;
import authentication.records.TokenRecord;
import authentication.records.UserRecord;
import authentication.repository.ITokenRepository;
import authentication.repository.IUserRepository;
import authentication.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class AuthService implements IAuthService{

    private final IUserRepository userRepo;
    private final ITokenRepository tokenRepo;

    @Autowired
    public AuthService(IUserRepository userRepo, ITokenRepository tokenRepo) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
    }

    @Override
    @Transactional
    public UserRecord register(UserRecord userRecord) {
        log.info("retrieved userRecord is: {}", userRecord);

        User savedUser = userRepo.save(User.of(userRecord.username(), userRecord.password(), User.Role.USER,
                userRecord.name(), userRecord.surname()));

        log.info("Registered user is: {}", savedUser);

        return new UserRecord(
                savedUser.getId(), savedUser.getUsername(), userRecord.password(), savedUser.getName(),
                savedUser.getSurname(), savedUser.getRole()
        );
    }

    @Override
    @Transactional
    public TokenRecord login(String username, String password) {
        Optional<User> userFound = userRepo.findUser(username, password);
        User user;
        TokenRecord tokenRecord;

        if(userFound.isPresent()){
            user = userFound.get();
            String generatedToken = TokenUtil.createUserToken();
            tokenRecord = new TokenRecord(user.getId(), generatedToken);
            return tokenRepo.saveToken(tokenRecord);
        }

        throw new RuntimeException("Provided username or password is wrong.");
    }

    @Override
    @Transactional
    public void logout(Integer id, String token) {
        int affectedRowCount = tokenRepo.deleteToken(new TokenRecord(id, token));
        log.info("Logout, {} Token(s) Deleted: {}", affectedRowCount, token);

        // TODO : kullanıcıya token'ın silinmediğini göstermeli mi??
        if (affectedRowCount == 0) throw new DeleteError();
    }
}
