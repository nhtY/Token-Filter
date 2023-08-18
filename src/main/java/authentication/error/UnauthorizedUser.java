package authentication.error;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason = "You are not authorized")
public class UnauthorizedUser extends RuntimeException{
}
