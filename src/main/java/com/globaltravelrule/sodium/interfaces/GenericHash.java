/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.interfaces;


import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.utils.Key;
import com.sun.jna.NativeLong;

public interface GenericHash {

    int
        BLAKE2B_BYTES = 32,
        BLAKE2B_BYTES_MIN = 16,
        BLAKE2B_BYTES_MAX = 64,
        BLAKE2B_KEYBYTES = 32,
        BLAKE2B_KEYBYTES_MIN = 16,
        BLAKE2B_KEYBYTES_MAX = 64,
        BLAKE2B_SALTBYTES = 16,
        BLAKE2B_PERSONALBYTES = 16,

        BYTES = BLAKE2B_BYTES,
        KEYBYTES = BLAKE2B_KEYBYTES,

        BYTES_MAX = BLAKE2B_BYTES_MAX,
        BYTES_MIN = BLAKE2B_BYTES_MIN,

        // KEYBYTES_MIN is highly error prone
        // and fails 1/10 times via our tests. Use
        // with caution, or just use KEYBYTES or KEYBYTES_MAX
        // KEYBYTES_MIN = BLAKE2B_KEYBYTES_MIN

        KEYBYTES_MAX = BLAKE2B_KEYBYTES_MAX;


    interface Native {

        /**
         * Generate a key. Store the key in {@code k}.
         * @param k A place to store the generated key
         *          of size {@link #KEYBYTES}. Though,
         *          it may be between {@link #KEYBYTES}
         *          and {@link #KEYBYTES_MAX}.
         */
        void cryptoGenericHashKeygen(byte[] k);

        /**
         * Hash a byte array.
         * @param out A place to store the resulting byte array.
         *            You may choose the output size, however the minimum
         *            recommended output size is {@link #BYTES}.
         *            You may also specify a value
         *            between {@link #BYTES_MIN} and {@link #BYTES_MAX}.
         * @param outLen Size of out.
         * @param in The text to hash.
         * @param inLen The size of in.
         * @param key The key generated by {@link #cryptoGenericHashKeygen(byte[])}.
         * @param keyLen The length of the key.
         * @return True if successfully hashed.
         */
        boolean cryptoGenericHash(
                byte[] out, int outLen,
                byte[] in, long inLen,
                byte[] key, int keyLen
        );

        /**
         * Hash a byte array without a key.
         * @param out A place to store the resulting byte array.
         *            You may choose the output size, however the minimum
         *            recommended output size is {@link #BYTES}.
         *            You may also specify a value
         *            between {@link #BYTES_MIN} and {@link #BYTES_MAX}.
         * @param outLen Size of out.
         * @param in The text to hash.
         * @param inLen The size of in.
         * @return True if successfully hashed.
         * @see #cryptoGenericHash(byte[], int, byte[], long, byte[], int)
         */
        boolean cryptoGenericHash(
                byte[] out, int outLen,
                byte[] in, long inLen
        );


        /**
         * Hash multiple parts of a message.
         * @param state The state which holds the key
         *              in memory for further hashing.
         * @param key The key generated by {@link #cryptoGenericHashKeygen(byte[])}.
         * @param keyLength Length of the key.
         * @param outLen The size of the hash array. Please
         *               see the param {@code out} in
         *               {@link #cryptoGenericHash(byte[], int, byte[], long, byte[], int)}
         *               for more information.
         *
         * @return True if initialised.
         */
        boolean cryptoGenericHashInit(byte[] state,
                                   byte[] key,
                                   int keyLength,
                                   int outLen);

        /**
         * Hash multiple parts of a message without a key
         * @param state The state which holds the current state
         *              in memory for further hashing.
         * @param outLen The size of the hash array. Please
         *               see the param {@code out} in
         *               {@link #cryptoGenericHash(byte[], int, byte[], long, byte[], int)}
         *               for more information.
         *
         * @return True if initialised.
         */
        boolean cryptoGenericHashInit(byte[] state, int outLen);

        /**
         * Update a multi-part hashing with another part.
         * @param state The state.
         * @param in Another hash part.
         * @param inLen The length if the hash part.
         * @return True if this part of the message was hashed.
         */
        boolean cryptoGenericHashUpdate(byte[] state,
                                     byte[] in,
                                     long inLen);

        /**
         * Now that the hash has finalised, the hash can
         * be put into {@code out}.
         * @param state The state.
         * @param out The final hash.
         * @param outLen The length of the hash.
         * @return True if hashed.
         */
        boolean cryptoGenericHashFinal(byte[] state, byte[] out, int outLen);

        int cryptoGenericHashStateBytes();



    }

    interface Lazy {

        /**
         * Generate a hashing key.
         * @return A hashing key.
         */
        Key cryptoGenericHashKeygen();

        /**
         * Generate a hashing key with a size.
         * @param size The size of the hashing key between
         *             {@link #KEYBYTES} and {@link #KEYBYTES_MAX}.
         * @return A hashing key.
         */
        Key cryptoGenericHashKeygen(int size) throws SodiumException;

        /**
         * Hash a string without a key.
         * @param in The string to hash.
         * @return The hashed string.
         */
        String cryptoGenericHash(String in) throws SodiumException;

        /**
         * Hash a string with a key, so later on you
         * can verify the hashed string with the key.
         * If you're hashing a password please see {@link PwHash.Lazy#cryptoPwHashStr(String, long, NativeLong)}}
         * instead.
         * @param in The string to hash.
         * @param key Can be null.
         * @return A hashed string.
         */
        String cryptoGenericHash(String in, Key key) throws SodiumException;

        /**
         * Initialise a multi-part hashing operation.
         * @param state The state which holds the key and operation.
         * @param key The key as generated by {@link #cryptoGenericHashKeygen()}.
         * @param outLen The size of the final hash.
         * @return True if initialised.
         */
        boolean cryptoGenericHashInit(byte[] state,
                                      Key key,
                                      int outLen);

        /**
         * Hash a part of a multi-part hash.
         * @param state State as put into {@link #cryptoGenericHashInit(byte[], Key, int)}.
         * @param in A part of a string to hash.
         * @return True if hashed successfully.
         */
        boolean cryptoGenericHashUpdate(byte[] state, String in) throws SodiumException;

        /**
         * Finalise the hashing operation.
         * @param state State as put into {@link #cryptoGenericHashInit(byte[], Key, int)}.
         * @param outLen The size of the final hash.
         * @return The final hash.
         */
        String cryptoGenericHashFinal(byte[] state, int outLen) throws SodiumException;



    }

}
