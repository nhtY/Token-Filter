package authentication.controller;

import authentication.records.TokenRecord;
import authentication.records.UserRecord;
import authentication.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;;
    }

    @PostMapping("/user/register")
    public UserRecord register(@RequestBody UserRecord userRecord) {
        return authService.register(userRecord);
    }



    @PostMapping("/login")
    public TokenRecord login(
            @RequestParam String username, @RequestParam String password ) {

        return authService.login(username, password);
    }

    @PostMapping("/logout")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void logout(@RequestParam Integer id, @RequestHeader("my-token") String token){
        authService.logout(id, token);
    }

}
