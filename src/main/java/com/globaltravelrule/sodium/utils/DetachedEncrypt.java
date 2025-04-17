/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;

import com.globaltravelrule.sodium.LazySodium;


public class DetachedEncrypt extends Detached {

    byte[] cipher;

    public DetachedEncrypt(byte[] cipher, byte[] mac) {
        super(mac);
        this.cipher = cipher;
    }

    public byte[] getCipher() {
        return cipher;
    }

    public String getCipherString() {
        return LazySodium.toHex(getCipher());
    }


}
