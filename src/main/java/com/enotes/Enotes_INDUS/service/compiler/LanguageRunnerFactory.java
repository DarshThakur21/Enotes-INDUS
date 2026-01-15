package com.enotes.Enotes_INDUS.service.compiler;

import com.enotes.Enotes_INDUS.service.compiler.languagesrunner.CppRunner;
import com.enotes.Enotes_INDUS.service.compiler.languagesrunner.JavaRunner;
import com.enotes.Enotes_INDUS.service.compiler.languagesrunner.JsRunner;
import com.enotes.Enotes_INDUS.service.compiler.languagesrunner.PythonRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LanguageRunnerFactory {

    private final Map<String,LanguageRunner> runnerMap;

    public LanguageRunnerFactory(JavaRunner javaRunner, PythonRunner pythonRunner, JsRunner jsRunner, CppRunner cppRunner){
            this.runnerMap=Map.of(
                    "java",javaRunner,
                    "python",pythonRunner,
                    "javascript",jsRunner,
                    "cpp",cppRunner);

    }

    public LanguageRunner getRunner(String language) {
        LanguageRunner runner = runnerMap.get(language);
        if (runner == null) {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }
        return runner;
    }






}
