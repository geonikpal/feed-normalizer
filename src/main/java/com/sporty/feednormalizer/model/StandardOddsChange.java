package com.sporty.feednormalizer.model;

public record StandardOddsChange(
        String eventId,
        double homeOdds,
        double drawOdds,
        double awayOdds
) implements StandardMessage {}
