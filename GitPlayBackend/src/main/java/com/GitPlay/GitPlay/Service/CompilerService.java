package com.GitPlay.GitPlay.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;


@Service
public class CompilerService {
    @Value("${flask.api.url}")
    private String CompileCodeServiceUrl;

    private final RestTemplate restTemplate;

    public CompilerService(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    public String executeCode(String language, String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:5000/execute";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("language", language);
        payload.put("code", code);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, request, Map.class);
            return response.getBody().get("output").toString();
        } catch (HttpClientErrorException e) {
            throw new Exception("Error from Flask service: " + e.getResponseBodyAsString());
        }
    }

}
