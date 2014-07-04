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

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.stagecents.common.DateEffective;
import com.stagecents.common.EffectiveDateInterval;

/**
 * Represents a tax rate for a tax rule for a specified date range.
 * 
 * @author Blair Caple
 */
public class TaxRule implements DateEffective {

    private TaxType taxType;
    private String name;
    private String description;
    private EffectiveDateInterval effectiveDateRange;
    private float rate;
    private float floor;
    private float ceiling;

    TaxRule() {
    }

    public TaxRule(TaxType taxType, String name, String description,
	    float rate, float floor, float ceiling, LocalDate startDate,
	    LocalDate endDate) {
	this.taxType = taxType;
	this.name = name;
	this.description = description;
	this.rate = rate;
	this.floor = floor;
	this.ceiling = ceiling;
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
    }

    public void setTaxType(TaxType taxType) {
	this.taxType = taxType;
    }

    public DateTime getStartDate() {
	return effectiveDateRange.getStartDate();
    }

    @Override
    public boolean isEffective(DateTime effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }

    @Override
    public boolean isEffective(LocalDate effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }
}
