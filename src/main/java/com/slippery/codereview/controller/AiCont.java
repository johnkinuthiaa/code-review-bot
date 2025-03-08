package com.slippery.codereview.controller;
import com.slippery.codereview.dto.AiDto;
import com.slippery.codereview.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")

public class AiCont {
    private final AiService aiService;

    public AiCont(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/analyse")
    public ResponseEntity<AiDto> createNewAiRequest(@RequestParam String request){
        return ResponseEntity.ok(aiService.createNewAiRequest(request));
    }
}
