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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.axonframework.eventsourcing.annotation.AbstractAnnotatedEntity;

public class AccountCode extends AbstractAnnotatedEntity {

    private String accountCodeId;
    private int version;

    private Structure structure;
    private AccountType accountType;
    private String value;
    private String name;
    private transient List<String> segmentValues = new ArrayList<String>();
    private Map<AccountBalance.Id, AccountBalance> accountBalances = new HashMap<AccountBalance.Id, AccountBalance>();

    AccountCode() {
    }

    public AccountCode(String accountCodeId, Structure structure,
	    AccountType accountType, String name, String value,
	    List<String> segmentValues) {
	this.accountCodeId = accountCodeId;
	this.structure = structure;
	this.accountType = accountType;
	this.name = name;
	this.segmentValues = segmentValues;

	if (value == null) {
	    value = concatenateSegmentValues(segmentValues);
	}
	this.value = value;
    }

    public String getAccountCodeId() {
	return accountCodeId;
    }

    public String getValue() {
	return value;
    }

    public Map<AccountBalance.Id, AccountBalance> getAccountBalances() {
	return accountBalances;
    }
    
    void addAccountBalance(AccountBalance accountBalance) {
	accountBalances.put(accountBalance.getId(), accountBalance);
    }

    private String concatenateSegmentValues(List<String> segmentValues) {
	StringBuffer sb = new StringBuffer();
	Iterator<String> iter = segmentValues.iterator();
	while (iter.hasNext()) {
	    sb.append(iter.next());
	    if (iter.hasNext()) {
		sb.append("-");
	    }
	}
	return sb.toString();
    }

    public static boolean validateCodeCombination(String code,
	    Structure structure) {
	if (structure.getSegments().isEmpty()) {
	    return false;
	}
	// Reformat any delimiters
	code = code.replaceAll("-", "");

	// Loop through each segment
	int pos = 0;
	Iterator<Segment> iter = structure.getSegments().iterator();
	while (iter.hasNext()) {
	    Segment s = iter.next();
	    int len = s.getLength();
	    String subject = code.substring(pos, pos + len);

	    // Loop through the values for each segment. Return false if the
	    // code segment does not match any values in the segment.
	    boolean isMatch = false;
	    Iterator<SegmentValue> it = s.getSegmentValues().iterator();
	    while (it.hasNext()) {
		SegmentValue sv = it.next();
		if (sv.getValue().equals(subject)) {
		    isMatch = true;
		    break;
		}
	    }
	    if (!isMatch) {
		return false;
	    }
	    pos += len;
	}
	return true;
    }

    @Override
    public int hashCode() {
	return accountCodeId.hashCode() + accountType.hashCode()
		+ value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj != null && obj instanceof AccountCode) {
	    AccountCode that = (AccountCode) obj;
	    return accountCodeId.equals(that.accountCodeId)
		    && accountType.equals(that.accountType)
		    && value.equals(that.value);

	}
	return false;
    }
}
