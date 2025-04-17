/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;


public class KeyPair {
    private final Key secretKey;
    private final Key publicKey;

    public KeyPair(Key publicKey, Key secretKey) {
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    public Key getSecretKey() {
        return secretKey;
    }

    public Key getPublicKey() {
        return publicKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyPair)) return false;
        KeyPair other = (KeyPair) obj;
        return other.getSecretKey().equals(getSecretKey())
                && other.getPublicKey().equals(getPublicKey());
    }
}
