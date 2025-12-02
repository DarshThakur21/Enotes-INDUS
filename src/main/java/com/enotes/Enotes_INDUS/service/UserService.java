package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.PasswordChangeRequest;
import com.enotes.Enotes_INDUS.dto.PasswordResetRequestDto;
import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.compress.PasswordRequiredException;

import java.io.UnsupportedEncodingException;

public interface UserService {
     void resetPassword(PasswordResetRequestDto passwordResetRequestDto);


    Boolean changePassword(PasswordChangeRequest passwordChangeRequest);

    void sendEmailPasswordReset(String email, HttpServletRequest request) throws ResourceNotFound, MessagingException, UnsupportedEncodingException;

    void verifyReset(Integer uid, String resetCode) throws RegisterException, PasswordRequiredException, ResourceNotFound;
}
