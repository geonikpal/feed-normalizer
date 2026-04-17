package com.sporty.feednormalizer.service.alpha;

import com.sporty.feednormalizer.model.AlphaFeedMessage;
import com.sporty.feednormalizer.model.StandardOddsChange;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AlphaOddsProcessorTest {

    private final AlphaOddsProcessor processor = new AlphaOddsProcessor();

    @Test
    void shouldMapAlphaOddsToStandardOddsChange() {
        AlphaFeedMessage message = new AlphaFeedMessage();
        message.setEventId("ev123");
        message.setValues(Map.of("1", 2.0, "X", 3.1, "2", 3.8));

        StandardOddsChange result = (StandardOddsChange) processor.process(message);

        assertEquals("ev123", result.eventId());
        assertEquals(2.0, result.homeOdds());
        assertEquals(3.1, result.drawOdds());
        assertEquals(3.8, result.awayOdds());
    }
}