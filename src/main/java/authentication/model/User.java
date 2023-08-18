package authentication.model;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class User implements Serializable {

    private Integer id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private Role role;

    @NonNull
    private String name;

    @NonNull
    private String surname;

    public enum Role {
        USER, ADMIN
    }
}
