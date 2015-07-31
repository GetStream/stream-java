/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
package io.getstream.client.repo.handlers;

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
     *
     * @param objectMapper
     */
    public StreamExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Translate http status code to Java Exception.
     *
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
        exception.setExceptionFields(error.getExceptionFields());
        exception.setDuration(error.getDuration());
        exception.setException(error.getException());
        return exception;
    }
}
