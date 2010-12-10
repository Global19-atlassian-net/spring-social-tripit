package org.springframework.security.encrypt;

import static org.springframework.security.encrypt.CipherUtils.*;
import static org.springframework.security.encrypt.EncodingUtils.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * A string encryptor that applies password-based MD5 plus DES symmetric key
 * encyption. Note: this encryptor does not apply any salt. Designed to be used
 * to encrypt fields that are queryable; for example, a indexed field such as an
 * OAuth apiKey.
 * 
 * @author Keith Donald
 */
// TODO evaluate AES for higher-level of security
public class SearchableStringEncryptor implements StringEncryptor {

	private final Cipher encryptor;

	private final Cipher decryptor;

	public SearchableStringEncryptor(String password, String salt) {
		String algorithm = "PBEWithMD5AndDES";
		byte[] saltBytes = hexDecode(salt);
		SecretKey secretKey = newSecretKey(algorithm, password);
		encryptor = newCipher(algorithm);
		initCipher(encryptor, Cipher.ENCRYPT_MODE, secretKey, saltBytes, 1000);
		decryptor = newCipher(algorithm);
		initCipher(decryptor, Cipher.DECRYPT_MODE, secretKey, saltBytes, 1000);
	}

	public String encrypt(String text) {
		return hexEncode(doFinal(encryptor, utf8Encode(text)));
	}

	public String decrypt(String encryptedText) {
		return utf8Decode(doFinal(decryptor, hexDecode(encryptedText)));
	}

}