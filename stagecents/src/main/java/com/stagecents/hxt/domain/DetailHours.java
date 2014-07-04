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

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import com.stagecents.gl.domain.AccountCode;
import com.stagecents.hr.domain.Position;
import com.stagecents.pa.domain.Activity;
import com.stagecents.pay.domain.ElementType;

/**
 * Represents a specific block of time worked for a specific date.
 * 
 * @author Blair Caple
 */
public class DetailHours {

    private SummaryHours summaryHours;
    private Timecard timecard;
    private Position position;
    private ElementType elementType;
    private Activity activity;
    private float hours;
    private Interval timeWorked;

    private AccountCode laborAccount;
    private float hourlyRate;
    private float rateMultiplier;
    private float amount;

    DetailHours() {
    }

    public DetailHours(SummaryHours summaryHours, Timecard timecard,
	    Position position, ElementType elementType, Activity activity,
	    Interval timeWorked) {
	this.summaryHours = summaryHours;
	this.timecard = timecard;
	this.position = position;
	this.elementType = elementType;
	this.activity = activity;
	this.timeWorked = timeWorked;
    }

    public DetailHours(SummaryHours summaryHours, Timecard timecard,
	    Position position, float hours, DateTime start, DateTime end,
	    ElementType elementType, Activity activity) {
	this.summaryHours = summaryHours;
	this.timecard = timecard;
	this.position = position;
	this.hours = hours;
	this.timeWorked = new Interval(start, end);
	this.elementType = elementType;
	this.activity = activity;
    }

    public void setSummaryHours(SummaryHours summaryHours) {
	this.summaryHours = summaryHours;
    }

    public Activity getActivity() {
	return activity;
    }

    public void setActivity(Activity activity) {
	this.activity = activity;
    }

    public ElementType getElementType() {
	return elementType;
    }

    public float getHours() {
	return hours;
    }

    public void setTimeWorked(DateTime start, DateTime end) {
	this.timeWorked = new Interval(start, end);
    }

    public LocalTime timeIn() {
	return timeWorked.getStart().toLocalTime();
    }

    public LocalTime timeOut() {
	return timeWorked.getEnd().toLocalTime();
    }

    public void update(Interval timeWorked) {
	this.timeWorked = timeWorked;
	hours = timeWorked.toDurationMillis() / 1000 / 60 / 60;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((activity == null) ? 0 : activity.hashCode());
	result = prime * result
		+ ((elementType == null) ? 0 : elementType.hashCode());
	result = prime * result
		+ ((position == null) ? 0 : position.hashCode());
	result = prime * result
		+ ((summaryHours == null) ? 0 : summaryHours.hashCode());
	result = prime * result
		+ ((timeWorked == null) ? 0 : timeWorked.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof DetailHours)) {
	    return false;
	}
	DetailHours other = (DetailHours) obj;
	if (activity == null) {
	    if (other.activity != null) {
		return false;
	    }
	} else if (!activity.equals(other.activity)) {
	    return false;
	}
	if (elementType == null) {
	    if (other.elementType != null) {
		return false;
	    }
	} else if (!elementType.equals(other.elementType)) {
	    return false;
	}
	if (position == null) {
	    if (other.position != null) {
		return false;
	    }
	} else if (!position.equals(other.position)) {
	    return false;
	}
	if (summaryHours == null) {
	    if (other.summaryHours != null) {
		return false;
	    }
	} else if (!summaryHours.equals(other.summaryHours)) {
	    return false;
	}
	if (timeWorked == null) {
	    if (other.timeWorked != null) {
		return false;
	    }
	} else if (!timeWorked.equals(other.timeWorked)) {
	    return false;
	}
	return true;
    }
}
