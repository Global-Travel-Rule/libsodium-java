/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.interfaces;


import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public interface Hash {

    int SHA256_BYTES = 32,
        SHA512_BYTES = 64,
        BYTES = SHA512_BYTES;

    interface Native {

        boolean cryptoHashSha256(byte[] out, byte[] in, long inLen);

        boolean cryptoHashSha512(byte[] out, byte[] in, long inLen);


        boolean cryptoHashSha256Init(Hash.State256 state);

        boolean cryptoHashSha256Update(Hash.State256 state,
                                                    byte[] in,
                                                    long inLen);

        boolean cryptoHashSha256Final(Hash.State256 state, byte[] out);


        boolean cryptoHashSha512Init(Hash.State512 state);

        boolean cryptoHashSha512Update(Hash.State512 state,
                                       byte[] in,
                                       long inLen);

        boolean cryptoHashSha512Final(Hash.State512 state, byte[] out);

    }

    interface Lazy {

        String cryptoHashSha256(String message) throws SodiumException;

        String cryptoHashSha512(String message) throws SodiumException;

        boolean cryptoHashSha256Init(Hash.State256 state);

        boolean cryptoHashSha256Update(Hash.State256 state, String messagePart);

        String cryptoHashSha256Final(Hash.State256 state) throws SodiumException;

        boolean cryptoHashSha512Init(Hash.State512 state);

        boolean cryptoHashSha512Update(Hash.State512 state, String messagePart);

        String cryptoHashSha512Final(Hash.State512 state) throws SodiumException;

    }


    class State256 extends Structure {

        public static class ByReference extends State256 implements Structure.ByReference { }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("state", "count", "buf");
        }

        public long[] state = new long[8];
        public long count;
        public byte[] buf = new byte[64];

    }

    class State512 extends Structure {

        public static class ByReference extends State512 implements Structure.ByReference { }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("state", "count", "buf");
        }

        public long[] state = new long[8];
        public long[] count = new long[2];
        public byte[] buf = new byte[128];

    }

}
