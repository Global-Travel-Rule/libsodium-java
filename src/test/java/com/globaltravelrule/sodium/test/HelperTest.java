/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelperTest extends BaseTest {

    @Test
    public void compare() {
        byte[] b1 = new byte[] { 4, 2, 2, 1 };
        byte[] b2 = new byte[] { 4, 2, 2, 1 };

        int r = lazySodium.getSodium().sodium_compare(b1, b2, 4);

        assertEquals(0, r);
    }
}
