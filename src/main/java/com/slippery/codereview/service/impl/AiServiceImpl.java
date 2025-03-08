package com.slippery.codereview.service.impl;

import com.slippery.codereview.dto.AiDto;
import com.slippery.codereview.service.AiService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AiServiceImpl implements AiService {
    private final WebClient webClient;

    public AiServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Override
    public AiDto createNewAiRequest(String request)  {
        AiDto response =new AiDto();
        String generatedPrompt =buildPrompt(request);
        Map<String,Object> requestBody = Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",generatedPrompt)
                        })
                }
        );
        try{
            String geminiResponse =webClient.post()
                    .uri("")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            response.setStatusCode(200);
            response.setResponse(geminiResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
    private String buildPrompt(String request){
        String prompt = """
                You are a code review bot that receives code from an input and analyzes the complexity of the code,
                the bugs in the code and how to fix them
                the total number of the bugs,the security issues with the code, the performance of the code
                and what it does checking whether it was written by a senior developer,junior developer or an ai system
                by analysing the contents of the code and how it can be improved to look like a senior devs code.
                and it returns jsx with tailwind code in the
                
                 please make sure it returns valid jsx container with some tailwind classes that will be embeded onto an existing website
                 to show the response to the user
                """
                ;
        return prompt +
                request;

    }
}
