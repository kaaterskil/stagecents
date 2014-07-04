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
package com.stagecents.pa.domain;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;

/**
 * Represents the most detailed implementation-defined classifications of
 * expenditures charged to projects and tasks.
 * 
 * @author Blair Caple
 */
public class ExpenditureType {

    private String name;
    // Unidirectional many-to-one association.
    private ExpenditureCategory category;
    private SystemLinkage systemLinkage;
    private Unit uom;
    private boolean requiresCostRate;
    private String description;

    // Bidirectional one-to-many association to costs rates for non-labor
    // resources.
    private SortedSet<CostRate> costRates = new TreeSet<CostRate>(
	    new CostRateComparator());

    ExpenditureType() {
    }

    public ExpenditureType(String name, ExpenditureCategory category,
	    SystemLinkage systemLinkage, Unit uom, boolean requiresCostRate,
	    String description) {
	this.name = name;
	this.category = category;
	this.systemLinkage = systemLinkage;
	this.uom = uom;
	this.requiresCostRate = requiresCostRate;
	this.description = description;
    }

    public SystemLinkage getSystemLinkage() {
        return systemLinkage;
    }

    public CostRate getCostRate(DateTime effectiveDate) {
	Iterator<CostRate> iter = costRates.iterator();
	while (iter.hasNext()) {
	    CostRate each = iter.next();
	    if (each.isEffective(effectiveDate)) {
		return each;
	    }
	}
	return null;
    }

    void addCostRate(CostRate arg) {
	if (systemLinkage.equals(SystemLinkage.STRAIGHT_TIME)
		|| systemLinkage.equals(SystemLinkage.OVERTIME)) {
	    throw new IllegalArgumentException("invalid cost rate");
	}
	arg.setExpenditureType(this);
	costRates.add(arg);
    }

    void removeCostRate(CostRate arg) {
	arg.setExpenditureType(null);
	costRates.remove(arg);
    }

    public static class CostRateComparator implements Comparator<CostRate> {

	@Override
	public int compare(CostRate o1, CostRate o2) {
	    DateTime d1 = o1.getStartDate();
	    DateTime d2 = o2.getStartDate();
	    return d1.isBefore(d2) ? -1 : (d1.isEqual(d2) ? 0 : 1);
	}

    }
}
