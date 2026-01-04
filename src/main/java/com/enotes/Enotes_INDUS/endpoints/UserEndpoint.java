package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.dto.PasswordChangeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.enotes.Enotes_INDUS.utils.Constants.ROLE_ADMIN;
import static com.enotes.Enotes_INDUS.utils.Constants.ROLE_USER;

@RequestMapping("/api/v1/user")
public interface UserEndpoint {

    @GetMapping("/user-profiles")
    @PreAuthorize(ROLE_ADMIN)
    public ResponseEntity<?> getAllUserProfiles();

    @PostMapping("/change-password")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest);
}
