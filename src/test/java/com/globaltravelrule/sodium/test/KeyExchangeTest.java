/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.interfaces.KeyExchange;
import com.globaltravelrule.sodium.utils.KeyPair;
import com.globaltravelrule.sodium.utils.SessionPair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KeyExchangeTest extends BaseTest {


    @Test
    public void generateKeyPair() {
        KeyPair keys = lazySodium.cryptoKxKeypair();
        assertNotNull(keys);
    }

    @Test
    public void generateDeterministicPublicKeyPair() {
        byte[] seed = new byte[KeyExchange.SEEDBYTES];
        KeyPair keys = lazySodium.cryptoKxKeypair(seed);
        KeyPair keys2 = lazySodium.cryptoKxKeypair(seed);

        assertEquals(keys.getPublicKey().getAsHexString(), keys2.getPublicKey().getAsHexString());
    }

    @Test
    public void generateDeterministicSecretKeyPair() {
        byte[] seed = new byte[KeyExchange.SEEDBYTES];
        KeyPair keys = lazySodium.cryptoKxKeypair(seed);
        KeyPair keys2 = lazySodium.cryptoKxKeypair(seed);

        assertEquals(keys.getSecretKey().getAsHexString(), keys2.getSecretKey().getAsHexString());
    }


    @Test
    public void generateSessionPair() throws SodiumException {
        // Generate the client's keypair
        KeyPair clientKeys = lazySodium.cryptoKxKeypair();

        // Generate the server keypair
        KeyPair serverKeys = lazySodium.cryptoKxKeypair();

        SessionPair clientSession = lazySodium.cryptoKxClientSessionKeys(clientKeys, serverKeys);
        SessionPair serverSession = lazySodium.cryptoKxServerSessionKeys(serverKeys, clientKeys);

        // You can now use the secret and public keys of the client and the server
        // to encrypt and decrypt messages to one another.
        // lazySodium.cryptoSecretBoxEasy( ... );

        // The Rx of the client should equal the Tx of the server
        assertEquals(clientSession.getRxString(), serverSession.getTxString());
    }

}
