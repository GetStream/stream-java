package io.getstream.client.utils;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class SignatureUtilsTest {

	@Test
	public void shouldCalculateHash() throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println(
				SignatureUtils.calculateHMAC("secret", "user:1"));
	}

}
