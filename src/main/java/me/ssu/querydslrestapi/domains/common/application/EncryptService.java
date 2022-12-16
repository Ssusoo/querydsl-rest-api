package me.ssu.querydslrestapi.domains.common.application;

import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.global.properties.EnvProperties;
import me.ssu.querydslrestapi.global.util.KISA_SEED_CBC;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 암호화(Encrypt) 서비스
 */
@Service
@Slf4j
public class EncryptService {

	private final EnvProperties env;
	private static byte[] secretKey;
	private static byte[] iv;

	public EncryptService(EnvProperties env) {
		this.env = env;
		secretKey = env.getKisaSeedCbcSecretKey();
		iv = env.getKisaSeedCbcIv();
	}

	public static String getEncryptedValue(String value) {
		byte[] plain = value.getBytes(StandardCharsets.UTF_8);
		var encryptedValue = KISA_SEED_CBC.SEED_CBC_Encrypt(secretKey, iv,
				plain, 0, plain.length);

		return new String(Base64.getEncoder().encode(encryptedValue), StandardCharsets.UTF_8);
	}

	public static String getDecryptedValue(String value) {
		byte[] plain = Base64.getDecoder().decode(value);

		var decryptedValue = KISA_SEED_CBC.SEED_CBC_Decrypt(secretKey, iv,
				plain, 0, plain.length);

		return new String(decryptedValue, StandardCharsets.UTF_8);
	}
}