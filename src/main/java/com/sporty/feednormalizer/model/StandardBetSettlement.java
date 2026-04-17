package com.sporty.feednormalizer.model;

public record StandardBetSettlement(
    String eventId,
    Outcome outcome
) implements StandardMessage{}