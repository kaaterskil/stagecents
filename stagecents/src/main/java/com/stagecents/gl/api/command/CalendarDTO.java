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

import java.util.ArrayList;
import java.util.List;

import com.stagecents.gl.domain.CalendarId;

public class CalendarDTO {

    private CalendarId calendarId;
    private String name;
    private String description;
    private List<PeriodDTO> periods = new ArrayList<PeriodDTO>();

    public CalendarDTO(CalendarId calendarId, String name,
	    String description, List<PeriodDTO> periods) {
	this.calendarId = calendarId;
	this.name = name;
	this.description = description;
	this.periods = periods;
    }

    public CalendarId getCalendarId() {
	return calendarId;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    public List<PeriodDTO> getPeriods() {
	return periods;
    }

    public void setPeriods(List<PeriodDTO> periods) {
        this.periods = periods;
    }
}
