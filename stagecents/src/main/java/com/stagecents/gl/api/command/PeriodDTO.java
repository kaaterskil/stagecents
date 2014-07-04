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
package com.stagecents.gl.api.command;

import org.joda.time.LocalDate;

import com.stagecents.gl.domain.PeriodType;

public class PeriodDTO {

    private String periodId;
    private CalendarDTO calendar;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate yearStartDate;
    private PeriodType periodType;
    private int periodYear;
    private int periodNum;
    private boolean adjustmentPeriod;

    public PeriodDTO(String periodId, CalendarDTO calendar, String name,
	    String description, LocalDate startDate, LocalDate endDate,
	    LocalDate yearStartDate, PeriodType periodType, int periodYear, int periodNum,
	    boolean adjustmentPeriod) {
	this.periodId = periodId;
	this.calendar = calendar;
	this.name = name;
	this.description = description;
	this.startDate = startDate;
	this.endDate = endDate;
	this.yearStartDate = yearStartDate;
	this.periodType = periodType;
	this.periodYear = periodYear;
	this.periodNum = periodNum;
	this.adjustmentPeriod = adjustmentPeriod;
    }

    public String getPeriodId() {
	return periodId;
    }

    public CalendarDTO getCalendar() {
	return calendar;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    public LocalDate getStartDate() {
	return startDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public LocalDate getYearStartDate() {
	return yearStartDate;
    }

    public void setYearStartDate(LocalDate yearStartDate) {
        this.yearStartDate = yearStartDate;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public int getPeriodYear() {
	return periodYear;
    }

    public int getPeriodNum() {
	return periodNum;
    }

    public boolean isAdjustmentPeriod() {
        return adjustmentPeriod;
    }
}
