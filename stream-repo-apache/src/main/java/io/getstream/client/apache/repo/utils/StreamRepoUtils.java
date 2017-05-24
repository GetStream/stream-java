package io.getstream.client.apache.repo.utils;

import io.getstream.client.model.feeds.BaseFeed;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

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
    public static HttpRequestBase addAuthentication(BaseFeed feed, String secretKey, HttpRequestBase httpRequest) {
        httpRequest.addHeader(HEADER_AUTHORIZATION, createFeedSignature(feed, secretKey));
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
     * @param request Outgoing request
     * @return The same request with authentication headers.
     */
    public static HttpPost addJwtAuthentication(String token, HttpPost request) {
        request.addHeader(HEADER_AUTHORIZATION, token);
        request.addHeader(HEADER_AUTH_TYPE, "jwt");
        return request;
    }

    /**
     * Add authentication headers to the request using the JWT authentication type.
     * @param token JWT token
     * @param request Outgoing request
     * @return The same request with authentication headers.
     */
    public static HttpGet addJwtAuthentication(String token, HttpGet request) {
        request.addHeader(HEADER_AUTHORIZATION, token);
        request.addHeader(HEADER_AUTH_TYPE, "jwt");
        return request;
    }
}
