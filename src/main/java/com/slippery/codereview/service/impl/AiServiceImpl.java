package com.slippery.codereview.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.slippery.codereview.dto.AiDto;
import com.slippery.codereview.dto.AiRequest;
import com.slippery.codereview.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
@Slf4j
public class AiServiceImpl implements AiService {
    private final WebClient webClient;

    public AiServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }
    @Value("${gemini_api_key}")
    private String geminiApiKey;

    @Override
    public AiDto createNewAiRequest(AiRequest request)  {
        AiDto response =new AiDto();
        ObjectMapper objectMapper = new ObjectMapper();
        String generatedPrompt =buildPrompt(request.message);
        Map<String,Object> requestBody = Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",generatedPrompt)
                        })
                }
        );
        try{
            String geminiResponse =webClient.post()
                    .uri(geminiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            response.setStatusCode(200);
            response.setResponse(geminiResponse);
            JsonNode rootNode = objectMapper.readTree(geminiResponse);
            JsonNode candidatesNode = rootNode.path("candidates");

            // Iterate through candidates
            for (JsonNode candidateNode : candidatesNode) {
                JsonNode contentNode = candidateNode.path("content");
                JsonNode partsNode = contentNode.path("parts");

                // Iterate through parts
                for (JsonNode partNode : partsNode) {
                    String text = partNode.path("text").asText();
                    response.setResponse(text);
                    response.setStatusCode(200);
                }
            }
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return response;
    }
//    TODO : make the prompts to be 3 different objects
    private String buildPrompt(String request){
        String prompt = """
                "You are an expert code reviewer focusing on identifying bugs and logical errors. Be thorough and specific.",
                prompt: `Analyze this code for bugs and logical errors:
                or logical errors you find. For each issue, include:
                 1. A clear description of the bug
                 2. The severity (low, medium, high)
                 3. A specific suggestion for how to fix it`,
                ),
                
                "You are a security expert focusing on identifying security vulnerabilities in code. Be thorough and specific.",
                prompt: `Analyze this code for security vulnerabilities:
                Provide a detailed analysis of any security issues you find. For each issue, include:
                 1. A clear description of the vulnerability
                 2. The severity (low, medium, high)
                 3. A specific suggestion for how to fix it`,
                
                "You are a performance optimization expert focusing on identifying performance issues in code. Be thorough and specific.",
                prompt: `Analyze this code for performance issues:
                
                 Provide a detailed analysis of any performance issues you find. For each issue, include:
                 1. A clear description of the performance issue
                 2. The impact level (low, medium, high)
                 3. A specific suggestion for how to optimize it`,
                 
                 "You are a code quality expert focusing on identifying best practices and code style issues. Be thorough and specific.",
                 prompt: `Analyze this code for adherence to best practices and code style:
                 Provide a detailed analysis of any best practice or code style issues you find. For each issue, include:
                 1. A clear description of the issue
                 2. A specific suggestion for how to improve it`,
                 
                 BELOW IS THE CODE YOU ARE TO ANALYZE : please make sure you return valid markdown without escape characters in the following format
                 # performance issues: 
                 * issue
                 * issue
                 * issue
                 
                 # Best practices
                 *
                 *
                 *
                 # security issues
                 * issue - explain 
                 ### suggest ways to fix
                 if there is a code block in the response,make sure it is formated in this way ```language {the code}```
            
                """
                ;
        return prompt +
                request;

    }
}
