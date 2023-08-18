package authentication.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "Delete failed: provided (u)id or token not found")
public class DeleteError extends RuntimeException{
}
