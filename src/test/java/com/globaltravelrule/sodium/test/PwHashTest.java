/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.LazySodium;
import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.interfaces.PwHash;
import com.globaltravelrule.sodium.interfaces.Scrypt;
import com.sun.jna.NativeLong;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PwHashTest extends BaseTest {

    private final String PASSWORD = "Password123456!!!!@@";
    private PwHash.Lazy pwHashLazy;

    @Before
    public void before() {
        pwHashLazy = lazySodium;
    }

    @Test
    public void scryptHash() throws SodiumException {
        byte[] salt = new byte[LazySodium.longToInt(Scrypt.SCRYPTSALSA208SHA256_SALT_BYTES)];
        String scryptHash = lazySodium.cryptoPwHashScryptSalsa208Sha256(
                PASSWORD,
                300L, // This can be anything up to Constants.SIZE_MAX
                salt,
                Scrypt.SCRYPTSALSA208SHA256_OPSLIMIT_MIN,
                Scrypt.SCRYPTSALSA208SHA256_MEMLIMIT_MIN
        );

        String hash = lazySodium.cryptoPwHashScryptSalsa208Sha256Str(
                PASSWORD,
                Scrypt.SCRYPTSALSA208SHA256_OPSLIMIT_MIN,
                Scrypt.SCRYPTSALSA208SHA256_MEMLIMIT_MIN
        );

        boolean isCorrect = lazySodium.cryptoPwHashScryptSalsa208Sha256StrVerify(hash, PASSWORD);


        assertTrue("Minimum hashing failed.", isCorrect);
    }

    @Test
    public void nativeHash() throws SodiumException {
        String output = pwHashLazy.cryptoPwHash(
                PASSWORD,
                PwHash.BYTES_MIN,
                lazySodium.randomBytesBuf(PwHash.SALTBYTES),
                5L,
                new NativeLong(8192 * 2),
                PwHash.Alg.PWHASH_ALG_ARGON2ID13
        );

        assertNotNull("Native hashing failed.", output);
    }

    @Test
    public void strMin() throws SodiumException {
        String hash = pwHashLazy.cryptoPwHashStr(
                PASSWORD,
                3,
                PwHash.MEMLIMIT_MIN
        );

        boolean isCorrect = pwHashLazy.cryptoPwHashStrVerify(hash, PASSWORD);

        assertTrue("Minimum hashing failed.", isCorrect);
    }


    // We don't test for this as it's pretty demanding and
    // will fail on most machines
    public void cryptoPwHashStrTestSensitive() {
    }

}
