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
package com.stagecents.hr.domain;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;

import com.stagecents.gl.domain.AccountCode;

/**
 * TaxType specifies a type of tax, including whether it is a Federal;, State or
 * Local tax, and whether it is an employee or employer tax.
 * 
 * @author Blair Caple
 */
public class TaxType {

    public enum Jurisdiction {
	FEDERAL, STATE, LOCAL;
    }

    public enum Payer {
	EMPLOYEE, EMPLOYER;
    }

    private String name;
    private Jurisdiction jurisdiction;
    private Payer payer;
    private AccountCode liabilityAccount;
    private AccountCode defaultExpenseAccount;

    // Bidirectional one-to-many association.
    private SortedSet<TaxRule> taxRules = new TreeSet<TaxRule>(
	    new TaxRuleComparator());

    TaxType() {
    }

    public TaxType(String name, Jurisdiction jurisdiction, Payer payer,
	    AccountCode liabilityAccount, AccountCode defaultExpenseAccount) {
	this.name = name;
	this.jurisdiction = jurisdiction;
	this.payer = payer;
	this.liabilityAccount = liabilityAccount;
	this.defaultExpenseAccount = defaultExpenseAccount;
    }

    public String getName() {
	return name;
    }

    public Jurisdiction getJurisdiction() {
	return jurisdiction;
    }

    public Payer getPayer() {
	return payer;
    }

    public AccountCode getLiabilityAccount() {
	return liabilityAccount;
    }

    public AccountCode getDefaultExpenseAccount() {
	return defaultExpenseAccount;
    }

    public void addTaxRule(TaxRule arg) {
	arg.setTaxType(this);
	taxRules.add(arg);
    }

    public void removeTaxRule(TaxRule arg) {
	arg.setTaxType(null);
	taxRules.remove(arg);
    }

    public static class TaxRuleComparator implements Comparator<TaxRule> {

	@Override
	public int compare(TaxRule o1, TaxRule o2) {
	    DateTime s1 = o1.getStartDate();
	    DateTime s2 = o2.getStartDate();
	    return s1.isBefore(s2) ? -1 : (s1.isEqual(s2) ? 0 : 1);
	}
    }
}
