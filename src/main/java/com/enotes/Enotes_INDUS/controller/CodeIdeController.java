package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.dto.CodeRequest;
import com.enotes.Enotes_INDUS.dto.CodeResponse;
import com.enotes.Enotes_INDUS.endpoints.CodeExecutionEndpoint;
import com.enotes.Enotes_INDUS.service.compiler.LanguageRunner;
import com.enotes.Enotes_INDUS.service.compiler.LanguageRunnerFactory;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CodeIdeController implements CodeExecutionEndpoint {

    @Autowired
    private LanguageRunnerFactory languageRunnerFactory;

    @Override

    public ResponseEntity<?> execute(CodeRequest codeRequest) throws Exception {
        try{
            LanguageRunner runner= languageRunnerFactory.getRunner(codeRequest.getLanguage());
            CodeResponse codeResponse=runner.run(codeRequest);

            if(ObjectUtils.isEmpty(codeResponse)){
                return CommonUtil.createErrorResponseMessage("Not accessible", HttpStatus.BAD_REQUEST);
            }

                return CommonUtil.createBuildResponse(codeResponse, HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
