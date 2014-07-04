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
package com.stagecents.gl.domain;

/**
 * Represents the type type (duration) of accounting period.
 * 
 * @author Blair Caple
 */
public enum PeriodType {
    QUARTER("Quarter", 4), MONTH("Month", 12), MONTH_ADJ("Adjusting Month", 13), FOUR_WEEK(
	    "13 Months", 13), BI_WEEK("26 Periods", 26);
    private String meaning;
    private int numPeriods;

    private PeriodType(String meaning, int numPeriods) {
	this.meaning = meaning;
	this.numPeriods = numPeriods;
    }

    public String getMeaning() {
	return meaning;
    }

    public int getNumPeriods() {
	return numPeriods;
    }
}
