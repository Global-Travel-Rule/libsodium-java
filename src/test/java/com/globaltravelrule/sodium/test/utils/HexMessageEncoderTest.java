/*
 * Copyright (c)  2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: $date $time
 */

package com.globaltravelrule.sodium.test.utils;

import com.globaltravelrule.sodium.test.BaseTest;
import com.globaltravelrule.sodium.utils.HexMessageEncoder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HexMessageEncoderTest extends BaseTest {

    @Test
    public void decodeEqualsEncode() {
        HexMessageEncoder encoder = new HexMessageEncoder();

        String cipherText = "612D6865782D656E636F6465642D737472696E67";
        byte[] cipher = encoder.decode(cipherText);

        assertEquals(cipherText, encoder.encode(cipher));
    }
}
