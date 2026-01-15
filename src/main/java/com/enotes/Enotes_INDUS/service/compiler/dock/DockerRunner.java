package com.enotes.Enotes_INDUS.service.compiler.dock;

import com.enotes.Enotes_INDUS.dto.CodeResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.io.file.PathUtils.deleteDirectory;

@Service
public class DockerRunner {
    private static final int TIMEOUT_SECONDS=9;

    public CodeResponse runContainer(String image, Map<String, String> files, String input) throws Exception
    {
        Path tempDir = Files.createTempDirectory("code-runner-");

        try {
            for(Map.Entry<String,String> entry :files.entrySet()){
                Files.writeString(
                        tempDir.resolve(entry.getKey()),
                        entry.getValue(),
                        StandardOpenOption.CREATE
                );
            }

            long startTime = System.currentTimeMillis();

            ProcessBuilder processBuilder=new ProcessBuilder(
                "docker", "run", "--rm",
                    "--network=none",
                    "--memory=256m",
                    "--cpus=1",
                    "--pids-limit=64",
                    "--read-only",
                    "--security-opt=no-new-privileges",
                    "-e", "INPUT=" + input,

                    "-v", tempDir.toAbsolutePath() + ":/app:ro",
                    image

            );

            Process process= processBuilder.start();
            boolean finish=process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if(!finish){
                process.destroyForcibly();
                return  new CodeResponse(
                  ""  ,
                  "Execution Timed out",
            System.currentTimeMillis() - startTime
                );
            }

            String stdOut=new String(process.getInputStream().readAllBytes());
            String stdderr=new String(process.getErrorStream().readAllBytes());
            return new CodeResponse(
                    stdOut.trim(),
                    stdderr.trim(),
                    System.currentTimeMillis() - startTime
            );

        } catch (RuntimeException e) {
            throw new RuntimeException(e);

        }finally {
            deleteDirectory(tempDir);
        }

    }
    private void deleteDirectory(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException ignored) {}
                });
    }

}
