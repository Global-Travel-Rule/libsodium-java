/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;

import com.globaltravelrule.sodium.LazySodium;


public class Detached {

    byte[] mac;

    public Detached(byte[] mac) {
        this.mac = mac;
    }

    public byte[] getMac() {
        return mac;
    }

    public String getMacString() {
        return LazySodium.toHex(getMac());
    }

}
