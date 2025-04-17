/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.interfaces.Hash;
import org.junit.Test;

import static org.junit.Assert.*;

public class HashTest extends BaseTest {


    private final String M1 = "With great power ";
    private final String M2 = "comes great responsibility";
    private final String MESSAGE = M1 + M2;

    @Test
    public void sha256Compare() throws SodiumException {
        String hashed1 = lazySodium.cryptoHashSha256(MESSAGE);
        String hashed2 = lazySodium.cryptoHashSha256(MESSAGE);
        assertNotSame(hashed1, hashed2);
    }

    @Test
    public void sha512Compare() throws SodiumException {
        String hash1 = lazySodium.cryptoHashSha512(MESSAGE);
        String hash2 = lazySodium.cryptoHashSha512(MESSAGE);
        assertNotSame(hash1, hash2);
    }

    @Test
    public void sha512IsLonger() throws SodiumException {
        String hash1 = lazySodium.cryptoHashSha256(MESSAGE);
        String hash2 = lazySodium.cryptoHashSha512(MESSAGE);
        assertTrue(hash1.length() < hash2.length());
    }

    @Test
    public void multipartSha256() throws SodiumException {
        Hash.State256 state = new Hash.State256.ByReference();
        lazySodium.cryptoHashSha256Init(state);

        lazySodium.cryptoHashSha256Update(state, M1);
        lazySodium.cryptoHashSha256Update(state, M2);
        lazySodium.cryptoHashSha256Update(state, "more text to be hashed");

        String hash = lazySodium.cryptoHashSha256Final(state);
        assertNotNull(hash);
    }

    @Test
    public void multipartSha512() throws SodiumException {
        Hash.State512 state = new Hash.State512.ByReference();
        lazySodium.cryptoHashSha512Init(state);

        lazySodium.cryptoHashSha512Update(state, M1);
        lazySodium.cryptoHashSha512Update(state, M2);
        lazySodium.cryptoHashSha512Update(state, "more text to be hashed");

        String hash = lazySodium.cryptoHashSha512Final(state);

        assertNotNull(hash);
    }
}
