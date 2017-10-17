package io.getstream.client.apache.repo.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.exception.AuthenticationFailedException;
import io.getstream.client.exception.InternalServerException;
import io.getstream.client.exception.InvalidOrMissingInputException;
import io.getstream.client.exception.RateLimitExceededException;
import io.getstream.client.exception.ResourceNotFoundException;
import io.getstream.client.exception.StreamClientException;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StreamExceptionHandlerTest {

    private final StreamExceptionHandler exceptionHandler;

    public StreamExceptionHandlerTest() {
        exceptionHandler = new StreamExceptionHandler(mock(ObjectMapper.class));
    }

    @Test(expected = RateLimitExceededException.class)
    public void testError429() throws IOException, StreamClientException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 429, ""));
        when(response.getEntity()).thenReturn(new StringEntity(""));
        exceptionHandler.handleResponseCode(response);
    }

    @Test(expected = InvalidOrMissingInputException.class)
    public void testError400() throws IOException, StreamClientException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, ""));
        when(response.getEntity()).thenReturn(new StringEntity(""));
        exceptionHandler.handleResponseCode(response);
    }

    @Test(expected = AuthenticationFailedException.class)
    public void testError401() throws IOException, StreamClientException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 401, ""));
        when(response.getEntity()).thenReturn(new StringEntity(""));
        exceptionHandler.handleResponseCode(response);
    }

    @Test(expected = AuthenticationFailedException.class)
    public void testError403() throws IOException, StreamClientException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 403, ""));
        when(response.getEntity()).thenReturn(new StringEntity(""));
        exceptionHandler.handleResponseCode(response);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testError404() throws IOException, StreamClientException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 404, ""));
        when(response.getEntity()).thenReturn(new StringEntity(""));
        exceptionHandler.handleResponseCode(response);
    }

    @Test(expected = InternalServerException.class)
    public void testError500() throws IOException, StreamClientException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, ""));
        when(response.getEntity()).thenReturn(new StringEntity(""));
        exceptionHandler.handleResponseCode(response);
    }

    @Test(expected = StreamClientException.class)
    public void testError503() throws IOException, StreamClientException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 503, ""));
        when(response.getEntity()).thenReturn(new StringEntity(""));
        exceptionHandler.handleResponseCode(response);
    }

    @Test
    public void testOk() throws IOException, StreamClientException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, ""));
        when(response.getEntity()).thenReturn(new StringEntity(""));
        exceptionHandler.handleResponseCode(response);
    }
}