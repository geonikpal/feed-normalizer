package com.sporty.feednormalizer.controller;

import com.sporty.feednormalizer.model.AlphaFeedMessage;
import com.sporty.feednormalizer.service.alpha.ProviderAlphaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProviderAlphaController {

    private final ProviderAlphaService providerAlphaService;

    @PostMapping("/provider-alpha/feed")
    public ResponseEntity<Void> receive(@RequestBody AlphaFeedMessage message) {
        log.debug("Received payload from ProviderAlpha: {}", message);
        providerAlphaService.process(message);
        return ResponseEntity.ok().build();
    }
}