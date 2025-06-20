/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.SodiumJava;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class SodiumJavaTest {

    @Test
    // This test is ignored for two reasons:
    //  - We cannot assume that libsodium is installed on any machine
    //  - Loading is a no-op if libsodium has already been loaded by another test (say, from resources)
    // It is supposed to work with 'sodium', 'libsodium.so' (platform dependent) and
    // '/usr/lib/x86_64-linux-gnu/libsodium.so'
    @Ignore
    public void canLoadWithSystemLibrary() {
        SodiumJava sodium = new SodiumJava("sodium");
        int initResult = sodium.sodium_init();
        assertNotEquals(-1, initResult);
    }
}
