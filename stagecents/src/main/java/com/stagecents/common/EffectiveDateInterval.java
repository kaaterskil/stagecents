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
import org.joda.time.LocalDate;

public class EffectiveDateInterval implements DateEffective {

    private DateTime startDate;
    private DateTime endDate;

    /**
     * Creates a new EffectiveDateInterval from the given <code>startDate</code>
     * and <code>endDate</code>.
     * <p>
     * If <code>startDate</code> is null, the start date of the
     * EffectiveDateInterval will be set to the beginning of time. If
     * <code>endDate</code> is null, the end date of the EffectiveDateInterval
     * will be set to the end of time.
     * 
     * @param startDate The date on which the EffectiveDateInterval becomes
     *            effective.
     * @param endDate The date after which the EffectiveDateInterval is no
     *            longer effective, or the end of time if null.
     */
    public EffectiveDateInterval(DateTime startDate, DateTime endDate) {
	this.startDate = (startDate == null) ? new DateTime(0) : startDate;
	this.endDate = (endDate == null) ? new DateTime(Long.MAX_VALUE)
		: endDate;
    }

    /**
     * Creates a new EffectiveDateInterval from the given <code>LocalDate</code>
     * start and end dates. The <code>startDate</code> is converted to a
     * <code>DateTime</code> object by setting it to midnight (00:00) at the
     * start of the given day. The <code>endDate</code> is converted to a
     * <code>DateTime</code> object by setting it to midnight (00:00) of the day
     * following the given day.
     * <p>
     * If <code>startDate</code> is null, the start date of the
     * EffectiveDateInterval will be set to the beginning of time. If
     * <code>endDate</code> is null, the end date of the EffectiveDateInterval
     * will be set to the end of time.
     * 
     * @param startDate The date on which the EffectiveDateInterval becomes
     *            effective.
     * @param endDate The date after which the EffectiveDateInterval is no
     *            longer effective or the end of time if null.
     * @see http://www.joda.org/joda-time/key_partial.html
     */
    public EffectiveDateInterval(LocalDate startDate, LocalDate endDate) {
	if (startDate != null) {
	    // Convert start day to midnight
	    this.startDate = startDate.toDateTimeAtStartOfDay();
	} else {
	    this.startDate = new DateTime(0);
	}
	if (endDate != null) {
	    this.endDate = endDate.plusDays(1).toDateTimeAtStartOfDay();
	} else {
	    this.endDate = new DateTime(Long.MAX_VALUE);
	}
    }

    public DateTime getStartDate() {
	return startDate;
    }

    public DateTime getEndDate() {
	return endDate;
    }

    public boolean isEffective(DateTime effectiveDate) {
	Interval interval = new Interval(startDate, endDate);
	return interval.contains(effectiveDate);
    }

    public boolean isEffective(LocalDate effectiveDate) {
	Interval period = new Interval(startDate, endDate);
	return period.contains(effectiveDate.toInterval());
    }

    /**
     * Returns the overlap between the effective date range represented by this
     * EffectivedateInterval object and the given interval.
     * 
     * @param arg The interval to test for overlap.
     * @return The overlapping interval.
     */
    public Interval getOverlap(Interval arg) {
	Interval period = new Interval(startDate, endDate);
	return period.overlap(arg);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
	result = prime * result
		+ ((startDate == null) ? 0 : startDate.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof EffectiveDateInterval)) {
	    return false;
	}
	EffectiveDateInterval other = (EffectiveDateInterval) obj;
	if (endDate == null) {
	    if (other.endDate != null) {
		return false;
	    }
	} else if (!endDate.equals(other.endDate)) {
	    return false;
	}
	if (startDate == null) {
	    if (other.startDate != null) {
		return false;
	    }
	} else if (!startDate.equals(other.startDate)) {
	    return false;
	}
	return true;
    }
}
