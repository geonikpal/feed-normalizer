package com.sporty.feednormalizer.service.beta;

import com.sporty.feednormalizer.model.BetaFeedMessage;
import com.sporty.feednormalizer.model.Outcome;
import com.sporty.feednormalizer.model.StandardBetSettlement;
import com.sporty.feednormalizer.model.StandardMessage;
import com.sporty.feednormalizer.service.FeedProcessor;
import org.springframework.stereotype.Component;

@Component
public class BetaSettlementProcessor implements FeedProcessor<BetaFeedMessage> {

    @Override
    public StandardMessage process(BetaFeedMessage message) {
        return new StandardBetSettlement(
            message.getEventId(),
            Outcome.valueOf(message.getResult().toUpperCase())
        );
    }
}