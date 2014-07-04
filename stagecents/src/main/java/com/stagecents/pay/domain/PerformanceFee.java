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
package com.stagecents.pay.domain;

import java.util.Iterator;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

/**
 * Represents a pre-defined ElementType for fee-based performance compensation.
 * 
 * @author Blair Caple
 */
public class PerformanceFee extends ElementType {

    public PerformanceFee(String name, String description, LocalDate startDate,
	    LocalDate endDate) {
	super(name, description, Type.PERF_FEE, Classification.REG_EARNINGS,
		ProcessingType.RECURRING, startDate, endDate);

	// Create input value for performance fee
	addInputValue(new PayMoneyValue(0, 0, startDate, endDate, true));
    }

    public float process(Interval interval) {
	PayMoneyValue fee = getFee(interval);
	if (fee != null) {
	    return fee.getDefaultValue();
	}
	return 0F;
    }

    private PayMoneyValue getFee(Interval interval) {
	Iterator<InputValue> iter = inputValues.iterator();
	while (iter.hasNext()) {
	    InputValue each = iter.next();
	    if (each instanceof PayMoneyValue) {
		PayMoneyValue fee = (PayMoneyValue) each;
		if (fee.getEffectiveDateRange().getOverlap(interval) != null) {
		    return fee;
		}
	    }
	}
	return null;
    }
}
