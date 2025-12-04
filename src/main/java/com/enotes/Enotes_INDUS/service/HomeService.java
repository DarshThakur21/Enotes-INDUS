package com.enotes.Enotes_INDUS.service;

import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.exceptions.ResourceNotFound;

public interface HomeService {

    Boolean verifyUser(Integer uid,String code) throws RegisterException, ResourceNotFound;
}
