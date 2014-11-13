package io.getstream.client.utils;

import com.sun.jersey.core.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureUtils {

	private static final String HMAC_SHA1 = "HmacSHA1";
    public static final String UTF_8 = "UTF-8";

    private SignatureUtils() {
		throw new IllegalStateException();
	}

	/**
	 * Calculate HMAC.
	 * @param secretKey Secret key
	 * @param feedId Feed Id
	 * @return Hash key
	 * @throws java.security.SignatureException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String calculateHMAC(final String secretKey, final String feedId)
            throws java.security.SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		SecretKeySpec signingKey = new SecretKeySpec(toSHA1(secretKey), HMAC_SHA1);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(signingKey);
		return new String(Base64.encode(mac.doFinal(feedId.getBytes(UTF_8))));
	}

    private static byte[] toSHA1(final String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-1").digest(key.getBytes(UTF_8));
    }

}
