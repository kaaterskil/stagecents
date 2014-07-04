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
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.stagecents.hr.domain.Position;
import com.stagecents.hxt.domain.Timecard;
import com.stagecents.pa.domain.Activity;
import com.stagecents.pa.domain.Performance;

/**
 * Represents a pre-defined ElementType for regular wage-based compensation.
 * 
 * @author Blair Caple
 */
public class RegularWages extends ElementType {

    public RegularWages(String name, String description, LocalDate startDate,
	    LocalDate endDate) {
	super(name, description, Type.REG_WAGE, Classification.REG_EARNINGS,
		ProcessingType.NON_RECURRING, startDate, endDate);
    }

    protected RegularWages(String name, String description, Type type,
	    LocalDate startDate, LocalDate endDate) {
	super(name, description, type, Classification.REG_EARNINGS,
		ProcessingType.RECURRING, startDate, endDate);

	// Create input values for hourly rate
	// The HoursValue input value should be set to true for real payroll, we
	// are using it for auto-entered budget values.
	addInputValue(new HoursValue(1, 0, startDate, endDate, false));
	addInputValue(new PayMoneyValue(1, 0, startDate, endDate, false));
	addInputValue(new MultiplierValue(2, 1, startDate, endDate, true));
	addInputValue(new RateValue(3, 0, startDate, endDate, true));
    }

    /**
     * Returns the decimal hours from the given time interval, adjusted by any
     * restricted hours specified by the given position and any regular hours
     * already accumulated in the given timecard.
     * 
     * @param interval
     *            The time period of the activity, adjusted to a single day.
     * @param activity
     *            The activity from which to compute regular hours.
     * @param timecard
     *            The timecard from which to accumulate prior regular hours.
     * @param position
     *            The position which participates in the given time interval and
     *            which may define restricted or maximum hours.
     */
    @Override
    public void processHours(Interval interval, Activity activity,
	    Timecard timecard, Position position) {
	if (activity instanceof Performance) {
	    return;
	}
	doProcessHours(interval, activity, timecard, position);
    }

    protected void doProcessHours(Interval interval, Activity activity,
	    Timecard timecard, Position position) {
	DateTime start = interval.getStart();
	DateTime end = interval.getEnd();

	// Test start time against normal start time.
	if (position.getNormalStart() != null) {
	    DateTime normalStart = start.toLocalDate().toDateTime(
		    position.getNormalStart());
	    start = start.isBefore(normalStart) ? normalStart : start;
	}

	// Test end time against minimum call.
	MinimumCallValue mc = getMinimumCall(activity.duration());
	if (mc != null) {
	    long minCall = (long) mc.getDefaultValue() * 1000 * 60 * 60;
	    DateTime minCallEnd = end.plus(minCall);
	    end = end.isBefore(minCallEnd) ? minCallEnd : end;
	}

	// Test end time against normal end time.
	if (position.getNormalEnd() != null) {
	    DateTime normalEnd = end.toLocalDate().toDateTime(
		    position.getNormalEnd());
	    end = end.isAfter(normalEnd) ? normalEnd : end;
	}

	// Test end time against maximum time for position.
	if (position.getMaximumHours() > 0) {
	    float priorHours = getPriorHours(timecard, start.toLocalDate());
	    long availMillis = (long) (position.getMaximumHours() - priorHours) * 1000 * 60 * 60;
	    DateTime availEnd = start.plus(availMillis);
	    end = availEnd.isBefore(end) ? availEnd : end;
	}

	updateTimecard(timecard, activity, new Interval(start, end));
    }

    private RateValue getRate(Interval interval) {
	Iterator<InputValue> iter = inputValues.iterator();
	while (iter.hasNext()) {
	    InputValue iv = iter.next();
	    if (iv instanceof RateValue) {
		RateValue rv = (RateValue) iv;
		if (rv.getEffectiveDateRange() == null
			|| isEffective(interval.getStart())) {
		    return rv;
		}
	    }
	}
	return null;
    }

    public static class RateValueComparator implements Comparator<RateValue> {

	@Override
	public int compare(RateValue o1, RateValue o2) {
	    DateTime d1 = o1.getStartDate();
	    DateTime d2 = o2.getStartDate();
	    return d1.isBefore(d2) ? -1 : (d1.isEqual(d2) ? 0 : 1);
	}

    }
}
