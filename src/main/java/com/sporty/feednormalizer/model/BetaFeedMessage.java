package com.sporty.feednormalizer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class BetaFeedMessage {

    private String type;

    @JsonProperty("event_id")
    private String eventId;

    private Map<String, Double> odds;

    private String result;
}