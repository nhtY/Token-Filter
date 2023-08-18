package authentication.controller;

import authentication.records.PagedResult;
import authentication.records.UserRecord;
import authentication.model.User;
import authentication.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/list-users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(){
//        if (tokenUtil.isTokenExpired(token)) throw new TokenNotFoundError();
//
//        if (!tokenUtil.isThisAdmin(token)) throw new UnauthorizedUser();

        return userService.getAllUsers();
    }

    @GetMapping(value = "/admin/list-users", params = {"page"})
    public PagedResult<User> getAllUsersPaged(@RequestParam Integer page){
//        if (tokenUtil.isTokenExpired(token)) throw new TokenNotFoundError();
//
//        if (!tokenUtil.isThisAdmin(token)) throw new UnauthorizedUser();

        return userService.getAllUsersPaged(page);
    }

    @GetMapping("/admin/find-user")
    public UserRecord findByUsername(@RequestParam String username) {

//        if (tokenUtil.isTokenExpired(token)) throw new TokenNotFoundError();
//
//        if (!tokenUtil.isThisAdmin(token)) throw new UnauthorizedUser();

       return userService.findByUsername(username);
    }

    @PatchMapping("/update-user")
    public ResponseEntity<Object> updateUser(@RequestParam Integer id, @RequestBody User patch,
                                             @RequestHeader("my-token") String token) {
        // Check if admin and not expired:
//        if (tokenUtil.isTokenExpired(token)) throw new TokenNotFoundError();

//        boolean isAdmin = tokenUtil.isThisAdmin(token);

        return ResponseEntity.ok(userService.updateUser(id, patch, token));
    }

    @DeleteMapping("/admin/delete-user")
    public Integer deleteById(@RequestParam Integer id){

//        if (tokenUtil.isTokenExpired(token)) throw new TokenNotFoundError();
//
//        if (!tokenUtil.isThisAdmin(token)) throw new UnauthorizedUser();

        // return deleted id:
        return userService.deleteById(id);
    }


//    @GetMapping("/check-token")
//    public void checkToken(@RequestHeader("my-token") String token) {
//        if (tokenUtil.isTokenExpired(token) || tokenUtil.isTokenExists(token)) throw new TokenNotFoundError();
//    }


}
