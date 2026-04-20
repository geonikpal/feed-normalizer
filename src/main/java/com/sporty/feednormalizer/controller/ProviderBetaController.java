package com.sporty.feednormalizer.controller;

import com.sporty.feednormalizer.model.BetaFeedMessage;
import com.sporty.feednormalizer.service.beta.ProviderBetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProviderBetaController {

    private final ProviderBetaService providerBetaService;

    @PostMapping("/provider-beta/feed")
    public ResponseEntity<Void> receive(@RequestBody BetaFeedMessage message) {
        log.debug("Received payload from ProviderBeta: {}", message);
        providerBetaService.process(message);
        return ResponseEntity.ok().build();
    }
}