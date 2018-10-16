package io.getstream.client.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;

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
        JWTCreator.Builder jwtBuilder = JWT.create();

        if (null != action) {
            jwtBuilder = jwtBuilder.withClaim("action", action);
        }

        if (null != resource) {
            jwtBuilder = jwtBuilder.withClaim("resource", resource);
        }

        if (null != feedId) {
            jwtBuilder = jwtBuilder.withClaim("feed_id", feedId);
        }

        if (null != userId) {
            jwtBuilder = jwtBuilder.withClaim("user_id", userId);
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return jwtBuilder.sign(algorithm);
        } catch (UnsupportedEncodingException exc) {
            throw new IllegalStateException("Fatal error: JWT Algorithm unsupported.");
        }
    }

    /**
     * Generate JWT token.
     * @param secretKey API Secret
     * @param feedId FeedId (if null it will not be added to the payload)
     * @param userId UserId (if null it will not be added to the payload)
     * @return Token string
     */
    public static String generateToken(final String secretKey, final String feedId, final String userId) {
        JWTCreator.Builder jwtBuilder = JWT.create();

        if (null != feedId) {
            jwtBuilder = jwtBuilder.withClaim("feed_id", feedId);
        }
        if (null != userId) {
            jwtBuilder = jwtBuilder.withClaim("user_id", userId);
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return jwtBuilder.sign(algorithm);
        } catch (UnsupportedEncodingException exc) {
            throw new IllegalStateException("Fatal error: JWT Algorithm unsupported.");
        }
    }
}
