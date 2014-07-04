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

import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.joda.time.LocalDate;

import com.stagecents.gl.api.command.DeleteJournalEntryCommand;
import com.stagecents.gl.api.command.JournalEntryCreatedEvent;
import com.stagecents.gl.api.command.JournalEntryDeletedEvent;
import com.stagecents.gl.api.command.JournalEntryPostedEvent;
import com.stagecents.gl.api.command.JournalHeaderDTO;
import com.stagecents.gl.api.command.JournalLineDTO;
import com.stagecents.gl.api.command.PostJournalEntryCommand;

public class JournalEntry extends AbstractAnnotatedAggregateRoot<JournalEntryId> {

    private JournalEntryId journalId;
    private int version;

    private Ledger ledger;
    private JournalEntryCategory category;
    private Period period;
    private String name;
    private Currency currency;
    private Status status;
    private LocalDate effectiveDate;
    private LocalDate postedDate;
    private String description;
    private float controlTotal;
    private float runningTotalDebit;
    private float runningTotalCredit;
    private Set<JournalEntryLine> journalEntryLines = new HashSet<JournalEntryLine>();

    public enum Status {
	UNPOSTED("Unposted"), POSTED("Posted");
	private String meaning;

	private Status(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    JournalEntry() {
    }

    public JournalEntry(JournalHeaderDTO data) {
	apply(new JournalEntryCreatedEvent(data.getJournalId(), data));
    }

    public Ledger getLedger() {
	return ledger;
    }

    public Period getPeriod() {
	return period;
    }

    public Status getStatus() {
	return status;
    }

    public LocalDate getEffectiveDate() {
	return effectiveDate;
    }

    public Set<JournalEntryLine> getJournalEntryLines() {
	return journalEntryLines;
    }

    public void addJournalEntryLine(JournalEntryLine journalEntryLine) {
	journalEntryLine.setJournalHeader(this);
	journalEntryLines.add(journalEntryLine);
    }

    public void removeJournalEntryLine(JournalEntryLine journalEntryLine) {
	journalEntryLine.setJournalHeader(null);
	journalEntryLines.remove(journalEntryLine);
    }

    @EventHandler
    public void handleCreateJournal(JournalEntryCreatedEvent event) {
	journalId = event.getJournalId();
	ledger = event.getJournalHeaderDTO().getLedger();
	category = event.getJournalHeaderDTO().getCategory();
	period = event.getJournalHeaderDTO().getPeriod();
	name = event.getJournalHeaderDTO().getName();
	currency = event.getJournalHeaderDTO().getCurrency();
	status = event.getJournalHeaderDTO().getStatus();
	effectiveDate = event.getJournalHeaderDTO().getEffectiveDate();
	postedDate = event.getJournalHeaderDTO().getPostedDate();
	description = event.getJournalHeaderDTO().getDescription();
	controlTotal = event.getJournalHeaderDTO().getControlTotal();
	runningTotalDebit = event.getJournalHeaderDTO().getRunningTotalDebit();
	runningTotalCredit = event.getJournalHeaderDTO()
		.getRunningTotalCredit();

	Iterator<JournalLineDTO> iter = event.getJournalHeaderDTO().getLines()
		.iterator();
	int sequence = 0;
	while (iter.hasNext()) {
	    JournalLineDTO data = iter.next();
	    JournalEntryLine line = new JournalEntryLine(
		    data.getJournalLineId(), this, sequence,
		    data.getAccountCode(), data.getDebit(), data.getCredit(),
		    data.getDescription());
	    addJournalEntryLine(line);
	    sequence++;
	}
    }

    @EventHandler
    public void handleDeleteJournal(JournalEntryDeletedEvent event) {
	Iterator<JournalEntryLine> iter = journalEntryLines.iterator();
	while (iter.hasNext()) {
	    removeJournalEntryLine(iter.next());
	}
	journalEntryLines = new HashSet<JournalEntryLine>();
	markDeleted();
    }

    @EventHandler
    public void handlePost(JournalEntryPostedEvent event) {
	// Update the account balances
	Iterator<JournalEntryLine> iter = journalEntryLines.iterator();
	while (iter.hasNext()) {
	    JournalEntryLine line = iter.next();
	    AccountCode accountCode = line.getAccountCode();
	    AccountBalance.Id key = new AccountBalance.Id(
		    ledger.getIdentifier(), accountCode.getAccountCodeId(),
		    period.getPeriodId());
	    AccountBalance balance = accountCode.getAccountBalances().get(key);
	    balance.updateBalances(line.getDebit(), line.getCredit());
	}

	// Set status
	status = Status.POSTED;
    }

    @CommandHandler
    public void delete(DeleteJournalEntryCommand command) {
	if (status.equals(Status.POSTED)) {
	    throw new IllegalArgumentException(
		    "postaed entry cannot be deleted");
	}
	apply(new JournalEntryDeletedEvent(journalId));
    }

    @CommandHandler
    public void post(PostJournalEntryCommand command) {
	if (status.equals(Status.POSTED)) {
	    throw new IllegalArgumentException("entry is already posted");
	}
	checkTotals();
	apply(new JournalEntryPostedEvent(journalId));
    }

    private void checkTotals() {
	float runningTotalDebit = 0;
	float runningTotalCredit = 0;
	Iterator<JournalEntryLine> iter = journalEntryLines.iterator();
	while (iter.hasNext()) {
	    JournalEntryLine line = iter.next();
	    runningTotalDebit += line.getDebit();
	    runningTotalCredit += line.getCredit();
	}

	if (runningTotalDebit != runningTotalCredit) {
	    throw new IllegalArgumentException("entry out of balance");
	}

	this.runningTotalDebit = runningTotalDebit;
	this.runningTotalCredit = runningTotalCredit;
	controlTotal = runningTotalDebit;
    }
}
