package com.nexchat.NexChat.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

@Service
public class GeminiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${gemini.api-key}")
    private String apiKey;
    @Value("${gemini.model}")
    private String model;
    @Value("${gemini.endpoint}")
    private String endpoint;

    public String summarize(String formattedChat) {
        String prompt = "You are a helpful assistant. Summarize the following group chat conversation.\n"
                + "Be concise. Highlight: key topics discussed, decisions made, and any action items.\n"
                + "Do NOT include any greetings or extra explanation — just the summary.\n\n"
                + "Chat:  "
                + formattedChat;

        ObjectNode requestBody = objectMapper.createObjectNode();
        ArrayNode contents = requestBody.putArray("contents");
        tools.jackson.databind.node.ObjectNode content = contents.addObject();
        ArrayNode parts = content.putArray("parts");
        parts.addObject().put("text", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity;
        try {
            entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize request body: " + e.getMessage());
        }

        String url = endpoint + "/" + model + ":generateContent?key=" + apiKey;

        String rawResponse = restTemplate.postForObject(url, entity, String.class);

        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            return root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage());
        }
    }
}