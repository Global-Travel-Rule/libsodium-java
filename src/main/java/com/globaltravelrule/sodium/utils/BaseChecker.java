/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;

import com.sun.jna.NativeLong;

public class BaseChecker {

    public static boolean isBetween(long num, long min, long max) {
        return min <= num && num <= max;
    }

    public static boolean isBetween(NativeLong num, NativeLong min, NativeLong max) {
        long number = num.longValue();
        return min.longValue() <= number && number <= max.longValue();
    }

    public static boolean correctLen(long num, long len) {
        return num == len;
    }

}
