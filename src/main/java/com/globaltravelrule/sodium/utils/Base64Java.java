/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;

import java.util.Base64;

public class Base64Java implements Base64Facade {

    @Override
    public String encode(byte[] cipher) {
        return Base64.getEncoder().encodeToString(cipher);
    }

    @Override
    public byte[] decode(String cipherText) {
        return Base64.getDecoder().decode(cipherText);
    }
}
