package com.sporty.feednormalizer;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.sporty.feednormalizer.service.MessageQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FeedNormalizerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(MessageQueueService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    // ── ProviderAlpha happy path ───────────────────────────────────────────────

    @Test
    void shouldPublishStandardOddsChangeForAlphaOddsMessage() throws Exception {
        String payload = """
                {
                    "msg_type": "odds_update",
                    "event_id": "ev123",
                    "values": { "1": 2.0, "X": 3.1, "2": 3.8 }
                }
                """;

        mockMvc.perform(post("/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        assertLogContains("ev123", "2.0", "3.1", "3.8");
    }

    @Test
    void shouldPublishStandardBetSettlementForAlphaSettlementMessage() throws Exception {
        String payload = """
                {
                    "msg_type": "settlement",
                    "event_id": "ev123",
                    "outcome": "1"
                }
                """;

        mockMvc.perform(post("/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        assertLogContains("ev123", "HOME");
    }

    // ── ProviderBeta happy path ───────────────────────────────────────────────

    @Test
    void shouldPublishStandardOddsChangeForBetaOddsMessage() throws Exception {
        String payload = """
                {
                    "type": "ODDS",
                    "event_id": "ev456",
                    "odds": { "home": 1.95, "draw": 3.2, "away": 4.0 }
                }
                """;

        mockMvc.perform(post("/provider-beta/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        assertLogContains("ev456", "1.95", "3.2", "4.0");
    }

    @Test
    void shouldPublishStandardBetSettlementForBetaSettlementMessage() throws Exception {
        String payload = """
                {
                    "type": "SETTLEMENT",
                    "event_id": "ev456",
                    "result": "away"
                }
                """;

        mockMvc.perform(post("/provider-beta/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        assertLogContains("ev456", "AWAY");
    }

    // ── GlobalExceptionHandler - malformed JSON ───────────────────────────────

    @Test
    void shouldReturn400WhenAlphaPayloadIsNotValidJson() throws Exception {
        mockMvc.perform(post("/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("not-json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("MALFORMED_JSON")));
    }

    @Test
    void shouldReturn400WhenBetaPayloadIsNotValidJson() throws Exception {
        mockMvc.perform(post("/provider-beta/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("MALFORMED_JSON")));
    }

    // ── GlobalExceptionHandler - unknown message type ─────────────────────────

    @Test
    void shouldReturn400WhenAlphaMessageTypeIsUnknown() throws Exception {
        String payload = """
                {
                    "msg_type": "unknown_type",
                    "event_id": "ev123"
                }
                """;

        mockMvc.perform(post("/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("INVALID_PAYLOAD")));
    }

    @Test
    void shouldReturn400WhenBetaMessageTypeIsUnknown() throws Exception {
        String payload = """
                {
                    "type": "UNKNOWN",
                    "event_id": "ev456"
                }
                """;

        mockMvc.perform(post("/provider-beta/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("INVALID_PAYLOAD")));
    }

    // ── GlobalExceptionHandler - empty body ───────────────────────────────────

    @Test
    void shouldReturn400WhenBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void assertLogContains(String... expectedFragments) {
        List<String> logMessages = listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .toList();

        for (String fragment : expectedFragments) {
            assertTrue(
                    logMessages.stream().anyMatch(msg -> msg.contains(fragment)),
                    "Expected log to contain: " + fragment
            );
        }
    }
}