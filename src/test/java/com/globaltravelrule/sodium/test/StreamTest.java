/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.interfaces.Stream;
import com.globaltravelrule.sodium.interfaces.StreamJava;
import com.globaltravelrule.sodium.utils.Key;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StreamTest extends BaseTest {

    private final String message1 = "A top secret message.";

    @Test
    public void javaXChaCha20() {
        StreamJava.Lazy streamLazy = lazySodium;

        byte[] nonce = lazySodium.nonce(StreamJava.XCHACHA20_NONCEBYTES);
        Key key = streamLazy.cryptoStreamKeygen(StreamJava.Method.XCHACHA20);
        String cipher = streamLazy.cryptoStreamXor(message1, nonce, key, StreamJava.Method.XCHACHA20);
        String finalMsg = streamLazy.cryptoStreamXorDecrypt(cipher, nonce, key, StreamJava.Method.XCHACHA20);

        assertEquals(message1, finalMsg);
    }

    @Test
    public void javaSalsa2012() {
        StreamJava.Lazy streamLazy = lazySodium;

        byte[] nonce = lazySodium.nonce(StreamJava.SALSA2012_NONCEBYTES);
        Key key = streamLazy.cryptoStreamKeygen(StreamJava.Method.SALSA20_12);
        String cipher = streamLazy.cryptoStreamXor(message1, nonce, key, StreamJava.Method.SALSA20_12);
        String finalMsg = streamLazy.cryptoStreamXorDecrypt(cipher, nonce, key, StreamJava.Method.SALSA20_12);

        assertEquals(message1, finalMsg);
    }

    @Test
    public void javaSalsa208() {
        StreamJava.Lazy streamLazy = lazySodium;

        byte[] nonce = lazySodium.nonce(StreamJava.SALSA208_NONCEBYTES);
        Key key = streamLazy.cryptoStreamKeygen(StreamJava.Method.SALSA20_8);
        String cipher = streamLazy.cryptoStreamXor(message1, nonce, key, StreamJava.Method.SALSA20_8);
        String finalMsg = streamLazy.cryptoStreamXorDecrypt(cipher, nonce, key, StreamJava.Method.SALSA20_8);

        assertEquals(message1, finalMsg);
    }

    @Test
    public void chacha20() {
        byte[] c = new byte[32];
        int cLen = c.length;
        byte[] nonce = lazySodium.nonce(Stream.CHACHA20_NONCEBYTES);
        byte[] key = "RANDOM_KEY_OF_32_BYTES_LENGTH121".getBytes();

        lazySodium.cryptoStreamChaCha20(c, cLen, nonce, key);

        // Encrypt
        byte[] mBytes = message1.getBytes();
        byte[] cipher = new byte[mBytes.length];
        lazySodium.cryptoStreamChaCha20Xor(cipher, mBytes, mBytes.length, nonce, key);

        // Decrypt
        byte[] result = new byte[mBytes.length];
        lazySodium.cryptoStreamChaCha20Xor(result, cipher, cipher.length, nonce, key);

        assertEquals(message1, lazySodium.str(result));
    }

    @Test
    public void lazyChacha20() {
        Stream.Lazy streamLazy = lazySodium;

        byte[] nonce = lazySodium.nonce(Stream.CHACHA20_NONCEBYTES);
        Key key = streamLazy.cryptoStreamKeygen(Stream.Method.CHACHA20);
        String cipher = streamLazy.cryptoStreamXor(message1, nonce, key, Stream.Method.CHACHA20);
        String finalMsg = streamLazy.cryptoStreamXorDecrypt(cipher, nonce, key, Stream.Method.CHACHA20);

        assertEquals(message1, finalMsg);
    }

    @Test
    public void lazyChacha20Ietf() {
        Stream.Lazy streamLazy = lazySodium;

        byte[] nonce = lazySodium.nonce(Stream.CHACHA20_IETF_NONCEBYTES);
        Key key = streamLazy.cryptoStreamKeygen(Stream.Method.CHACHA20_IETF);
        String cipher = streamLazy.cryptoStreamXor(message1, nonce, key, Stream.Method.CHACHA20_IETF);
        String finalMsg = streamLazy.cryptoStreamXorDecrypt(cipher, nonce, key, Stream.Method.CHACHA20_IETF);

        assertEquals(message1, finalMsg);
    }

    @Test
    public void lazySalsa20() {
        Stream.Lazy streamLazy = lazySodium;

        String message = "Hello";

        byte[] nonce = lazySodium.nonce(Stream.SALSA20_NONCEBYTES);
        Key key = streamLazy.cryptoStreamKeygen(Stream.Method.SALSA20);
        String cipher = streamLazy.cryptoStreamXor(message, nonce, key, Stream.Method.SALSA20);
        String finalMsg = streamLazy.cryptoStreamXorDecrypt(cipher, nonce, key, Stream.Method.SALSA20);

        assertEquals(message, finalMsg);
    }

    @Test
    public void lazyXSalsa20() {
        Stream.Lazy streamLazy = lazySodium;

        byte[] nonce = lazySodium.nonce(Stream.XSALSA20_NONCEBYTES);
        Key key = streamLazy.cryptoStreamKeygen(Stream.Method.XSALSA20);
        String cipher = streamLazy.cryptoStreamXor(message1, nonce, key, Stream.Method.XSALSA20);
        String finalMsg = streamLazy.cryptoStreamXorDecrypt(cipher, nonce, key, Stream.Method.XSALSA20);

        assertEquals(message1, finalMsg);
    }
}
