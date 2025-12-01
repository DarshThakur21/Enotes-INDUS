package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.dto.PasswordChangeRequest;

public interface UserService {
    Boolean changePassword(PasswordChangeRequest passwordChangeRequest);
}
