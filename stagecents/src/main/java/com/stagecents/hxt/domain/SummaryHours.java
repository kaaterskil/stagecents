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

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.stagecents.pa.domain.Activity;
import com.stagecents.pa.domain.Project;
import com.stagecents.pay.domain.ElementType;

/**
 * Represents a summary of the number of hours and type of premium associated
 * with each hour worked in a payroll period.
 * 
 * @author Blair Caple
 */
public class SummaryHours {

    // Bidirectional many-to-one associations.
    private Timecard timecard;
    private int sequence;
    private LocalDate dateWorked;
    private float hours;

    // Optional properties
    private Project project;
    private ElementType elementType;

    private float hourlyRate;
    private float rateMultiple;
    private float amount;

    // Bidirectional one-to-many association.
    private SortedSet<DetailHours> detailHours = new TreeSet<DetailHours>(
	    new DetailHoursComparator());

    SummaryHours() {
    }

    public SummaryHours(Timecard timecard, int sequence, LocalDate dateWorked) {
	this.timecard = timecard;
	this.sequence = sequence;
	this.dateWorked = dateWorked;
    }

    public SummaryHours(Timecard timecard, Project project,
	    ElementType elementType, int sequence, LocalDate dateWorked,
	    float hours, float hourlyRate, float rateMultiple, float amount) {
	this.timecard = timecard;
	this.project = project;
	this.elementType = elementType;
	this.sequence = sequence;
	this.dateWorked = dateWorked;
	this.hours = hours;
	this.amount = amount;
	this.hourlyRate = hourlyRate;
	this.rateMultiple = rateMultiple;
    }

    public Timecard getTimecard() {
	return timecard;
    }

    public void setTimecard(Timecard timecard) {
	this.timecard = timecard;
    }

    public ElementType getElementType() {
	return elementType;
    }

    public int getSequence() {
	return sequence;
    }

    public LocalDate getDateWorked() {
	return dateWorked;
    }

    public void addDetailHours(DetailHours arg) {
	arg.setSummaryHours(this);
	detailHours.add(arg);
    }

    public void removeDetailHours(DetailHours arg) {
	arg.setSummaryHours(null);
	detailHours.remove(arg);
    }

    public float getHours(ElementType element) {
	Iterator<DetailHours> iter = detailHours.iterator();
	while (iter.hasNext()) {
	    DetailHours dh = iter.next();
	    if (dh.getElementType().equals(element)) {
		return dh.getHours();
	    }
	}
	return 0F;
    }

    public void updateHours(Activity activity, ElementType element,
	    Interval interval) {
	DetailHours dh = getDetailHours(activity, element);
	if (dh == null) {
	    Timecard t = this.getTimecard();
	    dh = new DetailHours(this, t, t.getPosition(), element, activity,
		    interval);
	}
	dh.update(interval);
	recalculateHours();
    }

    private DetailHours getDetailHours(Activity activity, ElementType element) {
	DetailHours dh = null;
	Iterator<DetailHours> iter = detailHours.iterator();
	while (iter.hasNext()) {
	    DetailHours each = iter.next();
	    if (each.getActivity().equals(activity)
		    && each.getElementType().equals(element)) {
		dh = each;
		break;
	    }
	}
	return dh;
    }

    private void recalculateHours() {
	float hoursWorked = 0;
	Iterator<DetailHours> iter = detailHours.iterator();
	while (iter.hasNext()) {
	    hoursWorked += iter.next().getHours();
	}
	hours = hoursWorked;
    }

    public static class DetailHoursComparator implements
    Comparator<DetailHours> {

	@Override
	public int compare(DetailHours o1, DetailHours o2) {
	    LocalTime s1 = o1.timeIn();
	    LocalTime s2 = o1.timeIn();
	    if (s1.isBefore(s2)) {
		return -1;
	    } else if (s1.isEqual(s2)) {
		LocalTime e1 = o1.timeOut();
		LocalTime e2 = o2.timeOut();
		return e1.isBefore(e2) ? -1 : (e1.isEqual(e2) ? 0 : 1);
	    }
	    return 1;
	}

    }
}
