/*
 * Copyright (c)  2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: $date $time
 */

package com.globaltravelrule.sodium.test.utils;

import com.globaltravelrule.sodium.test.BaseTest;
import com.globaltravelrule.sodium.utils.Base64MessageEncoder;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class Base64MessageEncoderTest extends BaseTest {

    @Test
    public void decodeEqualsEncode() {
        Base64MessageEncoder encoder = new Base64MessageEncoder();
        String expected = "This is a hello from lazysodium";
        String cipherText = "VGhpcyBpcyBhIGhlbGxvIGZyb20gbGF6eXNvZGl1bQ==";
        byte[] plainText = encoder.decode(cipherText);
        String plain = new String(plainText, StandardCharsets.UTF_8);
        assertEquals(expected, plain);
        assertEquals(cipherText, encoder.encode(expected.getBytes(StandardCharsets.UTF_8)));
    }
}
