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

import org.axonframework.eventsourcing.annotation.AbstractAnnotatedEntity;
import org.joda.time.LocalDate;

public class JournalEntryLine extends AbstractAnnotatedEntity {

    private String journalLineId;
    private int version;

    private JournalEntry journalHeader;
    private int sequence;
    private Ledger ledger;
    private AccountCode accountCode;
    private Period period;
    private LocalDate effectiveDate;
    private JournalEntry.Status status;
    private float debit;
    private float credit;
    private String description;

    JournalEntryLine() {
    }

    public JournalEntryLine(String journalLineId, JournalEntry journalHeader,
	    int sequence, AccountCode accountCode, float debit, float credit,
	    String description) {
	this.journalLineId = journalLineId;
	this.journalHeader = journalHeader;
	this.sequence = sequence;
	this.accountCode = accountCode;
	this.debit = debit;
	this.credit = credit;
	this.description = description;

	ledger = journalHeader.getLedger();
	period = journalHeader.getPeriod();
	effectiveDate = journalHeader.getEffectiveDate();
	status = journalHeader.getStatus();
    }

    public void setJournalHeader(JournalEntry journalHeader) {
	this.journalHeader = journalHeader;
    }

    public AccountCode getAccountCode() {
        return accountCode;
    }

    public float getDebit() {
        return debit;
    }

    public float getCredit() {
        return credit;
    }
}
