/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;

/**
 * Indicates a failure to load the required library.
 */
public class LibraryLoadingException extends RuntimeException {

    public LibraryLoadingException(String message) {
        super(message);
    }

    public LibraryLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
