package authentication.records;

import authentication.model.User;

public record UserRecord(Integer id, String username, String password, String name, String surname, User.Role role) {

    public UserRecord withId(Integer id) {
        return new UserRecord(id, username(), password(), name(), surname(), role());
    }
}
