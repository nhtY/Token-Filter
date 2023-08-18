package authentication.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "Expired or invalid token.")
public class TokenNotFoundError extends RuntimeException{
}