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
package com.stagecents.gl.api.service;

import javax.annotation.Resource;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import com.stagecents.common.ValidationException;
import com.stagecents.gl.api.command.CreateCalendarCommand;
import com.stagecents.gl.api.command.PeriodDTO;
import com.stagecents.gl.domain.Calendar;
import com.stagecents.gl.domain.CalendarId;
import com.stagecents.gl.query.repository.CalendarRepository;

@Component
public class CalendarCommandHandler implements CalendarService {

    private Repository<Calendar> repository;
    private CalendarRepository calendarRepository;

    @Resource(name = "calendarJpaRepository")
    public void setRepository(Repository<Calendar> repository) {
	this.repository = repository;
    }

    @Resource(name = "calendarQueryRepository")
    public void setCalendarRepository(CalendarRepository calendarRepository) {
	this.calendarRepository = calendarRepository;
    }

    @CommandHandler
    public void createCalendar(CreateCalendarCommand command) {
	// Calendar validation
	checkUniqueName(command.getCalendarId(), command.getCalendatDTO()
		.getName());

	// Period validation
	PeriodDTO[] a = new PeriodDTO[command.getCalendatDTO().getPeriods()
		.size()];
	PeriodDTO[] periods = command.getCalendatDTO().getPeriods().toArray(a);
	LocalDate yearStartDate = getYearStartDate(periods);
	for (int i = 0; i < periods.length; i++) {
	    // 1. Verify that the period number is unique within the calendar.
	    checkUniquePeriodNumber(periods, i);

	    // 2. Period number range check.
	    checkPeriodNumRange(periods[i]);

	    // 3. If this is not an adjusting period, then verify that this
	    // period does not overlap with any other periods.
	    if (!(periods[i].isAdjustmentPeriod())
		    && overlappingPeriod(periods, i)) {
		throw new ValidationException("period date overlap");
	    }

	    // 4. Check for gaps
	    checkCalendarGap(periods);

	    // 5. Set year start date
	    command.getCalendatDTO().getPeriods().get(i)
		    .setYearStartDate(yearStartDate);
	}

	Calendar calendar = new Calendar(command.getCalendatDTO());
	repository.add(calendar);
    }

    private void checkUniqueName(CalendarId calendarId, String name) {
	boolean isUnique = calendarRepository.checkUnique(calendarId, name);
	if (!isUnique) {
	    throw new ValidationException("duplicate calendar name");
	}
    }

    private void checkUniquePeriodNumber(PeriodDTO[] periods, int index) {
	PeriodDTO period = periods[index];
	for (int i = 0; i < periods.length; i++) {
	    if ((i != index)
		    && (periods[i].getPeriodNum() == period.getPeriodNum())) {
		throw new ValidationException("duplicate period number");
	    }
	}
    }

    private void checkPeriodNumRange(PeriodDTO period) {
	if (period.getPeriodNum() < 1
		|| period.getPeriodNum() > period.getPeriodType()
			.getNumPeriods()) {
	    throw new IllegalArgumentException("period number out of bounds");
	}
    }

    /**
     * Returns true if the given period start and end dates overlap with some
     * other period that is not an adjustment period.
     * 
     * @param periods The array of periods that make up the calendar.
     * @param index The index number of the given period within the array.
     * @return True if the given period start and end dates overlap with some
     *         other period that is not an adjustment period.
     */
    private boolean overlappingPeriod(PeriodDTO[] periods, int index) {
	LocalDate startDate = periods[index].getStartDate();
	LocalDate endDate = periods[index].getEndDate();
	for (int i = 0; i < periods.length; i++) {
	    if (i != index && !(periods[i].isAdjustmentPeriod())) {
		if (startDate.isBefore(periods[i].getStartDate())
			&& endDate.isAfter(periods[i].getEndDate())) {
		    return true;
		}
	    }
	}
	return false;
    }

    private LocalDate getYearStartDate(PeriodDTO[] periods) {
	LocalDate result = new LocalDate(Long.MAX_VALUE);
	for (int i = 0; i < periods.length; i++) {
	    LocalDate startDate = periods[i].getStartDate();
	    if (startDate.isBefore(result)) {
		result = startDate;
	    }
	}
	return result;
    }

    private void checkCalendarGap(PeriodDTO[] periods) {
	boolean gapExists = false;
	LocalDate end = periods[0].getEndDate();
	for (int i = 1; i < periods.length; i++) {
	    LocalDate start = periods[i].getStartDate();
	    if (!(end.plusDays(1).isEqual(start))
		    && !(periods[i].isAdjustmentPeriod())) {
		gapExists = true;
		break;
	    }
	    end = periods[i].getEndDate();
	}
	if (gapExists) {
	    throw new ValidationException("gap found in calendar periods");
	}
    }
}
