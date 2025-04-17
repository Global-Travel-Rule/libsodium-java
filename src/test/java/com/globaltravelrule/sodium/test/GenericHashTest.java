/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.interfaces.GenericHash;
import com.globaltravelrule.sodium.utils.Key;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GenericHashTest extends BaseTest {

    @Test
    public void genKey() {
        Key key = lazySodium.cryptoGenericHashKeygen();
        assertNotNull(key);
    }

    @Test
    public void hash() throws SodiumException {
        String message = "https://globaltravelrule.com";
        Key key = lazySodium.cryptoGenericHashKeygen();
        String hash = lazySodium.cryptoGenericHash(message);
        assertNotNull(hash);
    }

    @Test
    public void hashNoKey() throws SodiumException {
        String message = "https://globaltravelrule.com";
        String hash = lazySodium.cryptoGenericHash(message);
        assertNotNull(hash);
    }


    @Test
    public void hashMultiPartRecommended() throws SodiumException {
        String message = "The sun";
        String message2 = "is shining";

        String hash = hashMultiPart(
                GenericHash.KEYBYTES,
                GenericHash.BYTES,
                message,
                message2
        );


        assertNotNull(hash);
    }


    @Test
    public void hashMultiPartMax() throws SodiumException {
        String message = "Do not go gentle into that good night";
        String message2 = "Old age should burn and rave at close of day";
        String message3 = "Rage, rage against the dying of the light";

        String hash = hashMultiPart(
                GenericHash.KEYBYTES_MAX,
                GenericHash.BYTES_MAX,
                message,
                message2,
                message3
        );

        assertNotNull(hash);
    }


    private String hashMultiPart(int keySize, int hashSize, String... messages) throws SodiumException {

        Key key = lazySodium.cryptoGenericHashKeygen(keySize);
        byte[] state = new byte[lazySodium.cryptoGenericHashStateBytes()];
        lazySodium.cryptoGenericHashInit(state, key, hashSize);

        for (String msg : messages) {
            lazySodium.cryptoGenericHashUpdate(state, msg);
        }

        return lazySodium.cryptoGenericHashFinal(state, hashSize);
    }


}
