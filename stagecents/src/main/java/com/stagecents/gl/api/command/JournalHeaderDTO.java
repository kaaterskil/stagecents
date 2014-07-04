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
import java.util.Currency;
import java.util.List;

import org.joda.time.LocalDate;

import com.stagecents.gl.domain.JournalEntry;
import com.stagecents.gl.domain.JournalEntry.Status;
import com.stagecents.gl.domain.JournalEntryCategory;
import com.stagecents.gl.domain.JournalEntryId;
import com.stagecents.gl.domain.Ledger;
import com.stagecents.gl.domain.Period;

public class JournalHeaderDTO {

    private JournalEntryId journalId;
    private Ledger ledger;
    private JournalEntryCategory category;
    private Period period;
    private String name;
    private Currency currency;
    private JournalEntry.Status status;
    private LocalDate effectiveDate;
    private LocalDate postedDate;
    private String description;
    private float controlTotal;
    private float runningTotalDebit;
    private float runningTotalCredit;
    private List<JournalLineDTO> lines = new ArrayList<JournalLineDTO>();

    public JournalHeaderDTO(JournalEntryId journalId, Ledger ledger,
	    JournalEntryCategory category, Period period, String name,
	    Currency currency, Status status, LocalDate effectiveDate,
	    LocalDate postedDate, String description, float controlTotal,
	    float runningTotalDebit, float runningTotalCredit,
	    List<JournalLineDTO> lines) {
	this.journalId = journalId;
	this.ledger = ledger;
	this.category = category;
	this.period = period;
	this.name = name;
	this.currency = currency;
	this.status = status;
	this.effectiveDate = effectiveDate;
	this.postedDate = postedDate;
	this.description = description;
	this.controlTotal = controlTotal;
	this.runningTotalDebit = runningTotalDebit;
	this.runningTotalCredit = runningTotalCredit;
	this.lines = lines;
    }

    public JournalEntryId getJournalId() {
	return journalId;
    }

    public Ledger getLedger() {
	return ledger;
    }

    public JournalEntryCategory getCategory() {
	return category;
    }

    public Period getPeriod() {
	return period;
    }

    public String getName() {
	return name;
    }

    public Currency getCurrency() {
	return currency;
    }

    public JournalEntry.Status getStatus() {
	return status;
    }

    public LocalDate getEffectiveDate() {
	return effectiveDate;
    }

    public LocalDate getPostedDate() {
	return postedDate;
    }

    public String getDescription() {
	return description;
    }

    public float getControlTotal() {
	return controlTotal;
    }

    public float getRunningTotalDebit() {
	return runningTotalDebit;
    }

    public float getRunningTotalCredit() {
	return runningTotalCredit;
    }

    public List<JournalLineDTO> getLines() {
	return lines;
    }
}
