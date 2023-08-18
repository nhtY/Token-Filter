package authentication.repository;

import authentication.model.User;
import authentication.records.PagedResult;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    /**
     * Takes two parameters
     * @param username
     * @param password
     * @return Optional<User>
     */
    Optional<User> findUser(String username, String password);
    Optional<User> findUserById(Integer id);
    Optional<User> findByUsername(String username);
    Optional<User> getUserIfTokenExists(String token);
    User save(User user);

    User updateUser(User user);
    Integer deleteById(Integer id);

    Boolean isAdmin(String token);

    List<User> fetchAll();
    PagedResult<User> fetchAllPaged(int page);
}
