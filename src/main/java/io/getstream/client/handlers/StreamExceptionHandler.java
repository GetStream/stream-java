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

/**
 * It provides a mechanism to translate the http status code (different from 200)
 * in a proper java Exception.
 */
public class StreamExceptionHandler {

    private final ObjectMapper objectMapper;

	/**
	 * Create the object using the ObjectMapper to deserialize the json response coming from
	 * remote side.
	 * @param objectMapper
	 */
    public StreamExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

	/**
	 * Translate http status code to Java Exception.
	 * @param response Response
	 * @throws IOException
	 * @throws StreamClientException
	 */
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
