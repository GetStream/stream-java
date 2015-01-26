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
				SignatureUtils.calculateHMAC("245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2", "user2"));
	}

}
