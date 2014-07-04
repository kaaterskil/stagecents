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

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.stagecents.hr.domain.Position;
import com.stagecents.hxt.domain.Timecard;
import com.stagecents.pa.domain.Activity;

/**
 * Represents a pre-defined ElementType for overtime compensation.
 * 
 * @author Blair Caple
 */
public class Overtime extends ElementType {

    public Overtime() {
	super();
    }

    public Overtime(String name, String description, LocalDate startDate,
	    LocalDate endDate) {
	super(name, description, Type.OVERTIME, Classification.OT_EARNINGS,
		ProcessingType.NON_RECURRING, startDate, endDate);

	// Create default input values.
	addInputValue(new HoursValue(1, 0, startDate, endDate, true));
	addInputValue(new PayMoneyValue(1, 0, startDate, endDate, false));
	addInputValue(new OvertimeMultiplierValue(3, 1, startDate, endDate,
		true));
	addInputValue(new RateValue(2, 0, startDate, endDate, true));
    }

    @Override
    public void processHours(Interval interval, Activity activity,
	    Timecard timecard, Position position) {
	DateTime start = interval.getStart();
	DateTime end = interval.getEnd();

	// 1. Test start time against normal start time.
	DateTime regStart = start;
	if (position.getNormalStart() != null) {
	    DateTime normalStart = start.toLocalDate().toDateTime(
		    position.getNormalStart());
	    regStart = regStart.isBefore(normalStart) ? normalStart : regStart;
	}

	// Update the time card with any premium time prior to the position's
	// normal start
	if (start.isBefore(regStart)) {
	    updateTimecard(timecard, activity, new Interval(start, regStart));
	}

	// 2. Test end time against minimum call.
	MinimumCallValue mc = getMinimumCall(new Interval(start, end));
	if (mc != null) {
	    long minCall = (long) mc.getDefaultValue() * 1000 * 60 * 60;
	    DateTime minCallEnd = end.plus(minCall);
	    end = end.isBefore(minCallEnd) ? minCallEnd : end;
	}

	// Test end time against normal end time.
	DateTime regEnd = end;
	if (position.getNormalEnd() != null) {
	    DateTime normalEnd = end.toLocalDate().toDateTime(
		    position.getNormalEnd());
	    regEnd = normalEnd.isBefore(regEnd) ? normalEnd : regEnd;
	}

	// Test end time against maximum time for position.
	if (position.getMaximumHours() > 0) {
	    float priorHrs = getPriorHours(timecard, regStart.toLocalDate());
	    long availMillis = (long) (position.getMaximumHours() - priorHrs) * 1000 * 60 * 60;
	    DateTime availEnd = regStart.plus(availMillis);
	    regEnd = availEnd.isBefore(regEnd) ? availEnd : regEnd;
	}

	// Update the timecard with any premium time after the position's normal
	// end
	if (regEnd.isBefore(end)) {
	    updateTimecard(timecard, activity, new Interval(regEnd, end));
	}
    }
}
