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

import org.joda.time.LocalDate;

import com.stagecents.hr.domain.Unit;

/**
 * Represents a fixed regular pay multiplier to be used in formulas.
 * 
 * @author Blair Caple
 */
public class MultiplierValue extends InputValue {

    public MultiplierValue() {
	super();
    }

    public MultiplierValue(int sequence, float defaultValue,
	    LocalDate startDate, LocalDate endDate, boolean userEnterable) {
	this(Type.MULTIPLE, null, sequence, "Multiplier", Unit.NUMBER,
		defaultValue, startDate, endDate, userEnterable);
    }

    protected MultiplierValue(Type type, ElementType elementType, int sequence,
	    String name, Unit uom, float defaultValue, LocalDate startDate,
	    LocalDate endDate, boolean userEnterable) {
	super(type, elementType, sequence, name, uom, defaultValue, startDate,
		endDate, userEnterable);
    }

}
