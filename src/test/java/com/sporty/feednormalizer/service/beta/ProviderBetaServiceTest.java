package com.sporty.feednormalizer.service.beta;

import com.sporty.feednormalizer.model.BetaFeedMessage;
import com.sporty.feednormalizer.model.StandardBetSettlement;
import com.sporty.feednormalizer.model.Outcome;
import com.sporty.feednormalizer.service.MessageQueueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderBetaServiceTest {

    @Mock
    private BetaOddsProcessor betaOddsProcessor;

    @Mock
    private BetaSettlementProcessor betaSettlementProcessor;

    @Mock
    private MessageQueueService messageQueueService;

    @InjectMocks
    private ProviderBetaService providerBetaService;

    @Test
    void shouldDelegateToOddsProcessorAndPublish() {
        BetaFeedMessage message = new BetaFeedMessage();
        message.setType("ODDS");
        message.setEventId("ev456");

        StandardBetSettlement standardMessage = new StandardBetSettlement("ev456", Outcome.HOME);
        when(betaOddsProcessor.process(message)).thenReturn(standardMessage);

        providerBetaService.process(message);

        verify(betaOddsProcessor).process(message);
        verify(messageQueueService).publish(standardMessage);
    }

    @Test
    void shouldDelegateToSettlementProcessorAndPublish() {
        BetaFeedMessage message = new BetaFeedMessage();
        message.setType("SETTLEMENT");
        message.setEventId("ev456");

        StandardBetSettlement standardMessage = new StandardBetSettlement("ev456", Outcome.AWAY);
        when(betaSettlementProcessor.process(message)).thenReturn(standardMessage);

        providerBetaService.process(message);

        verify(betaSettlementProcessor).process(message);
        verify(messageQueueService).publish(standardMessage);
    }

    @Test
    void shouldThrowExceptionForUnknownMessageType() {
        BetaFeedMessage message = new BetaFeedMessage();
        message.setType("unknown");

        assertThrows(IllegalArgumentException.class, () -> providerBetaService.process(message));
    }
}