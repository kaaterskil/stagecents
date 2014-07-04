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

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.stagecents.common.DateEffective;
import com.stagecents.common.EffectiveDateInterval;
import com.stagecents.hxt.domain.Timecard;
import com.stagecents.pay.domain.ElementEntry;

public class Position extends Party implements DateEffective {

    public enum PositionType {
	SINGLE, SHARED;
    }

    private PositionType type;
    private EffectiveDateInterval effectiveDateRange;

    // Unidirectional many-to-one associations.
    private Location location;
    private Grade grade;
    private SalaryBasis salaryBasis;
    private CollectiveAgreement collectiveAgreement;

    private int maxPersons;
    private LocalTime normalStart;
    private LocalTime normalEnd;
    private int workingHours;
    private float minimumCall;
    private float maximumHours;
    private boolean weekendOvertime = true;

    // Bidirectional one-to-many associations.
    private SortedSet<Timecard> timecards = new TreeSet<Timecard>(
	    new TimecardComparator());
    private Set<ElementEntry> elementEntries = new HashSet<ElementEntry>();

    Position() {
	super();
    }

    public Position(LocalDate startDate, LocalDate endDate, String name,
	    Location location, PositionType type, SalaryBasis salaryBasis,
	    int maxPersons, CollectiveAgreement collectiveAgreement) {
	super(name, PartyType.POSITION);
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
	this.location = location;
	this.type = type;
	this.salaryBasis = salaryBasis;
	this.maxPersons = maxPersons;
	this.collectiveAgreement = collectiveAgreement;
    }

    public void setGrade(Grade grade) {
	this.grade = grade;
    }

    public LocalTime getNormalStart() {
        return normalStart;
    }

    public LocalTime getNormalEnd() {
        return normalEnd;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public float getMaximumHours() {
        return maximumHours;
    }

    public boolean isWeekendOvertime() {
        return weekendOvertime;
    }

    public Set<ElementEntry> getElementEntries() {
        return elementEntries;
    }

    public void addTimecard(Timecard arg) {
	arg.setPosition(this);
	timecards.add(arg);
    }

    public void removeTimecard(Timecard arg) {
	arg.setPosition(null);
	timecards.remove(arg);
    }

    public Timecard getTimecard(LocalDate effectiveDate) {
	Iterator<Timecard> iter = timecards.iterator();
	while (iter.hasNext()) {
	    Timecard tc = iter.next();
	    if (tc.isEffective(effectiveDate)) {
		return tc;
	    }
	}
	return null;
    }
    
    public void addElementEntry(ElementEntry arg) {
	arg.setPosition(this);
	elementEntries.add(arg);
    }
    
    public void removeElementEntry(ElementEntry arg) {
	arg.setPosition(null);
	elementEntries.remove(arg);
    }

    int checkMaxPersons(int maxPersons) {
	if (type.equals(PositionType.SINGLE) && maxPersons != 1) {
	    throw new IllegalArgumentException("max persons must equal 1");
	}
	return maxPersons;
    }

    @Override
    public boolean isEffective(DateTime effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }

    @Override
    public boolean isEffective(LocalDate effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }

    public static class TimecardComparator implements Comparator<Timecard> {

	@Override
	public int compare(Timecard o1, Timecard o2) {
	    LocalDate d1 = o1.getStartDate();
	    LocalDate d2 = o2.getStartDate();
	    return d1.isBefore(d2) ? -1 : (d1.isEqual(d2) ? 0 : 1);
	}

    }
}
