/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.interfaces.AEAD;
import com.globaltravelrule.sodium.interfaces.MessageEncoder;
import com.globaltravelrule.sodium.utils.DetachedDecrypt;
import com.globaltravelrule.sodium.utils.DetachedEncrypt;
import com.globaltravelrule.sodium.utils.HexMessageEncoder;
import com.globaltravelrule.sodium.utils.Key;
import org.junit.Test;

import javax.crypto.AEADBadTagException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class AEADTest extends BaseTest {

    private final String PASSWORD = "superSecurePassword";
    private final MessageEncoder encoder = new HexMessageEncoder();

    @Test
    public void encryptChacha() throws AEADBadTagException {

        Key key = lazySodium.keygen(AEAD.Method.CHACHA20_POLY1305);

        byte[] nPub = lazySodium.nonce(AEAD.CHACHA20POLY1305_NPUBBYTES);

        String cipher = lazySodium.encrypt(PASSWORD, null, nPub, key, AEAD.Method.CHACHA20_POLY1305);
        String decrypted = lazySodium.decrypt(cipher, null, nPub, key, AEAD.Method.CHACHA20_POLY1305);

        assertEquals(decrypted, PASSWORD);
    }

    @Test
    public void encryptChachaMalformedCipher() {
        assertThrows(AEADBadTagException.class, () -> {
            Key key = lazySodium.keygen(AEAD.Method.CHACHA20_POLY1305);

            byte[] nPub = lazySodium.nonce(AEAD.CHACHA20POLY1305_NPUBBYTES);

            String cipher = lazySodium.encrypt(PASSWORD, null, nPub, key, AEAD.Method.CHACHA20_POLY1305);
            String decrypted = lazySodium.decrypt(malformCipher(cipher), null, nPub, key, AEAD.Method.CHACHA20_POLY1305);
        });
    }

    @Test
    public void encryptChachaIetf() throws AEADBadTagException {

        Key key = lazySodium.keygen(AEAD.Method.CHACHA20_POLY1305_IETF);

        byte[] nPub = lazySodium.nonce(AEAD.CHACHA20POLY1305_IETF_NPUBBYTES);

        String cipher = lazySodium.encrypt(PASSWORD, null, nPub, key, AEAD.Method.CHACHA20_POLY1305_IETF);
        String decrypted = lazySodium.decrypt(cipher, null, nPub, key, AEAD.Method.CHACHA20_POLY1305_IETF);

        assertEquals(decrypted, PASSWORD);
    }

    @Test
    public void encryptChachaIetfMalformedCipher() throws AEADBadTagException {
        assertThrows(AEADBadTagException.class, () -> {
            Key key = lazySodium.keygen(AEAD.Method.CHACHA20_POLY1305_IETF);

            byte[] nPub = lazySodium.nonce(AEAD.CHACHA20POLY1305_IETF_NPUBBYTES);

            String cipher = lazySodium.encrypt(PASSWORD, null, nPub, key, AEAD.Method.CHACHA20_POLY1305_IETF);
            String decrypted = lazySodium.decrypt(malformCipher(cipher), null, nPub, key, AEAD.Method.CHACHA20_POLY1305_IETF);
        });
    }

    @Test
    public void encryptXChacha() throws AEADBadTagException {

        Key key = lazySodium.keygen(AEAD.Method.XCHACHA20_POLY1305_IETF);

        byte[] nPub = lazySodium.nonce(AEAD.XCHACHA20POLY1305_IETF_NPUBBYTES);

        String cipher = lazySodium.encrypt(PASSWORD, null, nPub, key, AEAD.Method.XCHACHA20_POLY1305_IETF);
        String decrypted = lazySodium.decrypt(cipher, null, nPub, key, AEAD.Method.XCHACHA20_POLY1305_IETF);

        assertEquals(decrypted, PASSWORD);
    }

    @Test
    public void encryptXChachaMalformedCipher() throws AEADBadTagException {
        assertThrows(AEADBadTagException.class, () -> {
            Key key = lazySodium.keygen(AEAD.Method.XCHACHA20_POLY1305_IETF);

            byte[] nPub = lazySodium.nonce(AEAD.XCHACHA20POLY1305_IETF_NPUBBYTES);

            String cipher = lazySodium.encrypt(PASSWORD, null, nPub, key, AEAD.Method.XCHACHA20_POLY1305_IETF);
            String decrypted = lazySodium.decrypt(malformCipher(cipher), null, nPub, key, AEAD.Method.XCHACHA20_POLY1305_IETF);
        });
    }

    @Test
    public void encryptChachaDetached() throws AEADBadTagException {

        Key key = lazySodium.keygen(AEAD.Method.CHACHA20_POLY1305);

        byte[] nPub = lazySodium.nonce(AEAD.CHACHA20POLY1305_NPUBBYTES);

        DetachedEncrypt detachedEncrypt
                = lazySodium.encryptDetached(PASSWORD, null, null, nPub, key, AEAD.Method.CHACHA20_POLY1305);

        DetachedDecrypt detachedDecrypt = lazySodium.decryptDetached(detachedEncrypt, null, null, nPub, key, AEAD.Method.CHACHA20_POLY1305);

        assertEquals(detachedDecrypt.getMessageString(), PASSWORD);
    }

    @Test
    public void encryptChachaDetachedMalformedCipher() throws AEADBadTagException {
        assertThrows(AEADBadTagException.class, () -> {
            Key key = lazySodium.keygen(AEAD.Method.CHACHA20_POLY1305);

            byte[] nPub = lazySodium.nonce(AEAD.CHACHA20POLY1305_NPUBBYTES);

            DetachedEncrypt detachedEncrypt
                    = lazySodium.encryptDetached(PASSWORD, null, null, nPub, key, AEAD.Method.CHACHA20_POLY1305);

            DetachedEncrypt malformed = new DetachedEncrypt(malformCipherBytes(detachedEncrypt.getCipherString()), detachedEncrypt.getMac());
            DetachedDecrypt detachedDecrypt = lazySodium.decryptDetached(malformed, null, null, nPub, key, AEAD.Method.CHACHA20_POLY1305);
        });
    }


    @Test
    public void encryptChachaIetfDetached() throws AEADBadTagException {
        Key key = lazySodium.keygen(AEAD.Method.CHACHA20_POLY1305_IETF);
        byte[] nPub = lazySodium.nonce(AEAD.CHACHA20POLY1305_IETF_NPUBBYTES);

        DetachedEncrypt detachedEncrypt
                = lazySodium.encryptDetached(PASSWORD, null, null, nPub, key, AEAD.Method.CHACHA20_POLY1305_IETF);
        DetachedDecrypt detachedDecrypt = lazySodium.decryptDetached(detachedEncrypt, null, null, nPub, key, AEAD.Method.CHACHA20_POLY1305_IETF);
        assertEquals(detachedDecrypt.getMessageString(), PASSWORD);
    }

    @Test
    public void encryptChachaIetfDetachedMalformedCipher() {
        assertThrows(AEADBadTagException.class, () -> {
            Key key = lazySodium.keygen(AEAD.Method.CHACHA20_POLY1305_IETF);
            byte[] nPub = lazySodium.nonce(AEAD.CHACHA20POLY1305_IETF_NPUBBYTES);

            DetachedEncrypt detachedEncrypt
                    = lazySodium.encryptDetached(PASSWORD, null, null, nPub, key, AEAD.Method.CHACHA20_POLY1305_IETF);
            DetachedEncrypt malformed = new DetachedEncrypt(malformCipherBytes(detachedEncrypt.getCipherString()), detachedEncrypt.getMac());
            DetachedDecrypt detachedDecrypt = lazySodium.decryptDetached(malformed, null, null, nPub, key, AEAD.Method.CHACHA20_POLY1305_IETF);
        });
    }

    @Test
    public void encryptXChachaDetached() throws AEADBadTagException {
        Key key = lazySodium.keygen(AEAD.Method.XCHACHA20_POLY1305_IETF);
        byte[] nPub = lazySodium.nonce(AEAD.XCHACHA20POLY1305_IETF_NPUBBYTES);

        DetachedEncrypt detachedEncrypt
                = lazySodium.encryptDetached(PASSWORD, null, null, nPub, key, AEAD.Method.XCHACHA20_POLY1305_IETF);

        DetachedDecrypt detachedDecrypt = lazySodium.decryptDetached(detachedEncrypt, null, null, nPub, key, AEAD.Method.XCHACHA20_POLY1305_IETF);
        assertEquals(detachedDecrypt.getMessageString(), PASSWORD);
    }

    @Test
    public void encryptXChachaDetachedMalformedCipher() throws AEADBadTagException {
        assertThrows(AEADBadTagException.class, () -> {
            Key key = lazySodium.keygen(AEAD.Method.XCHACHA20_POLY1305_IETF);
            byte[] nPub = lazySodium.nonce(AEAD.XCHACHA20POLY1305_IETF_NPUBBYTES);

            DetachedEncrypt detachedEncrypt
                    = lazySodium.encryptDetached(PASSWORD, null, null, nPub, key, AEAD.Method.XCHACHA20_POLY1305_IETF);
            DetachedEncrypt malformed = new DetachedEncrypt(malformCipherBytes(detachedEncrypt.getCipherString()), detachedEncrypt.getMac());
            DetachedDecrypt detachedDecrypt = lazySodium.decryptDetached(malformed, null, null, nPub, key, AEAD.Method.XCHACHA20_POLY1305_IETF);
        });
    }


    @Test
    public void encryptAES() throws AEADBadTagException {
        if (lazySodium.cryptoAeadAES256GCMIsAvailable()) {
            Key key = lazySodium.keygen(AEAD.Method.AES256GCM);

            byte[] nPub = lazySodium.nonce(AEAD.AES256GCM_NPUBBYTES);

            String cipher = lazySodium.encrypt(PASSWORD, null, nPub, key, AEAD.Method.AES256GCM);
            String decrypted = lazySodium.decrypt(cipher, null, nPub, key, AEAD.Method.AES256GCM);

            assertEquals(decrypted, PASSWORD);
        }
    }

    @Test
    public void encryptAESMalformedCipher() {
        if (lazySodium.cryptoAeadAES256GCMIsAvailable()) {
            assertThrows(AEADBadTagException.class, () -> {
                Key key = lazySodium.keygen(AEAD.Method.AES256GCM);

                byte[] nPub = lazySodium.nonce(AEAD.AES256GCM_NPUBBYTES);

                String cipher = lazySodium.encrypt(PASSWORD, null, nPub, key, AEAD.Method.AES256GCM);
                String decrypted = lazySodium.decrypt(malformCipher(cipher), null, nPub, key, AEAD.Method.AES256GCM);

                assertEquals(decrypted, PASSWORD);
            });
        }
    }

    @Test
    public void encryptAESDetached() throws AEADBadTagException {
        if (lazySodium.cryptoAeadAES256GCMIsAvailable()) {
            Key key = lazySodium.keygen(AEAD.Method.AES256GCM);
            byte[] nPub = lazySodium.nonce(AEAD.AES256GCM_NPUBBYTES);
            DetachedEncrypt detachedEncrypt
                    = lazySodium.encryptDetached(PASSWORD, null, null, nPub, key, AEAD.Method.AES256GCM);
            DetachedDecrypt detachedDecrypt = lazySodium.decryptDetached(detachedEncrypt, null, null, nPub, key, AEAD.Method.AES256GCM);
            assertEquals(detachedDecrypt.getMessageString(), PASSWORD);
        }
    }

    @Test
    public void encryptAESDetachedMalformedCipher() throws AEADBadTagException {
        if (lazySodium.cryptoAeadAES256GCMIsAvailable()) {
            assertThrows(AEADBadTagException.class, () -> {
                Key key = lazySodium.keygen(AEAD.Method.AES256GCM);
                byte[] nPub = lazySodium.nonce(AEAD.AES256GCM_NPUBBYTES);

                DetachedEncrypt detachedEncrypt
                        = lazySodium.encryptDetached(PASSWORD, null, null, nPub, key, AEAD.Method.AES256GCM);
                DetachedEncrypt malformed = new DetachedEncrypt(malformCipherBytes(detachedEncrypt.getCipherString()), detachedEncrypt.getMac());
                lazySodium.decryptDetached(malformed, null, null, nPub, key, AEAD.Method.AES256GCM);
            });
        }
    }

    private String malformCipher(String ciphertext) {
        byte[] malformedBuf = malformCipherBytes(ciphertext);
        return encoder.encode(malformedBuf);
    }

    private byte[] malformCipherBytes(String ciphertext) {
        byte[] cipherBuf = encoder.decode(ciphertext);
        for (int i = 0; i < cipherBuf.length; i++) {
            cipherBuf[i] ^= 0xff;
        }
        return cipherBuf;
    }
}
