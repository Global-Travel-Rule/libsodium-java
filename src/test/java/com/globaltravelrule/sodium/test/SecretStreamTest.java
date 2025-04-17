/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.interfaces.SecretStream;
import com.globaltravelrule.sodium.utils.Key;
import org.junit.Assert;
import org.junit.Test;

public class SecretStreamTest extends BaseTest {

    private String message1 = "Arbitrary data to encrypt";
    private String message2 = "split into";
    private String message3 = "three messages";

    @Test
    public void test1() throws SodiumException {
        Key key = lazySodium.cryptoSecretStreamKeygen();

        byte[] header = lazySodium.randomBytesBuf(SecretStream.HEADERBYTES);

        // Start the encryption
        SecretStream.State state = lazySodium.cryptoSecretStreamInitPush(header, key);

        String c1 = lazySodium.cryptoSecretStreamPush(state, message1, SecretStream.TAG_MESSAGE);
        String c2 = lazySodium.cryptoSecretStreamPush(state, message2, SecretStream.TAG_MESSAGE);
        String c3 = lazySodium.cryptoSecretStreamPush(state, message3, SecretStream.TAG_FINAL);

        // Start the decryption
        byte[] tag = new byte[1];

        SecretStream.State state2 = lazySodium.cryptoSecretStreamInitPull(header, key);

        String decryptedMessage = lazySodium.cryptoSecretStreamPull(state2, c1, tag);
        String decryptedMessage2 = lazySodium.cryptoSecretStreamPull(state2, c2, tag);
        String decryptedMessage3 = lazySodium.cryptoSecretStreamPull(state2, c3, tag);

        if (tag[0] == SecretStream.XCHACHA20POLY1305_TAG_FINAL) {
            Assert.assertTrue(
                    decryptedMessage.equals(message1) &&
                            decryptedMessage2.equals(message2) &&
                            decryptedMessage3.equals(message3)
            );
        }
    }
}
