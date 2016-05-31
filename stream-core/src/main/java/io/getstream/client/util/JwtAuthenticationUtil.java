package io.getstream.client.util;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class to generate a JWT token.
 */
public class JwtAuthenticationUtil {

    /**
     * A convenient way to indicate the 'all' quantifier.
     */
    public final static String ALL = "*";

    /**
     * Generate JWT token.
     * @param secretKey API Secret
     * @param action Action to be performed
     * @param resource Target resource
     * @param feedId FeedId (if null it will not be added to the payload)
     * @param userId UserId (if null it will not be added to the payload)
     * @return Token string
     */
    public static String generateToken(final String secretKey, final String action, final String resource, final String feedId, final String userId) {
        Map<String, Object> claims = new LinkedHashMap<String, Object>();
        claims.put("action", action);
        claims.put("resource", resource);
        if (null != feedId) {
            claims.put("feed_id", feedId);
        }
        if (null != userId) {
            claims.put("user_id", userId);
        }
        return new JWTSigner(secretKey).sign(claims, new JWTSigner.Options().setAlgorithm(Algorithm.HS256));
    }
}
