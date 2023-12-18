package com.kingtest.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

@Service
public class StringEncryptionService  {
    private static final String DEFAULT_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTE_SIZE = 12;
    private static final int TAG_LENGTH_SIZE = 128;

    @Value("${spring.encryption.secretKey}")
    private String secretKey;

    public String encrypt(final String inputString) {
        if (inputString == null) {
            return null;
        }

        final Key secretKeySpec = new SecretKeySpec(secretKey.getBytes(), DEFAULT_ALGORITHM);

        try {

            final byte[] iv = new byte[IV_LENGTH_BYTE_SIZE];

            final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER);

            final GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_SIZE, iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);

            final byte[] cipherText = cipher.doFinal(inputString.getBytes());

            final ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);

            byteBuffer.put(iv);

            byteBuffer.put(cipherText);

            final byte[] cipherMessage = byteBuffer.array();

            return Base64.getEncoder().encodeToString(cipherMessage);
        } catch (final Exception e) {
            System.out.println(e.getStackTrace());
            System.out.println("[StringEncryptionService] - Failed to encrypt data sensitive");
        }

        return null;
    }

    public String decrypt(final String encryptedString) {
        if (encryptedString == null) {
            return null;
        }

        final Key secretKeySpec = new SecretKeySpec(secretKey.getBytes(), DEFAULT_ALGORITHM);

        try {

            final var cipher = Cipher.getInstance(DEFAULT_CIPHER);

            final byte[] cipherMessage =
                    Base64.getDecoder().decode(encryptedString.getBytes(StandardCharsets.UTF_8));

            final AlgorithmParameterSpec gcmIv =
                    new GCMParameterSpec(TAG_LENGTH_SIZE, cipherMessage, 0, IV_LENGTH_BYTE_SIZE);

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmIv);

            return new String(
                    cipher.doFinal(
                            cipherMessage, IV_LENGTH_BYTE_SIZE, cipherMessage.length - IV_LENGTH_BYTE_SIZE));
        } catch (final Exception e) {
            // log.info("[StringEncryptionService] - Failed to decrypt data sensitive", e);
        }

        return null;
    }
}
