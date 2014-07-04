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
import java.util.Iterator;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import com.stagecents.gl.api.command.LedgerCreatedEvent;
import com.stagecents.gl.api.command.LedgerDTO;

public class Ledger extends AbstractAnnotatedAggregateRoot<LedgerId> {

    @AggregateIdentifier
    private LedgerId ledgerId;
    private int version;

    private String name;
    private String shortName;
    private String description;
    private Currency currency;
    private CalendarId calendarId;
    private AccountCode retainedEarningsAccountCode;
    private AccountCode netIncomeAccountCode;
    private StructureId structureId;

    Ledger() {
    }

    public Ledger(LedgerDTO data) {
	apply(new LedgerCreatedEvent(data.getLedgerId(), data));
    }

    @Override
    public LedgerId getIdentifier() {
	return ledgerId;
    }

    @EventHandler
    public void handleCreateLedger(LedgerCreatedEvent event) {
	// Create ledger
	ledgerId = event.getLedgerId();
	name = event.getLedgerDTO().getName();
	shortName = event.getLedgerDTO().getShortName();
	description = event.getLedgerDTO().getDescription();
	currency = event.getLedgerDTO().getCurrency();
	calendarId = event.getLedgerDTO().getCalendar().getIdentifier();
	retainedEarningsAccountCode = event.getLedgerDTO()
		.getRetainedEarningsAccountCode();
	netIncomeAccountCode = event.getLedgerDTO().getNetIncomeAccountCode();
	structureId = event.getLedgerDTO().getStructure().getIdentifier();

	// Create AccountBalance entities
	Structure structure = event.getLedgerDTO().getStructure();
	Calendar calendar = event.getLedgerDTO().getCalendar();
	createAccountBalances(structure, calendar);
    }

    private void createAccountBalances(Structure structure, Calendar calendar) {
	Iterator<AccountCode> iter1 = structure.getChartOfAccounts().iterator();
	while (iter1.hasNext()) {
	    AccountCode accountCode = iter1.next();

	    Iterator<Period> iter2 = calendar.getPeriods().iterator();
	    while (iter2.hasNext()) {
		Period period = iter2.next();

		AccountBalance accountBalance = new AccountBalance(this,
			accountCode, currency, period, period.getPeriodYear(),
			period.getPeriodNum(), 0, 0, 0, 0);
		accountCode.addAccountBalance(accountBalance);
	    }
	}

    }
}
