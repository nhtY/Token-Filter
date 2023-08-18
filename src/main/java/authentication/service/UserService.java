package authentication.service;

import authentication.error.DeleteError;
import authentication.model.User;
import authentication.records.PagedResult;
import authentication.records.UserRecord;
import authentication.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements IUserService{

    private final IUserRepository userRepo;

    public UserService(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepo.fetchAll();
    }

    @Override
    @Transactional
    public PagedResult<User> getAllUsersPaged(Integer page) {
        return userRepo.fetchAllPaged(page);
    }

    @Override
    @Transactional
    public UserRecord findByUsername(String username) {
        Optional<User> userFound = userRepo.findByUsername(username);
        User user;
        if(userFound.isPresent()){
            user=userFound.get();
            return new UserRecord(user.getId(), user.getUsername(), null, user.getName(),
                    user.getSurname(), user.getRole());
        }

        throw new RuntimeException("User with provided username not found.");
    }

    @Override
    @Transactional
    public User updateUser(Integer id, User patch, String token) {

        boolean isAdmin = userRepo.isAdmin(token);

        User user = userRepo.findUserById(id).get();
        log.info("User before update: {}", user);

        if (patch.getUsername() != null) {
            user.setUsername(patch.getUsername());
        }

        if (patch.getPassword() != null) {
            user.setPassword(patch.getPassword());
        }

        if (patch.getName() != null) {
            user.setName(patch.getName());
        }

        if (patch.getSurname() != null) {
            user.setSurname(patch.getSurname());
        }

        if (isAdmin && patch.getRole().toString() != null) {
            User.Role role = patch.getRole().toString().equals("ADMIN")?
                    User.Role.ADMIN : User.Role.USER;
            user.setRole(role);
        }
        userRepo.updateUser(user);

        // on frontend do not use returned user object's password field. Otherwise, it will show the one in the Database.
        user.setPassword(patch.getPassword());

        log.info("user after update: {}", user);

        return user;
    }

    @Override
    @Transactional
    public Integer deleteById(Integer id) {

        if (userRepo.deleteById(id) == 0) {
            throw new DeleteError();
        }

        // return deleted id:
        return id;
    }
}
