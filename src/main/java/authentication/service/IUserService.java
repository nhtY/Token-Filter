package authentication.service;

import authentication.model.User;
import authentication.records.PagedResult;
import authentication.records.UserRecord;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    PagedResult<User> getAllUsersPaged(Integer page);
    UserRecord findByUsername(String username);
    User updateUser(Integer id, User patch, String token);
    Integer deleteById(Integer id);
}
