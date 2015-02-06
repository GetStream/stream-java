package io.getstream.client.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.exception.AuthenticationFailedException;
import io.getstream.client.exception.InternalServerException;
import io.getstream.client.exception.InvalidOrMissingInputException;
import io.getstream.client.exception.ResourceNotFoundException;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.beans.StreamErrorResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class StreamExceptionHandler {

    private final ObjectMapper objectMapper;

    public StreamExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handleResponseCode(final CloseableHttpResponse response) throws IOException, StreamClientException {
        switch (response.getStatusLine().getStatusCode()) {
            case 400:
                throw buildException(new InvalidOrMissingInputException(), response);
            case 401:
                throw buildException(new AuthenticationFailedException(), response);
			case 403:
				throw buildException(new AuthenticationFailedException(), response);
			case 404:
                throw buildException(new ResourceNotFoundException(), response);
            case 500:
                throw buildException(new InternalServerException(), response);
        }
    }

    private StreamClientException buildException(StreamClientException exception,
												 CloseableHttpResponse response) throws IOException {
		String responseMessage = EntityUtils.toString(response.getEntity());
        StreamErrorResponse error = objectMapper.readValue(responseMessage, StreamErrorResponse.class);
        exception.setCode(error.getCode());
        exception.setHttpStatusCode(error.getStatusCode());
        exception.setDetail(error.getDetail());
        exception.setExceptionField(error.getException());
        return exception;
    }
}
