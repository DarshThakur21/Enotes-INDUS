package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.dto.CodeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.CoderResult;

import static com.enotes.Enotes_INDUS.utils.Constants.ROLE_USER;

@RequestMapping("/api/v1/compiler")
@CrossOrigin
public interface CodeExecutionEndpoint {

    @PostMapping
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> execute(@RequestBody CodeRequest codeRequest) throws Exception;

}
