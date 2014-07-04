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
 * Represents the day of the week in ISO8601 standard, used by Joda time.
 * 
 * @author Blair Caple
 */
public enum DayOfWeek {
    MONDAY("Monday", 1), TUESDAY("Tuesday", 2), WEDNESDAY("Monday", 3), THURSDAY(
	    "Thursday", 4), FRIDAY("Friday", 5), SATURDAY("Saturday", 6), SUNDAY(
	    "Sunday", 7);
    private String meaning;
    private int isoValue;

    private DayOfWeek(String meaning, int isoValue) {
	this.meaning = meaning;
	this.isoValue = isoValue;
    }

    public String getMeaning() {
	return meaning;
    }

    /**
     * Returns the ISO8601 value of the day of the week, where 1 is Monday and 7
     * is Sunday.
     * 
     * @return The ISO8601 value of the day of the week.
     */
    public int getIsoValue() {
	return isoValue;
    }

}
