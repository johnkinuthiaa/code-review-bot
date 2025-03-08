package com.slippery.codereview.service;

import com.slippery.codereview.dto.AiDto;

public interface AiService {
    AiDto createNewAiRequest(String request);
}
