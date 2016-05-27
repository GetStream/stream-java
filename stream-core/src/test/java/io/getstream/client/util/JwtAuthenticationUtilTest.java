package io.getstream.client.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class JwtAuthenticationUtilTest {

    private static final String SECRET_KEY = "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2";

    @Test
    public void testGenerateToken() throws Exception {
        assertThat(JwtAuthenticationUtil.generateToken(
                SECRET_KEY,
                "activities",
                "myResource",
                null,
                null),
                is("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY3Rpb24iOiJhY3Rpdml0aWVzIiwicmVzb3VyY2UiOi" +
                    "JteVJlc291cmNlIn0.zLHVXhkZWK_VTpX_iHxyg_pjQBVSgBKkeSGpP8W6zF0")
        );
    }

    @Test
    public void testGenerateTokenWithFeedId() throws Exception {
        assertThat(JwtAuthenticationUtil.generateToken(
                SECRET_KEY,
                "activities",
                JwtAuthenticationUtil.ALL,
                "feed1",
                null),
                is("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY3Rpb24iOiJhY3Rpdml0aWVzIiwicmVzb3VyY2UiOiIq" +
                    "IiwiZmVlZF9pZCI6ImZlZWQxIn0.uBBDb91Lo-D9k9LQY-nK1nXMYjcwgQ4MWao69s4unRI")
        );
    }

    @Test
    public void testGenerateTokenWithUserId() throws Exception {
        assertThat(JwtAuthenticationUtil.generateToken(
                SECRET_KEY,
                "activities",
                JwtAuthenticationUtil.ALL,
                null,
                "userId1"),
                is("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY3Rpb24iOiJhY3Rpdml0aWVzIiwicmVzb3VyY2UiOiIqIi" +
                    "widXNlcl9pZCI6InVzZXJJZDEifQ.Ssr60hHaffWZ7TKj69bb7f5kxZ1b0K56GUAj_2tpkgg")
        );
    }

    @Test
    public void testGenerateTokenWithFeedAndUserId() throws Exception {
        assertThat(JwtAuthenticationUtil.generateToken(
                SECRET_KEY,
                "activities",
                "myResource",
                "feedId",
                "userId"),
                is("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY3Rpb24iOiJhY3Rpdml0aWVzIiwicmVzb3VyY2UiOiJteVJl" +
                        "c291cmNlIiwidXNlcl9pZCI6InVzZXJJZCIsImZlZWRfaWQiOiJmZWVkSWQifQ.A7Qv7dmj3EwoZ93YBWKidRi9BLVikxpwfspGO0IccPU")
        );
    }
}