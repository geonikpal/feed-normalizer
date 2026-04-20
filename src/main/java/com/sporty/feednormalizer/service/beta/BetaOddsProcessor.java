package com.sporty.feednormalizer.service.beta;

import com.sporty.feednormalizer.model.BetaFeedMessage;
import com.sporty.feednormalizer.model.StandardMessage;
import com.sporty.feednormalizer.model.StandardOddsChange;
import com.sporty.feednormalizer.service.FeedProcessor;
import org.springframework.stereotype.Component;

@Component
public class BetaOddsProcessor implements FeedProcessor<BetaFeedMessage> {

    @Override
    public StandardMessage process(BetaFeedMessage message) {
        return new StandardOddsChange(
            message.getEventId(),
            message.getOdds().get("home"),
            message.getOdds().get("draw"),
            message.getOdds().get("away")
        );
    }
}