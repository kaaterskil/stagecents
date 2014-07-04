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

import java.util.Currency;

import com.stagecents.gl.domain.AccountCode;
import com.stagecents.gl.domain.Calendar;
import com.stagecents.gl.domain.LedgerId;
import com.stagecents.gl.domain.Structure;

public class LedgerDTO {

    private LedgerId ledgerId;
    private String name;
    private String shortName;
    private String description;
    private Currency currency;
    private Calendar calendar;
    private AccountCode retainedEarningsAccountCode;
    private AccountCode netIncomeAccountCode;
    private Structure structure;

    public LedgerDTO(LedgerId ledgerId, String name, String shortName,
	    String description, Currency currency, Calendar calendar,
	    AccountCode retainedEarningsAccountCode,
	    AccountCode netIncomeAccountCode, Structure structure) {
	this.ledgerId = ledgerId;
	this.name = name;
	this.shortName = shortName;
	this.description = description;
	this.currency = currency;
	this.calendar = calendar;
	this.retainedEarningsAccountCode = retainedEarningsAccountCode;
	this.netIncomeAccountCode = netIncomeAccountCode;
	this.structure = structure;
    }

    public LedgerId getLedgerId() {
	return ledgerId;
    }

    public String getName() {
	return name;
    }

    public String getShortName() {
	return shortName;
    }

    public String getDescription() {
	return description;
    }

    public Currency getCurrency() {
	return currency;
    }

    public Calendar getCalendar() {
	return calendar;
    }

    public AccountCode getRetainedEarningsAccountCode() {
	return retainedEarningsAccountCode;
    }

    public AccountCode getNetIncomeAccountCode() {
	return netIncomeAccountCode;
    }

    public Structure getStructure() {
	return structure;
    }
}
