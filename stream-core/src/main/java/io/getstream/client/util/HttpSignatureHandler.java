package io.getstream.client.util;

import io.getstream.client.config.AuthenticationHandlerConfiguration;
import org.tomitribe.auth.signatures.Signature;
import org.tomitribe.auth.signatures.Signer;

import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Helper class to handle Http Signature Authentication.
 */
public class HttpSignatureHandler {

    public static final String X_API_KEY_HEADER = "X-Api-Key";

    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String DATE_HEADER = "Date";

    private final Signer signer;

    /**
     * Create a {@link Signature} template object using the incoming credentials.
     * @param authConfig Credentials container
     */
    public HttpSignatureHandler(final AuthenticationHandlerConfiguration authConfig) {
        signer = new Signer(new SecretKeySpec(authConfig.getSecretKey().getBytes(), "HmacSHA256"),
                new Signature(authConfig.getApiKey(), "hmac-sha256", null, DATE_HEADER));
    }

    /**
     * Get the Signer.
     * @return A thread-safe Signer
     */
    protected Signer getSigner() {
        return signer;
    }

    /**
     * Utility method to provide today date.
     * @return A string representing the date of today
     */
    protected String getTodayDate() {
        final SimpleDateFormat today = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        today.setTimeZone(TimeZone.getTimeZone("GMT"));
        return today.format(new Date());
    }
}
