package com.sporty.feednormalizer.service.alpha;

import com.sporty.feednormalizer.model.AlphaFeedMessage;
import com.sporty.feednormalizer.model.StandardBetSettlement;
import com.sporty.feednormalizer.model.StandardMessage;
import com.sporty.feednormalizer.service.FeedProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlphaSettlementProcessor implements FeedProcessor<AlphaFeedMessage> {

    private final AlphaOutcomeMapper outcomeMapper;

    @Override
    public StandardMessage process(AlphaFeedMessage message) {
        return new StandardBetSettlement(
            message.getEventId(),
            outcomeMapper.map(message.getOutcome())
        );
    }
}