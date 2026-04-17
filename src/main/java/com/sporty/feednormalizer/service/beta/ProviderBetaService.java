package com.sporty.feednormalizer.service.beta;

import com.sporty.feednormalizer.model.BetaFeedMessage;
import com.sporty.feednormalizer.model.StandardMessage;
import com.sporty.feednormalizer.service.FeedProcessor;
import com.sporty.feednormalizer.service.MessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProviderBetaService {

    private final Map<String, FeedProcessor<BetaFeedMessage>> processors;
    private final MessageQueueService messageQueueService;

    public ProviderBetaService(
            BetaOddsProcessor betaOddsProcessor,
            BetaSettlementProcessor betaSettlementProcessor,
            MessageQueueService messageQueueService) {

        this.processors = Map.of(
            "ODDS",       betaOddsProcessor,
            "SETTLEMENT", betaSettlementProcessor
        );
        this.messageQueueService = messageQueueService;
    }

    public void process(BetaFeedMessage message) {
        FeedProcessor<BetaFeedMessage> processor = processors.get(message.getType());
        if (processor == null) {
            throw new IllegalArgumentException("Unknown Beta message type: " + message.getType());
        }
        StandardMessage standardMessage = processor.process(message);
        messageQueueService.publish(standardMessage);
    }
}