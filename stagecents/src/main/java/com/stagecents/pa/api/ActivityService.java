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
package com.stagecents.pa.api;

import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.stagecents.hr.domain.Position;
import com.stagecents.hxt.domain.Timecard;
import com.stagecents.pa.domain.Activity;
import com.stagecents.pa.domain.PositionResource;
import com.stagecents.pa.domain.Resource;
import com.stagecents.pay.domain.ElementEntry;
import com.stagecents.pay.domain.ElementType;
import com.stagecents.pay.domain.PayCycle;

public class ActivityService {

    private Activity activity;

    public ActivityService(Activity activity) {
	this.activity = activity;
    }

    public void generateBudgetCost() {
	// 1. Delete any existing records.

	// 2. Generate new records.
	Iterator<Resource> iter = activity.getResources().iterator();
	while (iter.hasNext()) {
	    Resource resource = iter.next();
	    if (resource instanceof PositionResource) {
		PositionResource pr = (PositionResource) resource;
		generateHours(pr);
	    }
	}
    }

    private void generateHours(PositionResource resource) {
	Position position = resource.getResource();
	LocalDate startDate = activity.getScheduledStart().toLocalDate();
	LocalDate endDate = activity.getScheduledEnd().toLocalDate();

	PayCycle payCycle = resource.getPayroll().getPayCycle();
	LocalDate cycleStart = payCycle.getCycleStart(activity.duration());
	int cycles = payCycle.getCyclePeriods(activity.duration());
	for (int i = 0; i < cycles; i++) {
	    // Load or create time card.
	    Timecard timecard = position.getTimecard(cycleStart);
	    if (timecard == null) {
		LocalDate cycleEnd = payCycle.getCycleEnd(cycleStart);
		timecard = new Timecard(position, resource.getPayroll(),
			cycleStart, cycleEnd);
		position.addTimecard(timecard);
	    }

	    // Update the time card hours
	    updateTimecardHours(timecard, position, startDate, endDate);

	    // Update each element entry
	    updateElementEntry(timecard, position);

	    cycleStart = payCycle.getCycleEnd(cycleStart).plusDays(1);
	}
    }

    private void updateTimecardHours(Timecard timecard, Position position,
	    LocalDate startDate, LocalDate endDate) {
	while (timecard.isEffective(startDate)) {
	    Interval duration = new Interval(startDate.toDateTime(activity
		    .getScheduledStart()), startDate.toDateTime(activity
		    .getScheduledEnd()));

	    // Update the time card hours for each element type
	    Iterator<ElementType> iter = position.getElements().iterator();
	    while (iter.hasNext()) {
		ElementType e = iter.next();
		e.processHours(duration, activity, timecard, position);
	    }

	    // Increment the day
	    startDate = startDate.plusDays(1);
	    if (startDate.isAfter(endDate)) {
		return;
	    }
	}
    }

    private void updateElementEntry(Timecard timecard, Position position) {
	Iterator<ElementEntry> iter = position.getElementEntries().iterator();
	while (iter.hasNext()) {
	    ElementEntry ee = iter.next();
	    ee.processHours(timecard);
	}
    }
}
