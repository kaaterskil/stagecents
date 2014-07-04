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
package com.stagecents.pay.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.joda.time.LocalDate;

import com.stagecents.common.EffectiveDateInterval;
import com.stagecents.gl.domain.AccountCode;
import com.stagecents.hr.domain.Grade;
import com.stagecents.hr.domain.Party;
import com.stagecents.hr.domain.Position;

public class ElementLink {

    // Bidirectional many-to-one associations.
    private ElementType elementType;
    private Party party;
    private Grade grade;
    private Payroll payroll;

    // Unidirectional many-to-one associations.
    private AccountCode debitAccount;
    private AccountCode creditAccount;

    private EffectiveDateInterval effectiveDateRange;
    private boolean linkToAllPayrolls = false;

    private Set<ElementEntry> elementEntries = new HashSet<ElementEntry>();

    ElementLink() {
    }

    public ElementLink(ElementType elementType, Party party,
	    AccountCode debitAccount, AccountCode creditAccount,
	    boolean linkToAllPayrolls, LocalDate startDate, LocalDate endDate) {
	this(elementType, debitAccount, creditAccount, party, null, null,
		linkToAllPayrolls, startDate, endDate);

	// Create element entries
	if (party instanceof Position) {
	    Position position = (Position) party;
	    ElementEntry ee = new ElementEntry(this, startDate, endDate);
	    Iterator<InputValue> iter = elementType.getInputValues().iterator();
	    while (iter.hasNext()) {
		ElementEntryValue eev = new ElementEntryValue(ee, iter.next(),
			0);
		ee.addElementEntryValue(eev);
	    }
	    position.addElementEntry(ee);
	}
    }

    public ElementLink(ElementType elementType, Grade grade,
	    AccountCode debitAccount, AccountCode creditAccount,
	    boolean linkToAllPayrolls, LocalDate startDate, LocalDate endDate) {
	this(elementType, debitAccount, creditAccount, null, grade, null,
		linkToAllPayrolls, startDate, endDate);
    }

    public ElementLink(ElementType elementType, Payroll payroll,
	    AccountCode debitAccount, AccountCode creditAccount,
	    LocalDate startDate, LocalDate endDate) {
	this(elementType, debitAccount, creditAccount, null, null, payroll,
		false, startDate, endDate);
    }

    ElementLink(ElementType elementType, AccountCode debitAccount,
	    AccountCode creditAccount, Party party, Grade grade,
	    Payroll payroll, boolean linkToAllPayrolls, LocalDate startDate,
	    LocalDate endDate) {
	this.elementType = elementType;
	this.debitAccount = debitAccount;
	this.creditAccount = creditAccount;
	this.party = party;
	this.grade = grade;
	this.payroll = payroll;
	this.linkToAllPayrolls = linkToAllPayrolls;
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
    }

    public ElementType getElementType() {
	return elementType;
    }

    public void setElementType(ElementType elementType) {
	this.elementType = elementType;
    }

    public Party getParty() {
	return party;
    }

    public void setParty(Party party) {
	this.party = party;
    }

    public Grade getGrade() {
	return grade;
    }

    public void setGrade(Grade grade) {
	this.grade = grade;
    }

    public Payroll getPayroll() {
	return payroll;
    }

    public void setPayroll(Payroll payroll) {
	this.payroll = payroll;
    }

    public EffectiveDateInterval getEffectiveDateRange() {
	return effectiveDateRange;
    }

    public void setEffectiveDateRange(EffectiveDateInterval effectiveDateRange) {
	this.effectiveDateRange = effectiveDateRange;
    }

    public void addElementEntry(ElementEntry arg) {
	arg.setElementLink(this);
	elementEntries.add(arg);
    }

    public void removeElementEntry(ElementEntry arg) {
	arg.setElementLink(null);
	elementEntries.remove(arg);
    }

    public ElementEntry getElementEntry(LocalDate effectiveDate) {
	Iterator<ElementEntry> iter = elementEntries.iterator();
	while (iter.hasNext()) {
	    ElementEntry ee = iter.next();
	    if (effectiveDate == null || ee.isEffective(effectiveDate)) {
		return ee;
	    }
	}
	return null;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime
		* result
		+ ((effectiveDateRange == null) ? 0 : effectiveDateRange
			.hashCode());
	result = prime * result + elementType.hashCode();
	result = prime * result + ((party == null) ? 0 : party.hashCode());
	result = prime * result + ((payroll == null) ? 0 : payroll.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof ElementLink)) {
	    return false;
	}
	ElementLink other = (ElementLink) obj;
	if (!elementType.equals(other.elementType)) {
	    return false;
	}
	if (party == null) {
	    if (other.party != null) {
		return false;
	    }
	} else if (!party.equals(other.party)) {
	    return false;
	}
	if (payroll == null) {
	    if (other.payroll != null) {
		return false;
	    }
	} else if (!payroll.equals(other.payroll)) {
	    return false;
	}
	if (!effectiveDateRange.equals(other.effectiveDateRange)) {
	    return false;
	}
	return true;
    }
}
