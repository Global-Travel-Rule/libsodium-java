/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;

import com.globaltravelrule.sodium.LazySodium;
import com.globaltravelrule.sodium.interfaces.MessageEncoder;

public class HexMessageEncoder implements MessageEncoder {

    @Override
    public String encode(byte[] cipher) {
        return LazySodium.toHex(cipher);
    }

    @Override
    public byte[] decode(String cipherText) {
        return LazySodium.toBin(cipherText);
    }
}
