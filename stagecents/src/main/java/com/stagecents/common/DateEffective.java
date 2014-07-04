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

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public interface DateEffective {

    /**
     * Returns true if the implementing instance is effective on the
     * <code>DateTime</code> representation of the specified date. More
     * specifically, the method will true if and only if the specified date is
     * greater than or equal to the effective start date and less than or equal
     * to the effective end date.
     * 
     * @param effectiveDate The date to test effectiveness.
     * @return True if and only if the specified <code>DateTime</code>
     *         representation is contained within the instance's effective start
     *         and end dates.
     */
    public boolean isEffective(DateTime effectiveDate);

    /**
     * Returns true if the implementing instance is effective on the
     * <code>LocalDate</code> representation of the specified date. More
     * specifically, the method will true if and only if the specified date is
     * greater than or equal to the effective start date and less than or equal
     * to the effective end date.
     * 
     * @param effectiveDate The date to test effectiveness.
     * @return True if and only if the specified <code>LocalDate</code>
     *         representation is contained within the instance's effective start
     *         and end dates.
     * @see http://www.joda.org/joda-time/key_partial.html
     */
    public boolean isEffective(LocalDate effectiveDate);
}
