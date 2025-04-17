/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.utils;

import java.nio.charset.Charset;


public class DetachedDecrypt extends Detached {

    byte[] message;
    Charset charset;

    public DetachedDecrypt(byte[] message, byte[] mac) {
        super(mac);
        this.message = message;
        this.charset = Charset.forName("UTF-8");
    }

    public DetachedDecrypt(byte[] message, byte[] mac, Charset charset) {
        super(mac);
        this.message = message;
        this.charset = charset;
    }


    public byte[] getMessage() {
        return message;
    }

    public String getMessageString(Charset charset) {
        return new String(getMessage(), charset);
    }

    public String getMessageString() {
        return new String(getMessage(), charset);
    }

}
