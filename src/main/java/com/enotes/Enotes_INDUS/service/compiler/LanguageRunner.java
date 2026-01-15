package com.enotes.Enotes_INDUS.service.compiler;

import com.enotes.Enotes_INDUS.dto.CodeRequest;
import com.enotes.Enotes_INDUS.dto.CodeResponse;

public interface LanguageRunner {
    CodeResponse run(CodeRequest codeRequest) throws Exception;
}
