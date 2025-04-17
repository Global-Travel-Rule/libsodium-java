/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;

import com.globaltravelrule.sodium.LazySodium;

public class SessionPair {
    private byte[] rx;
    private byte[] tx;

    public SessionPair(byte[] rx, byte[] tx) {
        this.rx = rx;
        this.tx = tx;
    }

    public SessionPair(String rx, String tx) {
        this.rx = LazySodium.toBin(rx);
        this.tx =  LazySodium.toBin(tx);
    }

    public byte[] getRx() {
        return rx;
    }

    public byte[] getTx() {
        return tx;
    }

    public String getRxString() {
        return LazySodium.toHex(rx);
    }

    public String getTxString() {
        return LazySodium.toHex(tx);
    }
}
