package io.getstream.client.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

public class StreamExceptionHandler {

    private final ObjectMapper objectMapper;

    public StreamExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private void handleResponseCode(final CloseableHttpResponse response) throws StreamClientException {
        switch (response.getStatusLine().getStatusCode()) {
            case 400:
                throw buildException(new InvalidOrMissingInputException(), response.getEntity());
            case 401:
                throw buildException(new AuthenticationFailedException(), response.getEntity());
            case 404:
                throw buildException(new ResourceNotFoundException(), response.getEntity());
            case 500:
                throw buildException(new InternalServerException(), response.getEntity());
        }
    }

    private StreamClientException buildException(StreamClientException exception, HttpEntity entity) throws IOException {
        objectMapper.readValue(entity.getContent(), Exception.class);
        exception.setCode();
        exception.setHttpStatusCode();
        exception.getDetail();
        exception.setExceptionField();
        return exception;
    }

}
