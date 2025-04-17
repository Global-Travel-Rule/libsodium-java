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

public interface ShortHash {

    int SIPHASH24_BYTES = 8,
        SIPHASH24_KEYBYTES = 16,
        SIPHASHX24_BYTES = 16,
        SIPHASHX24_KEYBYTES = 16,

        BYTES = SIPHASH24_BYTES,
        KEYBYTES = SIPHASH24_KEYBYTES;



    interface Native {

        /**
         * Short-input hash some text.
         * @param out The hashed text of size {@link #SIPHASH24_BYTES} or
         *            {@link #SIPHASHX24_BYTES} depending on {@code in} size.
         * @param in The short-input text to hash of size {@link #BYTES} or of size {@link #SIPHASHX24_BYTES}.
         * @param inLen The length of the short-input.
         * @param key The key generated via {@link #cryptoShortHashKeygen(byte[])}.
         * @return true if success, false if fail.
         */
        boolean cryptoShortHash(byte[] out, byte[] in, long inLen, byte[] key);


        /**
         * Output a 64-bit key.
         * @param k The key of size {@link #SIPHASH24_KEYBYTES}.
         */
        void cryptoShortHashKeygen(byte[] k);

    }

    interface Lazy {

        /**
         * Generate a 64-bit key for short-input hashing.
         * @return Key in string format.
         */
        Key cryptoShortHashKeygen();

        /**
         * Hash a short message using a key.
         * @param in The short message to hash.
         * @param key The key generated via {@link #cryptoShortHashKeygen()}.
         * @return Your message hashed of size {@link #BYTES}.
         */
        String cryptoShortHash(String in, Key key) throws SodiumException;


    }


}
