/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;

import com.globaltravelrule.sodium.Sodium;
import com.globaltravelrule.sodium.interfaces.MessageEncoder;

public class Base64MessageEncoder implements MessageEncoder {

    @Override
    public String encode(byte[] cipher) {
        return Sodium.base64Facade.encode(cipher);
    }

    @Override
    public byte[] decode(String cipherText) {
        return Sodium.base64Facade.decode(cipherText);
    }
}
