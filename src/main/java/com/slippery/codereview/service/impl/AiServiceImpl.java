package com.slippery.codereview.service.impl;

import com.slippery.codereview.dto.AiDto;
import com.slippery.codereview.dto.AiRequest;
import com.slippery.codereview.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Slf4j
public class AiServiceImpl implements AiService {
    private final WebClient webClient;

    public AiServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Override
    public AiDto createNewAiRequest(AiRequest request)  {
        AiDto response =new AiDto();
        String generatedPrompt =buildPrompt(request.message);
        Map<String,Object> requestBody = Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",generatedPrompt)
                        })
                }
        );
//        sk-c89b1d03f3b5437c84a25f3525936a44
        try{
            String geminiResponse =webClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyD0IHwnimrFZkuLfbbXcO26dh3dbvkwmCU")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            response.setStatusCode(200);
            response.setResponse(geminiResponse);
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
                 
                 BELOW IS THE CODE YOU ARE TO ANALYZE : please make sure you return valid paragraphs without escape characters
                """
                ;
        return prompt +
                request;

    }
}
