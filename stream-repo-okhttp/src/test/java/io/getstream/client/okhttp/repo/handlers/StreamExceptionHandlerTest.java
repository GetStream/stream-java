package io.getstream.client.okhttp.repo.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.RealResponseBody;
import io.getstream.client.exception.AuthenticationFailedException;
import io.getstream.client.exception.InternalServerException;
import io.getstream.client.exception.InvalidOrMissingInputException;
import io.getstream.client.exception.RateLimitExceededException;
import io.getstream.client.exception.ResourceNotFoundException;
import io.getstream.client.exception.StreamClientException;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;

public class StreamExceptionHandlerTest {

    public static final String APPLICATION_JSON = "application/json";
    public static final String FAKE_URL = "http://www.example.com";

    private final StreamExceptionHandler exceptionHandler;

    public StreamExceptionHandlerTest() {
        exceptionHandler = new StreamExceptionHandler(mock(ObjectMapper.class));
    }

    @Test(expected = RateLimitExceededException.class)
    public void testError429() throws IOException, StreamClientException {
        exceptionHandler.handleResponseCode(new Response.Builder()
                .code(429)
                .message("")
                .body(RealResponseBody.create(MediaType.parse(APPLICATION_JSON), ""))
                .request(new Request.Builder().url(FAKE_URL).build())
                .protocol(Protocol.HTTP_1_1)
                .build());
    }

    @Test(expected = InvalidOrMissingInputException.class)
    public void testError400() throws IOException, StreamClientException {
        exceptionHandler.handleResponseCode(new Response.Builder()
                .code(400)
                .message("")
                .body(RealResponseBody.create(MediaType.parse(APPLICATION_JSON), ""))
                .request(new Request.Builder().url(FAKE_URL).build())
                .protocol(Protocol.HTTP_1_1)
                .build());
    }

    @Test(expected = AuthenticationFailedException.class)
    public void testError401() throws IOException, StreamClientException {
        exceptionHandler.handleResponseCode(new Response.Builder()
                .code(401)
                .message("")
                .body(RealResponseBody.create(MediaType.parse(APPLICATION_JSON), ""))
                .request(new Request.Builder().url(FAKE_URL).build())
                .protocol(Protocol.HTTP_1_1)
                .build());
    }

    @Test(expected = AuthenticationFailedException.class)
    public void testError403() throws IOException, StreamClientException {
        exceptionHandler.handleResponseCode(new Response.Builder()
                .code(403)
                .message("")
                .body(RealResponseBody.create(MediaType.parse(APPLICATION_JSON), ""))
                .request(new Request.Builder().url(FAKE_URL).build())
                .protocol(Protocol.HTTP_1_1)
                .build());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testError404() throws IOException, StreamClientException {
        exceptionHandler.handleResponseCode(new Response.Builder()
                .code(404)
                .message("")
                .body(RealResponseBody.create(MediaType.parse(APPLICATION_JSON), ""))
                .request(new Request.Builder().url(FAKE_URL).build())
                .protocol(Protocol.HTTP_1_1)
                .build());
    }

    @Test(expected = InternalServerException.class)
    public void testError500() throws IOException, StreamClientException {
        exceptionHandler.handleResponseCode(new Response.Builder()
                .code(500)
                .message("")
                .body(RealResponseBody.create(MediaType.parse(APPLICATION_JSON), ""))
                .request(new Request.Builder().url(FAKE_URL).build())
                .protocol(Protocol.HTTP_1_1)
                .build());
    }

    @Test(expected = StreamClientException.class)
    public void testError503() throws IOException, StreamClientException {
        exceptionHandler.handleResponseCode(new Response.Builder()
                .code(503)
                .message("")
                .body(RealResponseBody.create(MediaType.parse(APPLICATION_JSON), ""))
                .request(new Request.Builder().url(FAKE_URL).build())
                .protocol(Protocol.HTTP_1_1)
                .build());
    }

    @Test
    public void testOk() throws IOException, StreamClientException {
        exceptionHandler.handleResponseCode(new Response.Builder()
                .code(201)
                .message("")
                .body(RealResponseBody.create(MediaType.parse(APPLICATION_JSON), ""))
                .request(new Request.Builder().url(FAKE_URL).build())
                .protocol(Protocol.HTTP_1_1)
                .build());
    }
}