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
package com.stagecents.hr.domain;

/**
 * Represents the frequency by which earnings are computed, e.g. per week, per
 * month.
 * 
 * @author Blair Caple
 */
public enum Frequency {
    FLOAT("Float"), PERFORMANCE("per Performance"), WEEKLY("Weekly"), BIWEEKLY(
	    "Biweekly"), SEMIMONTHLY("Semi-Monthly"), MONTHLY("Monthly"), QUARTERLY(
	    "Quarterly"), TEN_PER_YEAR("Ten per Year"), THIRTEEN(
	    "Thirteen per Year");

    private String meaning;

    private Frequency(String meaning) {
	this.meaning = meaning;
    }

    String getMeaning() {
	return meaning;
    }
}
