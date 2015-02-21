package io.getstream.client.utils;

import io.getstream.client.model.feeds.BaseFeed;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Support utils for StreamRepository.
 */
public class StreamRepoUtils {

    private StreamRepoUtils() {
        throw new AssertionError();
    }

    /**
     * Generate the authentication header.
     * @param feed
     * @param secretKey
     * @param httpRequest
     * @return Request with the Authorization header.
     */
    public static HttpRequestBase addAuthentication(BaseFeed feed, String secretKey, HttpRequestBase httpRequest) {
        String tokenId = feed.getFeedSlug().concat(feed.getUserId());
        try {
            httpRequest.addHeader("Authorization", String.format("%s %s", tokenId, SignatureUtils.calculateHMAC(secretKey, tokenId)));
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException("Fatal error: cannot create authentication token.");
        }
        return httpRequest;
    }

}
