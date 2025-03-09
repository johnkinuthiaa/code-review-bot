package com.slippery.codereview.service;

import com.slippery.codereview.dto.AiDto;
import com.slippery.codereview.dto.AiRequest;

public interface AiService {
    AiDto createNewAiRequest(AiRequest request);
}
