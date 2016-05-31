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
package io.getstream.client.okhttp.repo.utils;

import com.squareup.okhttp.Request;
import io.getstream.client.model.feeds.BaseFeed;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Support utils for StreamRepository.
 */
public class StreamRepoUtils {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_AUTH_TYPE = "stream-auth-type";

    private StreamRepoUtils() {
        throw new AssertionError();
    }

    /**
     * Generate the authentication header.
     *
     * @param feed Source feed
     * @param secretKey Secret key
     * @param httpRequest incoming HTTP request
     * @return Request with the Authorization header.
     */
    public static Request.Builder addAuthentication(BaseFeed feed, String secretKey, Request.Builder httpRequest) {
        httpRequest.addHeader("Authorization", createFeedSignature(feed, secretKey));
        return httpRequest;
    }

    /**
     * Generate the token for a feed.
     *
     * @param feed Source feed
     * @param secretKey Secret key
     * @return String feed token.
     */
    public static String createFeedToken(BaseFeed feed, String secretKey){
        try {
            return SignatureUtils.calculateHMAC(secretKey, feed.getFeedId());
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException("Fatal error: cannot create authentication token.");
        }
    }

    /**
     * Generate the signature for a feed.
     *
     * @param feed Source feed
     * @param secretKey Secret key
     * @return String feed token.
     */
    public static String createFeedSignature(BaseFeed feed, String secretKey){
        String token = createFeedToken(feed, secretKey);
        return String.format("%s %s", feed.getFeedId(), token);
    }

    /**
     * Add authentication headers to the request using the JWT authentication type.
     * @param token JWT token
     * @param requestBuilder Outgoing request builder
     * @return The same request with authentication headers.
     */
    public static Request.Builder addJwtAuthentication(String token, Request.Builder requestBuilder) {
        requestBuilder.addHeader(HEADER_AUTHORIZATION, token);
        requestBuilder.addHeader(HEADER_AUTH_TYPE, "jwt");
        return requestBuilder;
    }
}
