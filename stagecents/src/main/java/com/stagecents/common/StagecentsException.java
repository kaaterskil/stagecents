/**
 * Copyright (c) 2009-2014 Kaaterskil Management, LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.stagecents.common;

/**
 * Base exception of all StageCents related exceptions.
 * 
 * @author Blair Caple
 */
public class StagecentsException extends RuntimeException {

    /**
     * Creates a new exception with no message.
     */
    public StagecentsException() {
	super();
    }

    /**
     * Creates a new exception with the given detail <code>message</code> and
     * <code>cause</code>.
     * 
     * @param message The detail message describing the exception.
     * @param cause The underlying cause of the exception.
     */
    public StagecentsException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * Creates a new exception with the given detail <code>message</code>.
     * 
     * @param message The detail message describing the exception.
     */
    public StagecentsException(String message) {
	super(message);
    }

}
