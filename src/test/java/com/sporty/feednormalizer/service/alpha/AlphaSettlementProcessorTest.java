package com.sporty.feednormalizer.service.alpha;

import com.sporty.feednormalizer.model.AlphaFeedMessage;
import com.sporty.feednormalizer.model.Outcome;
import com.sporty.feednormalizer.model.StandardBetSettlement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlphaSettlementProcessorTest {

    private final AlphaOutcomeMapper mapper = new AlphaOutcomeMapper();
    private final AlphaSettlementProcessor processor = new AlphaSettlementProcessor(mapper);

    @Test
    void shouldMapAlphaSettlementToStandardBetSettlement() {
        AlphaFeedMessage message = new AlphaFeedMessage();
        message.setEventId("ev123");
        message.setOutcome("1");

        StandardBetSettlement result = (StandardBetSettlement) processor.process(message);

        assertEquals("ev123", result.eventId());
        assertEquals(Outcome.HOME, result.outcome());
    }

    @Test
    void shouldMapDrawOutcome() {
        AlphaFeedMessage message = new AlphaFeedMessage();
        message.setEventId("ev123");
        message.setOutcome("X");

        StandardBetSettlement result = (StandardBetSettlement) processor.process(message);

        assertEquals(Outcome.DRAW, result.outcome());
    }

    @Test
    void shouldMapAwayOutcome() {
        AlphaFeedMessage message = new AlphaFeedMessage();
        message.setEventId("ev123");
        message.setOutcome("2");

        StandardBetSettlement result = (StandardBetSettlement) processor.process(message);

        assertEquals(Outcome.AWAY, result.outcome());
    }
}