/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.interfaces;


import com.globaltravelrule.sodium.utils.Constants;
import com.globaltravelrule.sodium.utils.Key;

public interface Stream {

    // REGULAR CHACHA

    int CHACHA20_NONCEBYTES = 8,
        CHACHA20_KEYBYTES = 32;
    long CHACHA20_MESSAGEBYTES_MAX = Constants.SIZE_MAX;


    // IETF CHACHA

    int CHACHA20_IETF_NONCEBYTES = 12,
        CHACHA20_IETF_KEYBYTES = 32;
    long CHACHA20_IETF_MESSAGEBYTES_MAX = Constants.GB_256;


    // SALSA20

    int SALSA20_NONCEBYTES = 12,
        SALSA20_KEYBYTES = 32;
    long SALSA20_MESSAGEBYTES_MAX = Constants.SIZE_MAX;


    // XSALSA20

    int XSALSA20_NONCEBYTES = 24,
        XSALSA20_KEYBYTES = 32;
    long XSALSA20_MESSAGEBYTES_MAX = Constants.SIZE_MAX;

    int NONCEBYTES = XSALSA20_NONCEBYTES,
        KEYBYTES = XSALSA20_KEYBYTES;
    long MESSAGEBYTES_MAX = XSALSA20_MESSAGEBYTES_MAX;


    enum Method {
        CHACHA20,
        CHACHA20_IETF,
        SALSA20,
        XSALSA20,
    }



    interface Native {

        void cryptoStreamChaCha20Keygen(byte[] key);

        boolean cryptoStreamChaCha20(
                byte[] c,
                long cLen,
                byte[] nonce,
                byte[] key
        );

        boolean cryptoStreamChaCha20Xor(
                byte[] cipher,
                byte[] message,
                long messageLen,
                byte[] nonce,
                byte[] key
        );

        boolean cryptoStreamChacha20XorIc(
                byte[] cipher,
                byte[] message,
                long messageLen,
                byte[] nonce,
                long ic,
                byte[] key
        );

        // IETF CHACHA

        void cryptoStreamChaCha20IetfKeygen(byte[] key);

        boolean cryptoStreamChaCha20Ietf(
                byte[] c,
                long cLen,
                byte[] nonce,
                byte[] key
        );

        boolean cryptoStreamChaCha20IetfXor(
                byte[] cipher,
                byte[] message,
                long messageLen,
                byte[] nonce,
                byte[] key
        );

        boolean cryptoStreamChacha20IetfXorIc(
                byte[] cipher,
                byte[] message,
                long messageLen,
                byte[] nonce,
                long ic,
                byte[] key
        );

        // Salsa20

        void cryptoStreamSalsa20Keygen(byte[] key);

        boolean cryptoStreamSalsa20(
                byte[] c,
                long cLen,
                byte[] nonce,
                byte[] key
        );

        boolean cryptoStreamSalsa20Xor(
                byte[] cipher,
                byte[] message,
                long messageLen,
                byte[] nonce,
                byte[] key
        );

        boolean cryptoStreamSalsa20XorIc(
                byte[] cipher,
                byte[] message,
                long messageLen,
                byte[] nonce,
                long ic,
                byte[] key
        );

        // XSalsa20

        void cryptoStreamXSalsa20Keygen(byte[] key);

        boolean cryptoStreamXSalsa20(
                byte[] c,
                long cLen,
                byte[] nonce,
                byte[] key
        );

        boolean cryptoStreamXSalsa20Xor(
                byte[] cipher,
                byte[] message,
                long messageLen,
                byte[] nonce,
                byte[] key
        );

    }



    interface Lazy {

        Key cryptoStreamKeygen(Method method);

        byte[] cryptoStream(
                byte[] nonce,
                Key key,
                Method method
        );

        String cryptoStreamXor(
                String message,
                byte[] nonce,
                Key key,
                Method method
        );

        String cryptoStreamXorDecrypt(
                String cipher,
                byte[] nonce,
                Key key,
                Method method
        );

        String cryptoStreamXorIc(
                String message,
                byte[] nonce,
                long ic,
                Key key,
                Method method
        );

        String cryptoStreamXorIcDecrypt(
                String cipher,
                byte[] nonce,
                long ic,
                Key key,
                Method method
        );

    }

}
