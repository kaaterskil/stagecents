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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.LocalDate;

import com.stagecents.pay.domain.ElementEntry;
import com.stagecents.pay.domain.ElementLink;
import com.stagecents.pay.domain.ElementType;

public abstract class Party {

    public enum PartyType {
	ORGANIZATION, JOB, POSITION, PERSON;
    }

    private String name;
    private PartyType partyType;

    // Bidirectional one-to-many associations.
    private Set<Accountability> parentAccountabilities = new HashSet<Accountability>();
    private Set<Accountability> childAccountabilities = new HashSet<Accountability>();

    protected SortedSet<ElementLink> elements = new TreeSet<ElementLink>(
	    new ElementLinkComparator());

    Party() {
    }

    protected Party(String name, PartyType partyType) {
	this.name = name;
	this.partyType = partyType;
    }

    public String getName() {
	return name;
    }

    void addChildAccountability(Accountability arg) {
	childAccountabilities.add(arg);
    }

    void addParentAccountability(Accountability arg) {
	parentAccountabilities.add(arg);
    }

    Set<Party> parents() {
	Set<Party> result = new HashSet<Party>();
	Iterator<Accountability> iter = parentAccountabilities.iterator();
	while (iter.hasNext()) {
	    Accountability each = iter.next();
	    result.add(each.getParent());
	}
	return result;
    }

    Set<Party> parents(AccountabilityType type) {
	Set<Party> result = new HashSet<Party>();
	Iterator<Accountability> iter = parentAccountabilities.iterator();
	while (iter.hasNext()) {
	    Accountability each = iter.next();
	    if (each.getType().equals(type)) {
		result.add(each.getParent());
	    }
	}
	return result;
    }

    Set<Party> children() {
	Set<Party> result = new HashSet<Party>();
	Iterator<Accountability> iter = childAccountabilities.iterator();
	while (iter.hasNext()) {
	    Accountability each = iter.next();
	    result.add(each.getChild());
	}
	return result;
    }

    Set<Party> children(AccountabilityType type) {
	Set<Party> result = new HashSet<Party>();
	Iterator<Accountability> iter = childAccountabilities.iterator();
	while (iter.hasNext()) {
	    Accountability each = iter.next();
	    if (each.getType().equals(type)) {
		result.add(each.getChild());
	    }
	}
	return result;
    }

    boolean ancestorsInclude(Party sample, AccountabilityType type) {
	Iterator<Party> iter = parents(type).iterator();
	while (iter.hasNext()) {
	    Party eachParent = iter.next();
	    if (eachParent.equals(sample)) {
		return true;
	    }
	    if (eachParent.ancestorsInclude(sample, type)) {
		return true;
	    }
	}
	return false;
    }

    public void linkElement(ElementLink arg) {
	arg.setParty(this);
	elements.add(arg);
    }

    public void unlinkElement(ElementLink arg) {
	arg.setParty(null);
	elements.remove(arg);
    }

    public List<ElementType> getElements() {
	List<ElementType> result = new ArrayList<ElementType>();
	Iterator<ElementLink> iter = elements.iterator();
	while (iter.hasNext()) {
	    result.add(iter.next().getElementType());
	}
	return result;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result
		+ ((partyType == null) ? 0 : partyType.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof Party)) {
	    return false;
	}
	Party other = (Party) obj;
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	if (partyType != other.partyType) {
	    return false;
	}
	return true;
    }

    public static class ElementLinkComparator implements
	    Comparator<ElementLink> {

	@Override
	public int compare(ElementLink o1, ElementLink o2) {
	    int s1 = o1.getElementType().getSequence();
	    int s2 = o2.getElementType().getSequence();
	    return (s1 < s2) ? -1 : ((s1 == s2) ? 0 : 1);
	}

    }
}
