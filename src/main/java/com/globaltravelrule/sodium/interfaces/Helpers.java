/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.interfaces;


public interface Helpers {

    interface Native {
        int sodiumInit();
    }

    interface Lazy {

        /**
         * Binary to hexadecimal. This method does not null terminate strings.
         * @param bin The binary bytes you want to convert to a string.
         * @return A hexadecimal string solely made up of the characters 0123456789ABCDEF.
         */
        String sodiumBin2Hex(byte[] bin);

        /**
         * Hexadecimal to binary. Does not null terminate the binary
         * array.
         * @param hex Hexadecimal string (a string that's
         *            made up of the characters 0123456789ABCDEF)
         *            to convert to a binary array.
         * @return Binary byte array.
         */
        byte[] sodiumHex2Bin(String hex);


    }


}
