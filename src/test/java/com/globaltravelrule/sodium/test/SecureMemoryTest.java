/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.sun.jna.Pointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SecureMemoryTest extends BaseTest {


    @Test
    public void memZero() {
        byte[] b = new byte[]{4, 2, 2, 1};
        boolean res = lazySodium.sodiumMemZero(b, b.length);
        assertTrue(isZero(b));
    }

    @Test
    public void mLock() {
        byte[] b = new byte[]{4, 5, 2, 1};
        boolean res = lazySodium.sodiumMLock(b, b.length);
        boolean res2 = lazySodium.sodiumMUnlock(b, b.length);
        assertTrue(isZero(b));
    }

    @Test
    public void malloc() {
        int size = 10;

        Pointer ptr = lazySodium.sodiumMalloc(size);

        byte[] arr = ptr.getByteArray(0, size);

        assertEquals(size, arr.length);
    }

    @Test
    public void free() {
        int size = 10;
        Pointer ptr = lazySodium.sodiumMalloc(size);
        lazySodium.sodiumFree(ptr);
        // If this test reached this comment it didn't segfault
        // so it passes
        assertTrue(true);
    }

    private boolean isZero(byte[] arr) {
        boolean allZeroes = true;
        for (byte b : arr) {
            if (b != 0) {
                allZeroes = false;
            }
        }
        return allZeroes;
    }


}
