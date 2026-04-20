package com.sporty.feednormalizer.exception;

import com.sporty.feednormalizer.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    // ── IllegalArgumentException ──────────────────────────────────────────────

    @Test
    void shouldReturn400WhenIllegalArgumentException() {
        ResponseEntity<ErrorResponse> response =
                handler.handleUnknownMessageType(new IllegalArgumentException("Unknown msg_type: foo"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo("INVALID_PAYLOAD");
        assertThat(response.getBody().message()).isEqualTo("Unknown msg_type: foo");
    }

    @Test
    void shouldIncludeOriginalMessageInBody() {
        String errorMessage = "Missing required field: msg_type";

        ResponseEntity<ErrorResponse> response =
                handler.handleUnknownMessageType(new IllegalArgumentException(errorMessage));

        assertThat(response.getBody().message()).isEqualTo(errorMessage);
    }

    // ── HttpMessageNotReadableException ───────────────────────────────────────

    @Test
    void shouldReturn400WhenMalformedJson() {
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);

        ResponseEntity<ErrorResponse> response = handler.handleMalformedJson(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo("MALFORMED_JSON");
        assertThat(response.getBody().message()).isEqualTo("Request body is not valid JSON");
    }

    // ── Generic Exception ─────────────────────────────────────────────────────

    @Test
    void shouldReturn500WhenGenericException() {
        ResponseEntity<ErrorResponse> response =
                handler.handleGeneric(new RuntimeException("Something exploded"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo("INTERNAL_ERROR");
        assertThat(response.getBody().message()).isEqualTo("An unexpected error occurred");
    }

    @Test
    void shouldReturn500WithGenericMessageRegardlessOfExceptionMessage() {
        ResponseEntity<ErrorResponse> response =
                handler.handleGeneric(new NullPointerException("sensitive internal detail"));

        // Must never leak internal details to the client
        assertThat(response.getBody().message()).isEqualTo("An unexpected error occurred");
        assertThat(response.getBody().message()).doesNotContain("sensitive internal detail");
    }
}