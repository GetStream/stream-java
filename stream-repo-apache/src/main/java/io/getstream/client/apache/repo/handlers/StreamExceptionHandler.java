package io.getstream.client.apache.repo.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.exception.AuthenticationFailedException;
import io.getstream.client.exception.InternalServerException;
import io.getstream.client.exception.InvalidOrMissingInputException;
import io.getstream.client.exception.RateLimitExceededException;
import io.getstream.client.exception.ResourceNotFoundException;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.beans.StreamErrorResponse;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

/**
 * It provides a mechanism to translate the http status code (different from 200)
 * in a proper java Exception.
 */
public class StreamExceptionHandler {

    private final ObjectMapper objectMapper;

    /**
     * Create the object using the ObjectMapper to deserialize the json response coming from
     * remote side.
     *
     * @param objectMapper Jackson's objectMapper
     */
    public StreamExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Translate http status code to Java Exception.
     *
     * @param response HTTP Response
     * @throws IOException in case of network/socket exceptions.
     * @throws StreamClientException in case of functional or server-side exception
     */
    public void handleResponseCode(final CloseableHttpResponse response) throws IOException, StreamClientException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode > 299) {
            parseException(response);
        }
    }

    private void parseException(final CloseableHttpResponse response) throws IOException, StreamClientException {
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 400:
                throw buildException(new InvalidOrMissingInputException(), response);
            case 401:
                throw buildException(new AuthenticationFailedException(), response);
            case 403:
                throw buildException(new AuthenticationFailedException(), response);
            case 404:
                throw buildException(new ResourceNotFoundException(), response);
            case 429:
                throw buildException(new RateLimitExceededException(), response);
            case 500:
                throw buildException(new InternalServerException(), response);
            default:
                StreamClientException e = new InternalServerException();
                e.setCode(statusCode);
                e.setHttpStatusCode(statusCode);
                throw e;
        }
    }

    private StreamClientException buildException(StreamClientException exception,
                                                 CloseableHttpResponse response) throws IOException {
        StreamErrorResponse error = objectMapper.readValue(response.getEntity().getContent(), StreamErrorResponse.class);
        if (null != error) {
            exception.setCode(error.getCode());
            exception.setHttpStatusCode(response.getStatusLine().getStatusCode());
            exception.setDetail(error.getDetail());
            exception.setExceptionField(error.getException());
        }
        return exception;
    }
}
