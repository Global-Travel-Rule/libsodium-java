/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;

import com.globaltravelrule.sodium.LazySodiumJava;
import com.globaltravelrule.sodium.SodiumJava;
import com.globaltravelrule.sodium.utils.LibraryLoader;
import org.junit.Before;

public class BaseTest {

    public static LazySodiumJava lazySodium;

    @Before
    public void doBeforeEverything() {
        lazySodium = new LazySodiumJava(new SodiumJava(LibraryLoader.Mode.BUNDLED_ONLY));
    }

}
