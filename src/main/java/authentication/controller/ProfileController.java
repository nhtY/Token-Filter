package authentication.controller;

import authentication.model.User;
import authentication.service.IProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin("http://localhost:3000")
public class ProfileController {

    private final IProfileService profileService;

    public ProfileController(IProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<Object> home(@RequestHeader("my-token") String token){
        return ResponseEntity.ok(profileService.getProfileData(token));
    }

    @PatchMapping
    public ResponseEntity<Object> updateProfile(@RequestParam Integer id, @RequestBody User patch, @RequestHeader("my-token") String token) {
        return ResponseEntity.ok(profileService.updateProfile(id, patch, token));
    }
}
