package authentication.service;

import authentication.model.User;
import authentication.records.UserRecord;

public interface IProfileService {

    UserRecord getProfileData(String token);

    User updateProfile(Integer id, User patch, String token);
}
