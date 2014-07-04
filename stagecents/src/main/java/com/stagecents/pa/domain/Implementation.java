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
package com.stagecents.pa.domain;

import com.stagecents.common.DayOfWeek;
import com.stagecents.gl.domain.CalendarId;
import com.stagecents.gl.domain.LedgerId;
import com.stagecents.hr.domain.OrganizationUnit;

public class Implementation {

    private OrganizationUnit startOrganization;
    private LedgerId ledgerId;
    private CalendarId calendarId;
    private DayOfWeek expenditureCycleStartDay;
    private int defaultHoursPerDay;
    private int defaultHoursPerWeek;

    Implementation() {
    }

    public Implementation(OrganizationUnit startOrganization,
	    LedgerId ledgerId, CalendarId calendarId,
	    DayOfWeek expenditureCycleStartDay, int defaultHoursPerDay,
	    int defaultHoursPerWeek) {
	this.startOrganization = startOrganization;
	this.ledgerId = ledgerId;
	this.calendarId = calendarId;
	this.expenditureCycleStartDay = expenditureCycleStartDay;
	this.defaultHoursPerDay = defaultHoursPerDay;
	this.defaultHoursPerWeek = defaultHoursPerWeek;
    }

    public LedgerId getLedgerId() {
	return ledgerId;
    }

    public CalendarId getCalendarId() {
	return calendarId;
    }

    public DayOfWeek getExpenditureCycleStartDay() {
	return expenditureCycleStartDay;
    }

    public int getDefaultHoursPerDay() {
	return defaultHoursPerDay;
    }

    public int getDefaultHoursPerWeek() {
	return defaultHoursPerWeek;
    }
}
