package com.kingtest.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StringEncryptionServiceTest {
    @Autowired
    private StringEncryptionService stringEncryptionService;

    @Test
    void testEncryptionAndDecryption() {
        String text = "TEXT";
        String encryptedText = this.stringEncryptionService.encrypt(text);
        String decryptedText = this.stringEncryptionService.decrypt(encryptedText);

        assertEquals(text, decryptedText);
    }
}
