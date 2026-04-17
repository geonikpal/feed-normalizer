package com.sporty.feednormalizer.service;

import com.sporty.feednormalizer.model.StandardMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageQueueService {

    public void publish(StandardMessage message) {
        log.info("Publishing message to queue: {}", message);
    }
}