package io.getstream.client.utils;

import com.google.common.collect.ImmutableList;
import com.sun.jersey.core.util.Base64;
import io.getstream.client.model.activities.BaseActivity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public final class SignatureUtils {

	private static final String HMAC_SHA1 = "HmacSHA1";
    public static final String UTF_8 = "UTF-8";

    private SignatureUtils() {
		throw new AssertionError();
	}

	/**
	 * Sign all the recipients in the activity.
	 * @param secretKey Secret key
	 * @param activity Activity to sign.
	 */
	public static void addSignatureToRecipients(final String secretKey, final BaseActivity activity) {
		ImmutableList.Builder<String> recipients = ImmutableList.builder();
		for (String recipient : activity.getTo()) {
			try {
				recipients.add(String.format("%s %s", recipient, SignatureUtils.calculateHMAC(secretKey, recipient.replace(":", ""))));
			} catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
				throw new RuntimeException("Fatal error: cannot create authentication token.");
			}
		}
		activity.setTo(recipients.build());
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
		return escapeDigest(new String(Base64.encode(mac.doFinal(feedId.getBytes(UTF_8)))));
	}

    private static byte[] toSHA1(final String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-1").digest(key.getBytes(UTF_8));
    }

    private static String escapeDigest(final String digest) {
        return digest.replace("+", "-").replace("/", "_").replaceAll("^=+", "").replaceAll("=+$", "");
    }

}
