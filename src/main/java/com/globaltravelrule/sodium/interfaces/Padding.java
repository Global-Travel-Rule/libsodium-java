/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.interfaces;


import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface Padding {

    interface Native {

        /**
         * Adds extra padding to a buffer {@code buf} whose
         * original size is {@code unpaddedBufLen} in order
         * to extend its total length to a multiple of {@code blocksize}.
         * @param paddedBuffLen New length of buffer.
         * @param buf The buffer byte array.
         * @param unpaddedBufLen The length of {@code buf} with no padding.
         * @param blockSize Block size.
         * @param maxBufLen The absolute maximum you want this buffer length
         *                  to be.
         * @return False if the padded buffer length would exceed {@code maxBufLen}.
         */
        boolean sodiumPad(IntByReference paddedBuffLen, Pointer buf, int unpaddedBufLen, int blockSize, int maxBufLen);

        /**
         * Computes the original, unpadded length of a message previously padded using
         * {@link #sodiumPad(IntByReference, Pointer, int, int, int)}. The original length is put into
         * {@code unpaddedBufLen}.
         * @param unpaddedBufLen This will be populated with the unpadded buffer length.
         * @param buf The buffer.
         * @param paddedBufLen The padded buffer size.
         * @param blockSize The block size.
         * @return True if the buffer was unpadded.
         */
        boolean sodiumUnpad(IntByReference unpaddedBufLen, Pointer buf, int paddedBufLen, int blockSize);
    }

    interface Lazy {

    }


}
