package com.sporty.feednormalizer.service.alpha;

import com.sporty.feednormalizer.model.Outcome;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlphaOutcomeMapperTest {

    private final AlphaOutcomeMapper mapper = new AlphaOutcomeMapper();

    @Test
    void shouldMapOneToHome() {
        assertEquals(Outcome.HOME, mapper.map("1"));
    }

    @Test
    void shouldMapXToDraw() {
        assertEquals(Outcome.DRAW, mapper.map("X"));
    }

    @Test
    void shouldMapTwoToAway() {
        assertEquals(Outcome.AWAY, mapper.map("2"));
    }

    @Test
    void shouldThrowExceptionForUnknownOutcome() {
        assertThrows(IllegalArgumentException.class, () -> mapper.map("unknown"));
    }
}