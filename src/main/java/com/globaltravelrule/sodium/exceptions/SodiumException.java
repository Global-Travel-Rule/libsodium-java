/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */
package com.globaltravelrule.sodium.exceptions;

public class SodiumException extends Exception {

    public SodiumException(String message) {
        super(message);
    }

    public SodiumException(String message, Throwable cause) {
        super(message, cause);
    }

    public SodiumException(Throwable cause) {
        super(cause);
    }

}
