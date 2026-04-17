package com.sporty.feednormalizer.service.alpha;

import com.sporty.feednormalizer.model.Outcome;
import org.springframework.stereotype.Component;

@Component
public class AlphaOutcomeMapper {

    public Outcome map(String outcome) {
        return switch (outcome) {
            case "1" -> Outcome.HOME;
            case "X" -> Outcome.DRAW;
            case "2" -> Outcome.AWAY;
            default  -> throw new IllegalArgumentException("Unknown Alpha outcome: " + outcome);
        };
    }
}