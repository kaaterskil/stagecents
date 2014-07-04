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

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import com.stagecents.common.DayOfWeek;

public class PayCycle {

    public enum Frequency {
	FF("Once (Flat fee)"), W("Weekly"), F("Bi-Weekly"), SM("Semi-Monthly"), CM(
		"Monthly"), Q("Quarterly"), Y("Annually");
	private String meaning;

	private Frequency(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    private String name;
    private Frequency frequency;
    private DayOfWeek periodEndDay;
    private int payDateOffset;
    private boolean nonCash;

    // Bidirectional one-to-many association
    private SortedSet<PayCycleSplit> splitPayments = new TreeSet<PayCycleSplit>(
	    new PayCycleSplitComparator());

    PayCycle() {
    }

    /**
     * Creates a new PayCycle with the given information.
     * 
     * @param name The name of the pay cycle.
     * @param frequency The pay cycle frequency, e.g. weekly.
     * @param payDateOffset The number of days between the end of the pay cycle
     *            and the pay date. This number may be positive (in arrears) or
     *            negative (in advance),
     * @param nonCash True if the pay is non-cash, false otherwise.
     */
    public PayCycle(String name, Frequency frequency, int payDateOffset,
	    boolean nonCash) {
	this(name, frequency, null, payDateOffset, nonCash);
    }

    /**
     * Creates a new PayCycle with a known day of the week as the end of the pay
     * cycle, i.e. for weekly and bi-weekly payrolls.
     * 
     * @param name The name of the pay cycle.
     * @param frequency The pay cycle frequency, e.g. weekly.
     * @param periodEndDay The day of the week that the pay cycle ends.
     * @param payDateOffset The number of days between the end of the pay cycle
     *            and the pay date. This number may be positive (in arrears) or
     *            negative (in advance),
     * @param nonCash True if the pay is non-cash, false otherwise.
     */
    public PayCycle(String name, Frequency frequency, DayOfWeek periodEndDay,
	    int payDateOffset, boolean nonCash) {
	this.name = name;
	this.frequency = frequency;
	this.periodEndDay = periodEndDay;
	this.payDateOffset = payDateOffset;
	this.nonCash = nonCash;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public DayOfWeek getPeriodEndDay() {
	return periodEndDay;
    }

    public LocalDate getCycleStart(Interval arg) {
	LocalDate result = null;
	if (frequency.equals(Frequency.W)) {
	    result = getWeeklyCycleStart(arg).toLocalDate();

	} else if (frequency.equals(Frequency.F)) {
	    // TODO Need to compute biweekly start date

	} else if (frequency.equals(Frequency.SM)) {
	    result = (arg.getStart().getDayOfMonth() <= 15) ? arg.getStart()
		    .dayOfMonth().withMinimumValue().toLocalDate() : arg
		    .getStart().withDayOfMonth(15).toLocalDate();

	} else if (frequency.equals(Frequency.CM)) {
	    result = arg.getStart().withDayOfMonth(1).toLocalDate();

	} else if (frequency.equals(Frequency.Q)) {
	    int adjStartMonth = getQuarterStartMonth(arg.getStart()
		    .getMonthOfYear());
	    result = arg.getStart().withMonthOfYear(adjStartMonth)
		    .withDayOfMonth(1).toLocalDate();
	}
	return result;
    }

    public LocalDate getCycleEnd(LocalDate cycleStart) {
	LocalDate result = null;
	if (frequency.equals(Frequency.W)) {
	    result = cycleStart.plusDays(6);
	    
	} else if (frequency.equals(Frequency.F)) {
	    result = cycleStart.plusDays(13);
	    
	} else if (frequency.equals(Frequency.SM)) {
	    result = (cycleStart.getDayOfMonth() == 1) ? cycleStart
		    .withDayOfMonth(15) : cycleStart.dayOfMonth()
		    .withMaximumValue();
		    
	} else if(frequency.equals(Frequency.CM)) {
	    result = cycleStart.dayOfMonth().withMaximumValue();
	    
	} else if(frequency.equals(Frequency.Q)) {
	    result = cycleStart.plusMonths(2).dayOfMonth().withMaximumValue();
	}
	return result;
    }

    private DateTime getWeeklyCycleStart(Interval arg) {
	int argStartDOW = arg.getStart().getDayOfWeek();
	int cycleStartDOW = (periodEndDay.getIsoValue() == 7) ? 1
		: periodEndDay.getIsoValue() + 1;

	DateTime cycleStart = null;
	if (argStartDOW < cycleStartDOW) {
	    cycleStart = arg.getStart().minusDays(argStartDOW - cycleStartDOW)
		    .withTimeAtStartOfDay();
	} else {
	    cycleStart = arg.getStart()
		    .minusDays(argStartDOW - cycleStartDOW + 7)
		    .withTimeAtStartOfDay();
	}
	return cycleStart;
    }

    public int getCyclePeriods(Interval arg) {
	int result = 0;
	if (frequency.equals(Frequency.W) || frequency.equals(Frequency.F)) {
	    // Weekly and biweekly pay cycles
	    result = getCycleDays(arg);

	} else if (frequency.equals(Frequency.SM)) {
	    // Semimonthly pay cycle
	    result = getSemiMonthlyCycles(arg);

	} else if (frequency.equals(Frequency.CM)) {
	    // Monthly pay cycle
	    result = getMonthlyCycles(arg);

	} else if (frequency.equals(Frequency.Q)) {
	    // Quarterly pay cycle
	    result = getQuarterlyCycles(arg);

	} else if (frequency.equals(Frequency.Y)) {
	    // Annual pay cycle
	    result = getAnnualCycles(arg);

	}
	return result;
    }

    /**
     * Returns the number of elapsed days in this pay cycle including the given
     * time interval.
     * 
     * @param arg The given time interval
     * @return The number of days since the beginning of this pay cycle,
     *         inclusive of the given time interval.
     */
    private int getCycleDays(Interval arg) {
	return Days.daysIn(arg.withStart(getWeeklyCycleStart(arg))).getDays();
    }

    /**
     * Returns the number of semimonthly pay cycles in the given time interval.
     * 
     * @param arg The time period to analyze.
     * @return The number of semimonthly pay cycles in the given time interval.
     */
    private int getSemiMonthlyCycles(Interval arg) {
	int startPer = arg.getStart().getDayOfMonth() <= 15 ? 1 : 2;
	int endPer = arg.getEnd().getDayOfMonth() <= 15 ? 1 : 2;
	DateTime adjStart = (arg.getStart().getDayOfMonth() <= 15) ? arg
		.getStart().dayOfMonth().withMinimumValue() : arg.getStart()
		.withDayOfMonth(15);
	DateTime adjEnd = (arg.getEnd().getDayOfMonth() <= 15) ? arg.getEnd()
		.dayOfMonth().withMinimumValue() : arg.getEnd().dayOfMonth()
		.withMaximumValue();

	return (Months.monthsBetween(adjStart, adjEnd).getMonths() * 2)
		+ ((startPer == endPer) ? 1 : 2);
    }

    /**
     * Returns the number of monthly pay cycles in the given time interval.
     * 
     * @param arg The time interval to analyze.
     * @return The number of monthly pay cycles in the given time interval.
     */
    private int getMonthlyCycles(Interval arg) {
	DateTime adjStart = arg.getStart().withDayOfMonth(1);
	DateTime adjEnd = (arg.getEnd().getDayOfMonth() < 15) ? arg.getEnd()
		.withDayOfMonth(1) : arg.getEnd().dayOfMonth()
		.withMaximumValue();

	return Months.monthsBetween(adjStart, adjEnd).getMonths() + 1;
    }

    /**
     * Returns the number of calendar quarters in the given time interval.
     * 
     * @param arg The time interval to analyze.
     * @return The number of calendar quarters in the given time interval.
     */
    private int getQuarterlyCycles(Interval arg) {
	int adjStartMonth = getQuarterStartMonth(arg.getStart()
		.getMonthOfYear());
	int adjEndMonth = getQuarterEndMonth(arg.getEnd().getMonthOfYear());
	DateTime adjStart = arg.getStart().withMonthOfYear(adjStartMonth)
		.withDayOfMonth(1);
	DateTime adjEnd = arg.getEnd().withMonthOfYear(adjEndMonth)
		.dayOfMonth().withMaximumValue();

	return (int) Math.ceil((Months.monthsBetween(adjStart, adjEnd)
		.getMonths() + 1) / 3d);
    }

    /**
     * Returns the number of calendar years in the given time interval.
     * 
     * @param arg The time interval to analyze.
     * @return The number of calendar years.
     */
    private int getAnnualCycles(Interval arg) {
	return arg.getEnd().getYear() - arg.getStart().getYear() + 1;
    }

    private int getQuarterStartMonth(int v) {
	int[] qtrs = { 1, 4, 7, 10 };
	for (int i = (qtrs.length - 1); i >= 0; i--) {
	    if (v >= qtrs[i]) {
		return qtrs[i];
	    }
	}
	return -1;
    }

    private int getQuarterEndMonth(int v) {
	int[] qtrs = { 3, 6, 9, 12 };
	for (int i = 0; i < qtrs.length; i++) {
	    if (v <= qtrs[i]) {
		return qtrs[i];
	    }
	}
	return -1;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result
		+ ((frequency == null) ? 0 : frequency.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof PayCycle)) {
	    return false;
	}
	PayCycle other = (PayCycle) obj;
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	if (frequency != other.frequency) {
	    return false;
	}
	return true;
    }

    public static class PayCycleSplitComparator implements
	    Comparator<PayCycleSplit> {

	@Override
	public int compare(PayCycleSplit o1, PayCycleSplit o2) {
	    int off1 = o1.getPayDateOffset();
	    int off2 = o2.getPayDateOffset();
	    return off1 < off2 ? -1 : (off1 == off2 ? 0 : 1);
	}

    }
}
