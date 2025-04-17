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
import com.globaltravelrule.sodium.interfaces.DiffieHellman;
import com.globaltravelrule.sodium.interfaces.SecretBox;
import com.globaltravelrule.sodium.utils.Key;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DiffieHellmanTest extends BaseTest {

    private String clientSecretKey = "CLIENT_TOP_SECRET_KEY_1234567890";
    private String serverSecretKey = "SERVER_TOP_SECRET_KEY_1234567890";


    @Test
    public void create() throws SodiumException {
        DiffieHellman.Lazy dh = lazySodium;
        SecretBox.Lazy box = lazySodium;

        Key secretKeyC = Key.fromPlainString(clientSecretKey);
        Key publicKeyC = dh.cryptoScalarMultBase(secretKeyC);

        Key secretKeyS = Key.fromPlainString(serverSecretKey);
        Key publicKeyS = dh.cryptoScalarMultBase(secretKeyS);

        // -----
        // ON THE CLIENT
        // -----

        // Compute a shared key for sending from client
        // to server.
        Key sharedKey = dh.cryptoScalarMult(secretKeyC, publicKeyS);

        String message = "Hello";
        byte[] nonce = new byte[Box.NONCEBYTES];
        String encrypted = box.cryptoSecretBoxEasy(message, nonce, sharedKey);

        // Send 'encrypted' to server...


        // -----
        // ON THE SERVER
        // -----

        // Compute the shared key for receiving server messages from client
        Key sharedKeyServer = dh.cryptoScalarMult(secretKeyS, publicKeyC);
        String decrypted = box.cryptoSecretBoxOpenEasy(encrypted, nonce, sharedKeyServer);

        // 'decrypted' == Hello

        assertEquals(message, decrypted);
    }
}
