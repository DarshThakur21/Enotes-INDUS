package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.dto.EditUserDto;
import com.enotes.Enotes_INDUS.dto.PasswordChangeRequest;
import com.enotes.Enotes_INDUS.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/edit-user")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> editUser(@RequestBody EditUserDto userDto);


}
