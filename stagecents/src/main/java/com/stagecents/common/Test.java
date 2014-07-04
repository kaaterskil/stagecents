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
import org.joda.time.Interval;
import org.joda.time.Months;

public class Test {

    public static void main(String[] args) {
	DateTime start = new DateTime(2013, 1, 3, 12, 0);
	DateTime end = new DateTime(2014, 4, 23, 13, 0);
	Interval arg = new Interval(start, end);
	
	int yrs = arg.getEnd().getYear() - arg.getStart().getYear() + 1;
	

	System.out.println("Start: " + arg.getStart().toString("YYYY-MM-dd"));
	System.out.println("End: " + arg.getEnd().toString("YYYY-MM-dd"));
	System.out.println("Annual cycles: " + yrs);
    }
}
