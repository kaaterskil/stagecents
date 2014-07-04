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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.stagecents.common.DateEffective;
import com.stagecents.common.EffectiveDateInterval;
import com.stagecents.hr.domain.Position;
import com.stagecents.hxt.domain.DetailHours;
import com.stagecents.hxt.domain.SummaryHours;
import com.stagecents.hxt.domain.Timecard;

/**
 * Represents a compensation element for each assignment.
 * 
 * @author Blair Caple
 */
public class ElementEntry implements DateEffective {

    private EffectiveDateInterval effectiveDateRange;
    // Bidirectional many-to-one association
    private Position position;
    // Unidirectional many-to-one association
    private ElementLink elementLink;

    // Bidirectional one-to-many-association.
    private Set<ElementEntryValue> entryValues = new HashSet<ElementEntryValue>();

    ElementEntry() {
    }

    public ElementEntry(ElementLink elementLink, LocalDate startDate,
	    LocalDate endDate) {
	this.elementLink = elementLink;
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);

	// Initialize element entry values from the link element.
	Iterator<InputValue> iter = elementLink.getElementType()
		.getInputValues().iterator();
	while (iter.hasNext()) {
	    addElementEntryValue(new ElementEntryValue(this, iter.next(), 0));
	}
    }

    public void setPosition(Position position) {
	this.position = position;
    }

    public void setElementLink(ElementLink elementLink) {
	this.elementLink = elementLink;
    }

    public void addElementEntryValue(ElementEntryValue arg) {
	arg.setElementEntry(this);
	entryValues.add(arg);
    }

    public void removeElementEntryValue(ElementEntryValue arg) {
	arg.setElementEntry(null);
	entryValues.remove(arg);
    }

    public void processHours(Timecard timecard) {
	// Fetch hours
	float hours = 0;
	Iterator<SummaryHours> iter1 = timecard.getSummaryHours().iterator();
	while (iter1.hasNext()) {
	    SummaryHours sh = iter1.next();
	    hours += sh.getHours(elementLink.getElementType());
	}
	ElementEntryValue hoursValue = getElementEntryValue(HoursValue.class);
	if (hoursValue != null) {
	    hoursValue.setValue(hours);
	}

	// Fetch rate
	float rate = 0;
	ElementEntryValue rateValue = getElementEntryValue(RateValue.class);
	if (rateValue != null) {
	    rate = rateValue.getValue();
	}

	// Fetch rate multiplier
	float multiplier = 1;
	ElementEntryValue multiplierValue = getElementEntryValue(MultiplierValue.class);
	if (multiplierValue != null) {
	    multiplier = multiplierValue.getValue();
	}

	// Compute and set pay
	float pay = rate * multiplier * hours;
	ElementEntryValue payValue = getElementEntryValue(PayMoneyValue.class);
	if (payValue != null) {
	    payValue.setValue(pay);
	}
    }

    private ElementEntryValue getElementEntryValue(Class inputValueClazz) {
	Iterator<ElementEntryValue> iter = entryValues.iterator();
	while (iter.hasNext()) {
	    ElementEntryValue eev = iter.next();
	    if (eev.getInputValueClass().equals(inputValueClazz)) {
		return eev;
	    }
	}
	return null;
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
