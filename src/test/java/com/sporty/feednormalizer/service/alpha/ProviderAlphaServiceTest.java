package com.sporty.feednormalizer.service.alpha;

import com.sporty.feednormalizer.model.AlphaFeedMessage;
import com.sporty.feednormalizer.model.Outcome;
import com.sporty.feednormalizer.model.StandardBetSettlement;
import com.sporty.feednormalizer.model.StandardOddsChange;
import com.sporty.feednormalizer.service.MessageQueueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderAlphaServiceTest {

    @Mock
    private AlphaOddsProcessor alphaOddsProcessor;

    @Mock
    private AlphaSettlementProcessor alphaSettlementProcessor;

    @Mock
    private MessageQueueService messageQueueService;

    @InjectMocks
    private ProviderAlphaService providerAlphaService;

    @Test
    void shouldDelegateToOddsProcessorAndPublish() {
        AlphaFeedMessage message = new AlphaFeedMessage();
        message.setMsgType("odds_update");
        message.setEventId("ev123");
        message.setValues(Map.of("1", 2.0, "X", 3.1, "2", 3.8));

        StandardOddsChange standardMessage = new StandardOddsChange("ev123", 2.0, 3.1, 3.8);
        when(alphaOddsProcessor.process(message)).thenReturn(standardMessage);

        providerAlphaService.process(message);

        verify(alphaOddsProcessor).process(message);
        verify(messageQueueService).publish(standardMessage);
    }

    @Test
    void shouldDelegateToSettlementProcessorAndPublish() {
        AlphaFeedMessage message = new AlphaFeedMessage();
        message.setMsgType("settlement");
        message.setEventId("ev123");
        message.setOutcome("1");

        StandardBetSettlement standardMessage = new StandardBetSettlement("ev123", Outcome.HOME);
        when(alphaSettlementProcessor.process(message)).thenReturn(standardMessage);

        providerAlphaService.process(message);

        verify(alphaSettlementProcessor).process(message);
        verify(messageQueueService).publish(standardMessage);
    }

    @Test
    void shouldThrowExceptionForUnknownMessageType() {
        AlphaFeedMessage message = new AlphaFeedMessage();
        message.setMsgType("unknown");

        assertThrows(IllegalArgumentException.class, () -> providerAlphaService.process(message));
    }
}