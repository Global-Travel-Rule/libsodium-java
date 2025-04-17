/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.interfaces.Box;
import com.globaltravelrule.sodium.utils.DetachedDecrypt;
import com.globaltravelrule.sodium.utils.DetachedEncrypt;
import com.globaltravelrule.sodium.utils.KeyPair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests public and private key encryption.
 */
public class BoxTest extends BaseTest {


    private Box.Lazy cryptoBoxLazy;

    @Before
    public void before() {
        cryptoBoxLazy = lazySodium;
    }

    @Test
    public void generateKeyPair() throws SodiumException {
        KeyPair keys = cryptoBoxLazy.cryptoBoxKeypair();
        assertNotNull(keys);
    }

    @Test
    public void generateDeterministicPublicKeyPair() throws SodiumException {
        byte[] seed = new byte[Box.SEEDBYTES];
        KeyPair keys = cryptoBoxLazy.cryptoBoxSeedKeypair(seed);
        KeyPair keys2 = cryptoBoxLazy.cryptoBoxSeedKeypair(seed);

        assertEquals(keys.getPublicKey().getAsHexString(), keys2.getPublicKey().getAsHexString());
    }

    @Test
    public void generateDeterministicSecretKeyPair() throws SodiumException {
        byte[] seed = new byte[Box.SEEDBYTES];
        KeyPair keys = cryptoBoxLazy.cryptoBoxSeedKeypair(seed);
        KeyPair keys2 = cryptoBoxLazy.cryptoBoxSeedKeypair(seed);

        assertEquals(keys.getSecretKey().getAsHexString(), keys2.getSecretKey().getAsHexString());
    }


    @Test
    public void encryptMessage() throws SodiumException {
        String message = "This should get encrypted";

        // Generate the client's keypair
        KeyPair clientKeys = cryptoBoxLazy.cryptoBoxKeypair();

        // Generate the server keypair
        KeyPair serverKeys = cryptoBoxLazy.cryptoBoxKeypair();


        // We're going to encrypt a message on the client and
        // send it to the server.
        byte[] nonce = lazySodium.nonce(Box.NONCEBYTES);
        KeyPair encryptionKeyPair = new KeyPair(serverKeys.getPublicKey(), clientKeys.getSecretKey());
        String encrypted = cryptoBoxLazy.cryptoBoxEasy(message, nonce, encryptionKeyPair);

        // ... In this space, you can theoretically send encrypted to
        // the server.

        // Now we can decrypt the encrypted message.
        KeyPair decryptionKeyPair = new KeyPair(clientKeys.getPublicKey(), serverKeys.getSecretKey());
        String decryptedMessage = cryptoBoxLazy.cryptoBoxOpenEasy(encrypted, nonce, decryptionKeyPair);

        // Public-private key encryption complete!
        assertEquals(message, decryptedMessage);
    }


    @Test
    public void encryptMessageBeforeNm() throws SodiumException {
        String message = "This should get encrypted";

        // Generate a keypair
        KeyPair keyPair = cryptoBoxLazy.cryptoBoxKeypair();

        // Generate a shared key which can be used
        // to encrypt and decrypt data
        String sharedKey = cryptoBoxLazy.cryptoBoxBeforeNm(keyPair);

        byte[] nonce = lazySodium.nonce(Box.NONCEBYTES);

        // Encrypt the data using the shared key
        String encrypted = cryptoBoxLazy.cryptoBoxEasyAfterNm(message, nonce, sharedKey);

        // Decrypt the data using the shared key
        String decryptedMessage = cryptoBoxLazy.cryptoBoxOpenEasyAfterNm(encrypted, nonce, sharedKey);

        assertEquals(message, decryptedMessage);
    }

    @Test
    public void encryptMessageBeforeNmDetached() throws SodiumException {
        String message = "This should get encrypted";

        // Generate a keypair
        KeyPair keyPair = cryptoBoxLazy.cryptoBoxKeypair();

        // Generate a shared key which can be used
        // to encrypt and decrypt data
        String sharedKey = cryptoBoxLazy.cryptoBoxBeforeNm(keyPair);

        byte[] nonce2 = lazySodium.nonce(Box.NONCEBYTES);

        // Use the detached functions
        DetachedEncrypt encDet = cryptoBoxLazy.cryptoBoxDetachedAfterNm(message, nonce2, sharedKey);
        DetachedDecrypt decryptDet = cryptoBoxLazy.cryptoBoxOpenDetachedAfterNm(encDet, nonce2, sharedKey);

        assertEquals(message, lazySodium.str(decryptDet.getMessage()));
    }

    @Test
    public void sealMessage() throws SodiumException {
        String message = "This should get encrypted";

        // Generate the keypair
        KeyPair keyPair = cryptoBoxLazy.cryptoBoxKeypair();

        // Encrypt the message
        String encrypted = cryptoBoxLazy.cryptoBoxSealEasy(message, keyPair.getPublicKey());

        // Now we can decrypt the encrypted message.
        String decryptedMessage = cryptoBoxLazy.cryptoBoxSealOpenEasy(encrypted, keyPair);

        // Public-private key encryption complete!
        assertEquals(message, decryptedMessage);
    }
}
