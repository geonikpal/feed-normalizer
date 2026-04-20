package com.sporty.feednormalizer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@Getter
public class AlphaFeedMessage {

    @JsonProperty("msg_type")
    private String msgType;

    @JsonProperty("event_id")
    private String eventId;

    private Map<String, Double> values;

    private String outcome;
}