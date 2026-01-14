package com.enotes.Enotes_INDUS.service.compiler.languagesrunner;

import com.enotes.Enotes_INDUS.dto.CodeRequest;
import com.enotes.Enotes_INDUS.dto.CodeResponse;
import com.enotes.Enotes_INDUS.service.compiler.LanguageRunner;
import com.enotes.Enotes_INDUS.service.compiler.dock.DockerRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PythonRunner implements LanguageRunner {

    @Autowired
    private DockerRunner dockerRunner;


    @Override
    public CodeResponse run(CodeRequest codeRequest) throws Exception {
        return dockerRunner.runContainer(
                "python-runner",
                Map.of("main.py",codeRequest.getCode()),
                codeRequest.getInput()
        );
    }
}
