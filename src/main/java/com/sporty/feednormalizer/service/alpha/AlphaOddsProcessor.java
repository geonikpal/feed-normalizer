package com.sporty.feednormalizer.service.alpha;

import com.sporty.feednormalizer.model.AlphaFeedMessage;
import com.sporty.feednormalizer.model.StandardMessage;
import com.sporty.feednormalizer.model.StandardOddsChange;
import com.sporty.feednormalizer.service.FeedProcessor;
import org.springframework.stereotype.Component;

@Component
public class AlphaOddsProcessor implements FeedProcessor<AlphaFeedMessage> {

    @Override
    public StandardMessage process(AlphaFeedMessage message) {
        return new StandardOddsChange(
                message.getEventId(),
                message.getValues().get("1"),
                message.getValues().get("X"),
                message.getValues().get("2")
        );
    }
}
