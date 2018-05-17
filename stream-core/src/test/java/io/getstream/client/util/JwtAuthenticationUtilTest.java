package io.getstream.client.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

import static io.getstream.client.util.JwtAuthenticationUtil.ALL;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JwtAuthenticationUtilTest {

    private static final String SECRET_KEY = "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2";

    @Test
    public void testGenerateToken() throws Exception {
        Map<String, Claim> map = verifyToken(
                JwtAuthenticationUtil.generateToken(
                        SECRET_KEY,
                        "activities",
                        "myResource",
                        null,
                        null)
        );
        assertTrue(map.size() > 0);
        assertThat(map.get("action").asString(), is("activities"));
    }

    @Test
    public void testGenerateTokenWithFeedId() throws Exception {
        Map<String, Claim> map = verifyToken(
                JwtAuthenticationUtil.generateToken(
                        SECRET_KEY,
                        "activities",
                        ALL,
                        "feedId",
                        null)
        );
        assertTrue(map.size() > 0);
        assertThat(map.get("resource").asString(), is(ALL));
        assertThat(map.get("feed_id").asString(), is("feedId"));
    }

    @Test
    public void testGenerateTokenWithUserId() throws Exception {
        Map<String, Claim> map = verifyToken(
                JwtAuthenticationUtil.generateToken(
                        SECRET_KEY,
                        "activities",
                        ALL,
                        null,
                        "userId1")
        );
        assertTrue(map.size() > 0);
        assertThat(map.get("resource").asString(), is(ALL));
        assertThat(map.get("user_id").asString(), is("userId1"));
    }

    @Test
    public void testGenerateTokenWithFeedAndUserId() throws Exception {
        Map<String, Claim> map = verifyToken(
                JwtAuthenticationUtil.generateToken(
                        SECRET_KEY,
                        "activities",
                        ALL,
                        "feedId",
                        "userId1")
        );
        assertTrue(map.size() > 0);
        assertThat(map.get("resource").asString(), is(ALL));
        assertThat(map.get("feed_id").asString(), is("feedId"));
        assertThat(map.get("user_id").asString(), is("userId1"));
    }

    private Map<String, Claim> verifyToken(final String token) {
        byte[] secret = SECRET_KEY.getBytes();
        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaims();
    }
}