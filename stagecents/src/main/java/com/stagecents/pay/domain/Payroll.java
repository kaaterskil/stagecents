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
import java.util.Set;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.stagecents.common.EffectiveDateInterval;
import com.stagecents.gl.domain.Ledger;
import com.stagecents.hr.domain.OrganizationUnit;
import com.stagecents.hxt.domain.Timecard;

/**
 * Represents the definition of a group of employees who share the same
 * frequency of processing and payment defined by the payroll calendar.
 * 
 * @author Blair Caple
 */
public class Payroll {

    private EffectiveDateInterval effectiveDateRange;
    private String name;
    private String comments;
    private LocalDate firstEndDate;
    private int cutoffDayOffset;
    private int payDayOffset;
    private int midpointOffset;

    // Unidirectional many-to-one association (required)
    private PayCycle payCycle;
    private Ledger ledger;

    // Unidirectional many-to-one association (optional)
    private OrganizationUnit businessGroup;

    // Bidirectional one-to-many associations.
    private Set<ElementLink> elements = new HashSet<ElementLink>();
    private Set<Timecard> timecards = new HashSet<Timecard>();

    Payroll() {
    }

    public Payroll(PayCycle payCycle, Ledger ledger, String name,
	    String comments, LocalDate firstEndDate, int cutoffDayOffset,
	    int payDayOffset, int midpointOffset,
	    OrganizationUnit businessGroup, LocalDate startDate,
	    LocalDate endDate) {
	this.payCycle = payCycle;
	this.ledger = ledger;
	this.name = name;
	this.comments = comments;
	this.firstEndDate = firstEndDate;
	this.cutoffDayOffset = cutoffDayOffset;
	this.payDayOffset = payDayOffset;
	this.midpointOffset = midpointOffset;
	this.businessGroup = businessGroup;
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
    }

    public PayCycle getPayCycle() {
	return payCycle;
    }

    public void linkElement(ElementLink arg) {
	arg.setPayroll(this);
	elements.add(arg);
    }

    public void unlinkElement(ElementLink arg) {
	arg.setPayroll(null);
	elements.remove(arg);
    }

    public void addTimecard(Timecard arg) {
	arg.setPayroll(this);
	timecards.add(arg);
    }

    public void removeTimecard(Timecard arg) {
	arg.setPayroll(null);
	timecards.remove(arg);
    }

    /**
     * Returns the number of pay cycles in the given time interval. For weekly
     * and biweekly pay cycles, the method returns the number of days of the
     * current pay cycle in the given time interval. For flat fee arrangements,
     * the method returns 0.
     * 
     * @param arg The time period to analyze.
     * @return The number of pay cycles in the given time period, or days of the
     *         current pay cycle for weekly and biweekly pay cycles.
     */
    public int getPayCyclePeriods(Interval arg) {
	return payCycle.getCyclePeriods(arg);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result
		+ ((payCycle == null) ? 0 : payCycle.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof Payroll)) {
	    return false;
	}
	Payroll other = (Payroll) obj;
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	if (payCycle == null) {
	    if (other.payCycle != null) {
		return false;
	    }
	} else if (!payCycle.equals(other.payCycle)) {
	    return false;
	}
	return true;
    }

}
