package com.sporty.feednormalizer.service.beta;

import com.sporty.feednormalizer.model.BetaFeedMessage;
import com.sporty.feednormalizer.model.StandardOddsChange;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BetaOddsProcessorTest {

    private final BetaOddsProcessor processor = new BetaOddsProcessor();

    @Test
    void shouldMapBetaOddsToStandardOddsChange() {
        BetaFeedMessage message = new BetaFeedMessage();
        message.setEventId("ev456");
        message.setOdds(Map.of("home", 1.95, "draw", 3.2, "away", 4.0));

        StandardOddsChange result = (StandardOddsChange) processor.process(message);

        assertEquals("ev456", result.eventId());
        assertEquals(1.95, result.homeOdds());
        assertEquals(3.2, result.drawOdds());
        assertEquals(4.0, result.awayOdds());
    }
}