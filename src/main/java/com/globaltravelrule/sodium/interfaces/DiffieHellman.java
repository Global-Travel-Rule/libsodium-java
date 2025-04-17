/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.interfaces;


import com.globaltravelrule.sodium.utils.Key;

public interface DiffieHellman {

    int SCALARMULT_CURVE25519_BYTES = 32;
    int SCALARMULT_CURVE25519_SCALARBYTES = 32;

    int SCALARMULT_BYTES = SCALARMULT_CURVE25519_BYTES;
    int SCALARMULT_SCALARBYTES = SCALARMULT_CURVE25519_SCALARBYTES;


    interface Native {

        boolean cryptoScalarMultBase(byte[] publicKey, byte[] secretKey);

        boolean cryptoScalarMult(byte[] shared, byte[] secretKey, byte[] publicKey);

    }


    interface Lazy {

        /**
         * Generate a public key from a private key.
         *
         * @param secretKey Provide the secret key.
         * @return The public key and the provided secret key.
         */
        Key cryptoScalarMultBase(Key secretKey);


        /**
         * Generate a shared key from another user's public key
         * and a secret key.
         *
         * @param publicKey Another user's public key.
         * @param secretKey A secret key.
         * @return Shared secret key.
         */
        Key cryptoScalarMult(Key publicKey, Key secretKey);

    }

}
