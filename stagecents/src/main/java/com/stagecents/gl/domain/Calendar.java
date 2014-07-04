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
package com.stagecents.gl.domain;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import com.stagecents.gl.api.command.CalendarCreatedEvent;
import com.stagecents.gl.api.command.CalendarDTO;
import com.stagecents.gl.api.command.PeriodDTO;

/**
 * Represents a calendar.
 * 
 * @author Blair Caple
 *
 */
public class Calendar extends AbstractAnnotatedAggregateRoot<CalendarId> {

    @AggregateIdentifier
    private CalendarId calendarId;
    private int version;

    private String name;
    private String description;
    private SortedSet<Period> periods = new TreeSet<Period>(
	    new PeriodComparator());

    Calendar() {
    }

    public Calendar(CalendarDTO data) {
	apply(new CalendarCreatedEvent(data.getCalendarId(), data));
    }

    public SortedSet<Period> getPeriods() {
	return periods;
    }

    public void addPeriod(Period period) {
	period.setPeriodSet(this);
	periods.add(period);
    }

    @EventHandler
    public void handleCreateCalendar(CalendarCreatedEvent event) {
	calendarId = event.getCalendarId();
	name = event.getCalendatDTO().getName();
	description = event.getCalendatDTO().getDescription();

	Iterator<PeriodDTO> iter = event.getCalendatDTO().getPeriods()
		.iterator();
	while (iter.hasNext()) {
	    PeriodDTO data = iter.next();
	    Period period = new Period(data.getPeriodId(), this,
		    data.getName(), data.getDescription(), data.getStartDate(),
		    data.getEndDate(), data.getYearStartDate(),
		    data.getPeriodType(), data.getPeriodYear(),
		    data.getPeriodNum(), data.isAdjustmentPeriod());
	    addPeriod(period);
	}
    }

    private static class PeriodComparator implements Comparator<Period> {

	@Override
	public int compare(Period o1, Period o2) {
	    int n1 = o1.getPeriodNum();
	    int n2 = o2.getPeriodNum();
	    return (n1 < n2) ? -1 : ((n1 == n2) ? 0 : 1);
	}

    }
}
