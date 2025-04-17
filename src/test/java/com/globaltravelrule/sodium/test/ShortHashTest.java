/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.utils.Key;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ShortHashTest extends BaseTest {

    @Test
    public void hash() throws SodiumException {
        String hashThis = "This should get hashed";

        Key key = lazySodium.cryptoShortHashKeygen();
        String hash = lazySodium.cryptoShortHash(hashThis, key);
        assertNotNull(hash);
    }
}
