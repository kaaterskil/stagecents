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

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.stagecents.hr.domain.Party;

/**
 * Represents a grouping of expenditure items incurred by a party for a specific
 * expenditure period.
 * 
 * @author Blair Caple
 */
public class Expenditure {

    public enum ExpenditureStatus {
	APPROVED, WORKING;
    }

    private ExpenditureStatus status;
    private LocalDate date;
    private Party incurringParty;
    private float controlTotal;
    private String description;
    private List<ExpenditureItem> expenditureItems = new ArrayList<ExpenditureItem>();

    Expenditure() {
    }

    public Expenditure(ExpenditureStatus status, LocalDate date,
	    Party incurringParty, String description) {
	this.status = status;
	this.date = date;
	this.incurringParty = incurringParty;
	this.description = description;
    }

    public void addExpeditureItem(ExpenditureItem arg) {
	arg.setExpenditure(this);
	expenditureItems.add(arg);
    }

    public void removeExpenditureItem(ExpenditureItem arg) {
	arg.setExpenditure(null);
	expenditureItems.remove(arg);
    }
}
