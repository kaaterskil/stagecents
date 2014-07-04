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

import java.io.Serializable;
import java.util.Currency;

import org.axonframework.eventsourcing.annotation.AbstractAnnotatedEntity;

public class AccountBalance extends AbstractAnnotatedEntity {

    public static class Id implements Serializable {
	private LedgerId ledgerId;
	private String accountCodeId;
	private String periodId;

	Id() {
	}

	public Id(LedgerId ledgerId, String accountCodeId, String periodId) {
	    this.ledgerId = ledgerId;
	    this.accountCodeId = accountCodeId;
	    this.periodId = periodId;
	}

	@Override
	public int hashCode() {
	    return this.ledgerId.hashCode() + this.accountCodeId.hashCode()
		    + this.periodId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj == this) {
		return true;
	    }
	    if (obj != null && obj instanceof Id) {
		Id that = (Id) obj;
		return this.ledgerId.equals(that.ledgerId)
			&& this.accountCodeId.equals(that.accountCodeId)
			&& this.periodId.equals(that.periodId);
	    }
	    return false;
	}
    }

    private Id id = new Id();
    private int version;

    private Ledger ledger;
    private AccountCode accountCode;
    private Currency currency;
    private Period period;
    private int periodYear;
    private int periodNum;
    private float periodNetDebit;
    private float periodNetCredit;
    private float beginningBalanceDebit;
    private float beginningBalanceCredit;

    AccountBalance() {
    }

    public AccountBalance(Ledger ledger, AccountCode accountCode,
	    Currency currency, Period period, int periodYear, int periodNum,
	    float periodNetDebit, float periodNetCredit,
	    float beginningBalanceDebit, float beginningBalanceCredit) {

	this.ledger = ledger;
	this.accountCode = accountCode;
	this.currency = currency;
	this.period = period;
	this.periodYear = periodYear;
	this.periodNum = periodNum;
	this.periodNetDebit = periodNetDebit;
	this.periodNetCredit = periodNetCredit;
	this.beginningBalanceDebit = beginningBalanceDebit;
	this.beginningBalanceCredit = beginningBalanceCredit;

	// Set identifier
	this.id.ledgerId = ledger.getIdentifier();
	this.id.accountCodeId = accountCode.getAccountCodeId();
	this.id.periodId = period.getPeriodId();

	// Ensure referential integrity
	accountCode.getAccountBalances().put(id, this);
    }

    public Id getId() {
	return id;
    }

    void updateBalances(float debit, float credit) {
	periodNetDebit += debit;
	periodNetCredit += credit;
    }
}
