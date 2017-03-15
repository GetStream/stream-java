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
