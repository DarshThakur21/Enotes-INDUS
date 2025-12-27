package com.enotes.Enotes_INDUS.service.impl.feature;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GeminiClient {

    @Value("${gemini.api.key}")
    private String key;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate=new RestTemplate();


    @PostConstruct
    public void checkKey() {
        log.info("Gemini key loaded: {}", key != null);
    }


    public String generate(String prompt) {

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String genurl = apiUrl + "?key=" + key;
        log.info("Gemini URL being called: {}", genurl);


        HttpEntity<Map<String, Object>> request =new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(genurl, request, Map.class);

        return extractText(response.getBody());
    }

    private String extractText(Map body) {
        List candidates = (List) body.get("candidates");
        Map content = (Map) ((Map) candidates.get(0)).get("content");
        List parts = (List) content.get("parts");
        return (String) ((Map) parts.get(0)).get("text");
    }




}
