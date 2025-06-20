/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.interfaces;

import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.utils.BaseChecker;
import com.globaltravelrule.sodium.utils.Key;

public interface KeyDerivation {

    int MASTER_KEY_BYTES = 32,
        CONTEXT_BYTES = 8,
        BLAKE2B_BYTES_MIN = 16,
        BLAKE2B_BYTES_MAX = 64,
        BYTES_MIN = BLAKE2B_BYTES_MIN,
        BYTES_MAX = BLAKE2B_BYTES_MAX;

    interface Native {

        /**
         * Creates a master key.
         * @param masterKey The byte array to populate. Should be
         *                  {@link KeyDerivation#MASTER_KEY_BYTES}.
         */
        void cryptoKdfKeygen(byte[] masterKey);

        /**
         * Derive a subkey from a master key.
         * @param subKey The subkey.
         * @param subKeyLen The length of the subkey. Should be
         *                  from {@link KeyDerivation#BYTES_MIN} to {@link KeyDerivation#BYTES_MAX}.
         * @param subKeyId ID of subkey.
         * @param context The context of the subkey. Must be {@link KeyDerivation#CONTEXT_BYTES}.
         * @param masterKey The generated master key from {@link #cryptoKdfKeygen(byte[])}. Must be {@link KeyDerivation#MASTER_KEY_BYTES}.
         * @return 0 on success, -1 otherwise.
         */
        int cryptoKdfDeriveFromKey(byte[] subKey, int subKeyLen, long subKeyId, byte[] context, byte[] masterKey);
    }

    interface Lazy {

        /**
         * Auto generates a master key and returns
         * it in string format.
         * The reason why this does not return a string via the normal 'masterKey.getBytes()'
         * is because the resulting string is mangled.
         * @return A master Key.
         */
        Key cryptoKdfKeygen();


        /**
         * Derive a subkey from a master key.
         * @param lengthOfSubKey The length of the subkey. Should be
         *                       from {@link KeyDerivation#BYTES_MIN} to {@link KeyDerivation#BYTES_MAX}.
         * @param subKeyId The ID of the subkey.
         * @param context The context of the subkey. Must be {@link KeyDerivation#CONTEXT_BYTES}.
         * @param masterKey The generated master key from {@link #cryptoKdfKeygen()}.
         * @return A subkey that's gone through {@link Helpers.Lazy#sodiumBin2Hex(byte[])}.
         * @throws SodiumException If any of the lengths were not correct.
         */
        Key cryptoKdfDeriveFromKey(int lengthOfSubKey, long subKeyId, String context, Key masterKey) throws SodiumException;

    }

    class Checker extends BaseChecker {

        public static boolean masterKeyIsCorrect(long masterKeyLen) {
            return masterKeyLen == KeyDerivation.MASTER_KEY_BYTES;
        }

        public static boolean subKeyIsCorrect(int lengthOfSubkey) {
            return isBetween(lengthOfSubkey, BYTES_MIN, BYTES_MAX);
        }

        public static boolean contextIsCorrect(int length) {
            return length == KeyDerivation.CONTEXT_BYTES;
        }
    }
}
