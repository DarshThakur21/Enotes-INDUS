package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.dto.PasswordResetRequestDto;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.compress.PasswordRequiredException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.enotes.Enotes_INDUS.utils.Constants.ROLE_USER;


import java.io.UnsupportedEncodingException;

@RequestMapping("/api/v1/home")
public interface HomeEndpoint {

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam Integer uid, @RequestParam String code) throws RegisterException, ResourceNotFound;

    @GetMapping("/reset-password-mail")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws MessagingException, ResourceNotFound, UnsupportedEncodingException;

    @GetMapping("/email-verify")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid,@RequestParam String resetCode) throws PasswordRequiredException, RegisterException, ResourceNotFound;

    @PostMapping("/reset-password")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> resetPasssword(@RequestBody PasswordResetRequestDto passwordResetRequestDto);

}
