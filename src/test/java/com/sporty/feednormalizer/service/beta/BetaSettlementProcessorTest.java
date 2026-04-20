package com.sporty.feednormalizer.service.beta;

import com.sporty.feednormalizer.model.BetaFeedMessage;
import com.sporty.feednormalizer.model.Outcome;
import com.sporty.feednormalizer.model.StandardBetSettlement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BetaSettlementProcessorTest {

    private final BetaSettlementProcessor processor = new BetaSettlementProcessor();

    @Test
    void shouldMapHomeResult() {
        BetaFeedMessage message = new BetaFeedMessage();
        message.setEventId("ev456");
        message.setResult("home");

        StandardBetSettlement result = (StandardBetSettlement) processor.process(message);

        assertEquals("ev456", result.eventId());
        assertEquals(Outcome.HOME, result.outcome());
    }

    @Test
    void shouldMapDrawResult() {
        BetaFeedMessage message = new BetaFeedMessage();
        message.setEventId("ev456");
        message.setResult("draw");

        StandardBetSettlement result = (StandardBetSettlement) processor.process(message);

        assertEquals(Outcome.DRAW, result.outcome());
    }

    @Test
    void shouldMapAwayResult() {
        BetaFeedMessage message = new BetaFeedMessage();
        message.setEventId("ev456");
        message.setResult("away");

        StandardBetSettlement result = (StandardBetSettlement) processor.process(message);

        assertEquals(Outcome.AWAY, result.outcome());
    }
}