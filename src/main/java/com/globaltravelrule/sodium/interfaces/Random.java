/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.interfaces;



public interface Random {

    /**
     * Return a unsigned int byte 0 and 0xffffffff included.
     * @return A random byte.
     */
    long randomBytesRandom();

    /**
     * Returns an unpredictable value between 0 and upperBound (excluded).
     * Unlike randombytes_random() % upper_bound, it guarantees a uniform distribution
     * of the possible output values even when upper_bound is not a power of 2. Note
     * that an upper_bound less than 2 leaves only a single element to be chosen, namely 0.
     * @param upperBound
     * @return A uniformly random unsigned int.
     */
    long randomBytesUniform(int upperBound);

    /**
     * Get a random number of bytes.
     * @param size The length of the byte array to return.
     * @return Random byte array.
     */
    byte[] randomBytesBuf(int size);

    /**
     * Get deterministically random bytes given a seed.
     * @param size Size of byte array to return.
     * @param seed Seed to provide.
     * @return Deterministically random byte array.
     */
    byte[] randomBytesDeterministic(int size, byte[] seed);


    /**
     * Get a random number of bytes to use in a nonce.
     * @param size The size of the byte array to return.
     * @return Random nonce array.
     * @see #randomBytesBuf(int)
     */
    byte[] nonce(int size);
}
