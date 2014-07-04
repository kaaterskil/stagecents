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
package com.stagecents.gl.query;

import org.joda.time.DateTime;

import com.stagecents.gl.domain.PeriodType;

public class PeriodEntry {

    private String periodId;
    private CalendarEntry calendar;
    private String name;
    private String description;
    private DateTime startDate;
    private DateTime endDate;
    private DateTime yearStartDate;
    private PeriodType periodType;
    private Integer priodYear;
    private Integer periodNum;
    private Boolean adjustmentPeriod;

    public PeriodEntry(String periodId, CalendarEntry calendar, String name,
	    String description, DateTime startDate, DateTime endDate,
	    DateTime yearStartDate, PeriodType periodType, Integer priodYear, Integer periodNum,
	    Boolean adjustmentPeriod) {
	this.periodId = periodId;
	this.calendar = calendar;
	this.name = name;
	this.description = description;
	this.startDate = startDate;
	this.endDate = endDate;
	this.yearStartDate = yearStartDate;
	this.periodType = periodType;
	this.priodYear = priodYear;
	this.periodNum = periodNum;
	this.adjustmentPeriod = adjustmentPeriod;
    }

    public String getPeriodId() {
	return periodId;
    }

    public CalendarEntry getCalendar() {
	return calendar;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    public DateTime getStartDate() {
	return startDate;
    }

    public DateTime getEndDate() {
	return endDate;
    }

    public DateTime getYearStartDate() {
	return yearStartDate;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public Integer getPriodYear() {
	return priodYear;
    }

    public Integer getPeriodNum() {
	return periodNum;
    }

    public Boolean getAdjustmentPeriod() {
	return adjustmentPeriod;
    }
}
