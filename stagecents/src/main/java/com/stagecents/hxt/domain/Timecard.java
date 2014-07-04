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
package com.stagecents.hxt.domain;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.stagecents.common.DateEffective;
import com.stagecents.common.EffectiveDateInterval;
import com.stagecents.hr.domain.Position;
import com.stagecents.pa.domain.Activity;
import com.stagecents.pay.domain.ElementType;
import com.stagecents.pay.domain.PayCycle.Frequency;
import com.stagecents.pay.domain.Payroll;

public class Timecard implements DateEffective {

    private Position position;
    private Payroll payroll;
    private EffectiveDateInterval effectiveDateRange;

    // Bidirectional one-to-many association.
    private SortedSet<SummaryHours> summaryHours = new TreeSet<SummaryHours>(
	    new SummaryHoursComparator());

    Timecard() {
    }

    public Timecard(Position position, Payroll payroll, LocalDate startDate,
	    LocalDate endDate) {
	this.position = position;
	this.payroll = payroll;
	this.effectiveDateRange = new EffectiveDateInterval(startDate, endDate);

	// Create daily summary entities for weekly and biweekly timecards
	Frequency f = payroll.getPayCycle().getFrequency();
	if (f.equals(Frequency.W) || f.equals(Frequency.F)) {
	    LocalDate dateWorked = startDate;
	    int calendarDays = f.equals(Frequency.W) ? 7 : 14;
	    for (int i = 0; i < calendarDays; i++) {
		SummaryHours sh = new SummaryHours(this, i, dateWorked);
		addSummaryHours(sh);
		dateWorked = dateWorked.plusDays(1);
	    }
	}
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
	this.position = position;
    }

    public void setPayroll(Payroll payroll) {
	this.payroll = payroll;
    }

    public SortedSet<SummaryHours> getSummaryHours() {
	return summaryHours;
    }

    public void addSummaryHours(SummaryHours arg) {
	arg.setTimecard(this);
	summaryHours.add(arg);
    }

    public void removeSummaryHours(SummaryHours arg) {
	arg.setTimecard(null);
	summaryHours.remove(arg);
    }

    public LocalDate getStartDate() {
	return effectiveDateRange.getStartDate().toLocalDate();
    }

    public SummaryHours getSummaryHours(LocalDate dateWorked) {
	Iterator<SummaryHours> iter = summaryHours.iterator();
	while (iter.hasNext()) {
	    SummaryHours sh = iter.next();
	    if (sh.getDateWorked().equals(dateWorked)) {
		return sh;
	    }
	}
	return null;
    }

    public void updateHours(Activity activity, ElementType element,
	    Interval interval) {
	LocalDate dateWorked = interval.getStart().toLocalDate();
	
	SummaryHours sh = getSummaryHours(dateWorked);
	if (sh == null && isEffective(dateWorked)) {
	    int seq = getSummaryHoursSequence(dateWorked);
	    sh = new SummaryHours(this, seq, dateWorked);
	}
	sh.updateHours(activity, element, interval);
    }

    public int getSummaryHoursSequence(LocalDate dateWorked) {
	int seq = 0;
	Iterator<SummaryHours> iter = summaryHours.iterator();
	while (iter.hasNext()) {
	    SummaryHours sh = iter.next();
	    if (sh.getDateWorked().equals(dateWorked)) {
		return sh.getSequence();
	    }
	    LocalDate tmp = getStartDate().plusDays(seq);
	    if(tmp.equals(dateWorked)) {
		return seq;
	    }
	    seq++;
	}
	return seq;
    }

    @Override
    public boolean isEffective(DateTime effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }

    @Override
    public boolean isEffective(LocalDate effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }

    public static class SummaryHoursComparator implements
	    Comparator<SummaryHours> {

	@Override
	public int compare(SummaryHours o1, SummaryHours o2) {
	    int s1 = o1.getSequence();
	    int s2 = o2.getSequence();
	    return (s1 < s2) ? -1 : ((s1 == s2) ? 0 : 1);
	}
    }
}
