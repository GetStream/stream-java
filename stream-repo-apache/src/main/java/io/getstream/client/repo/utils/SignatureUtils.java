/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
package io.getstream.client.repo.utils;

import com.google.common.collect.ImmutableList;
import io.getstream.client.exception.InvalidOrMissingInputException;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Utility class to handle signatures in Stream.io.
 */
public final class SignatureUtils {

    private static final String HMAC_SHA1 = "HmacSHA1";
    public static final String UTF_8 = "UTF-8";

    private SignatureUtils() {
        throw new AssertionError();
    }

    /**
     * Sign all the recipients in the activity.
     *
     * @param secretKey Secret key
     * @param activity  Activity to sign.
     */
    public static void addSignatureToRecipients(final String secretKey, final BaseActivity activity) throws StreamClientException {
        if (activity.getTo() != null && !activity.getTo().isEmpty()) {
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
    }

    /**
     * Calculate HMAC.
     *
     * @param secretKey Secret key
     * @param feedId    Feed Id
     * @return Hash key
     * @throws java.security.SignatureException
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String calculateHMAC(final String secretKey, final String feedId)
            throws java.security.SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec signingKey = new SecretKeySpec(toSHA1(secretKey), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        return escapeDigest(DatatypeConverter.printBase64Binary(mac.doFinal(feedId.getBytes(UTF_8))));
    }

    private static byte[] toSHA1(final String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-1").digest(key.getBytes(UTF_8));
    }

    private static String escapeDigest(final String digest) {
        return digest.replace("+", "-").replace("/", "_").replaceAll("^=+", "").replaceAll("=+$", "");
    }

}
