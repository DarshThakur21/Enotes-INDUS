package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.dto.PasswordResetRequestDto;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.compress.PasswordRequiredException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RequestMapping("/api/v1/home")
public interface HomeEndpoint {

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam Integer uid, @RequestParam String code) throws RegisterException, ResourceNotFound;

    @GetMapping("/reset-password-mail")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws MessagingException, ResourceNotFound, UnsupportedEncodingException;

    @GetMapping("/email-verify")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid,@RequestParam String resetCode) throws PasswordRequiredException, RegisterException, ResourceNotFound;

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPasssword(@RequestBody PasswordResetRequestDto passwordResetRequestDto);

}
