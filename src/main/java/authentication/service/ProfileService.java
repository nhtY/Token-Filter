package authentication.service;

import authentication.error.UserNotFound;
import authentication.model.User;
import authentication.records.UserRecord;
import authentication.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ProfileService implements IProfileService {

    private final IUserRepository userRepo;

    @Autowired
    public ProfileService(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    @Transactional
    public UserRecord getProfileData(String token) {

        User user = userRepo.getUserIfTokenExists(token).orElse(null);

        if(user != null) {
            return new UserRecord(user.getId(), user.getUsername(), null, user.getName(),
                    user.getSurname(), user.getRole());
        }

        log.warn("Logging GET profile data: User not found.");
        throw new UserNotFound();
    }

    @Override
    @Transactional
    public User updateProfile(Integer id, User patch, String token) {
        boolean isAdmin = userRepo.isAdmin(token);

        User user = userRepo.findUserById(id).get();
        log.info("User before update: {}", user);
        log.info("Patch is: {}", patch);

        user.setUsername(patch.getUsername()); // @NonNull, so no need to check if null

        user.setPassword(patch.getPassword()); // @NonNull, so no need to check if null

        if (patch.getName() != null) {
            user.setName(patch.getName());
        }

        if (patch.getSurname() != null) {
            user.setSurname(patch.getSurname());
        }

        if (isAdmin && patch.getRole() != null) {
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
}
