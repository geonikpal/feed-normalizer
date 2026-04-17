package com.sporty.feednormalizer.service.alpha;

import com.sporty.feednormalizer.model.AlphaFeedMessage;
import com.sporty.feednormalizer.model.StandardMessage;
import com.sporty.feednormalizer.service.FeedProcessor;
import com.sporty.feednormalizer.service.MessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProviderAlphaService {

    private final Map<String, FeedProcessor<AlphaFeedMessage>> processors;
    private final MessageQueueService messageQueueService;

    public ProviderAlphaService(
            AlphaOddsProcessor alphaOddsProcessor,
            AlphaSettlementProcessor alphaSettlementProcessor,
            MessageQueueService messageQueueService) {

        this.processors = Map.of(
            "odds_update", alphaOddsProcessor,
            "settlement",  alphaSettlementProcessor
        );
        this.messageQueueService = messageQueueService;
    }

    public void process(AlphaFeedMessage message) {
        FeedProcessor<AlphaFeedMessage> processor = processors.get(message.getMsgType());
        if (processor == null) {
            throw new IllegalArgumentException("Unknown Alpha message type: " + message.getMsgType());
        }
        StandardMessage standardMessage = processor.process(message);
        messageQueueService.publish(standardMessage);
    }
}