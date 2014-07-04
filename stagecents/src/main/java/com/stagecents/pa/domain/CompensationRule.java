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
import org.joda.time.LocalDate;

import com.stagecents.common.DateEffective;
import com.stagecents.common.EffectiveDateInterval;

/**
 * Represents the implementation-defined classifications used to describe how
 * employees are compensated.
 * 
 * @author Blair Caple
 */
public class CompensationRule implements DateEffective {

    public enum CostingMethod {
	STANDARD_COSTING, ACTUAL_COSTING;
    }

    private String name;
    private String description;
    private EffectiveDateInterval effectiveDateRange;
    private ExpenditureType overtimeExpenditureType;

    private Project project;
    private Activity activity;
    private CostingMethod method = CostingMethod.ACTUAL_COSTING;

    // Bidirectional one-to-many association.
    private SortedSet<CompensationDetail> compensationDetails = new TreeSet<CompensationDetail>(
	    new CompensationDetailComparator());
    // Unidirectional many-to-many association. Join table is named
    // PA_COMP_OT_EXP_TYPES.
    private SortedSet<LaborCostMultiplier> laborCostMultipliers = new TreeSet<LaborCostMultiplier>(
	    new LaborCostMultiplierComparator());

    CompensationRule() {
    }

    public CompensationRule(String name, String description,
	    ExpenditureType overtimeExpenditureType, Project project,
	    Activity activity, CostingMethod method, LocalDate startDate,
	    LocalDate endDate) {
	this.name = name;
	this.description = description;
	this.overtimeExpenditureType = overtimeExpenditureType;
	this.project = project;
	this.activity = activity;
	this.method = method;
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
    }

    public float getHourlyRate(DateTime effectiveDate) {
	CompensationDetail cd = getCompensationDetail(effectiveDate);
	LaborCostMultiplier lcm = getMultiplier(effectiveDate);
	float rawCostRate = (cd == null) ? 0 : cd.getHourlyCostRate();
	float multiplier = (lcm == null) ? 1 : lcm.getMultiplier();
	return rawCostRate * multiplier;
    }

    CompensationDetail getCompensationDetail(DateTime effectiveDate) {
	Iterator<CompensationDetail> iter = compensationDetails.iterator();
	while (iter.hasNext()) {
	    CompensationDetail each = iter.next();
	    if (each.isEffective(effectiveDate)) {
		return each;
	    }
	}
	return null;
    }

    LaborCostMultiplier getMultiplier(DateTime effectiveDate) {
	Iterator<LaborCostMultiplier> iter = laborCostMultipliers.iterator();
	while (iter.hasNext()) {
	    LaborCostMultiplier each = iter.next();
	    if (each.isEffective(effectiveDate)) {
		return each;
	    }
	}
	return null;
    }

    public void addCompensationDetail(CompensationDetail arg) {
	arg.setCompensationRule(this);
	compensationDetails.add(arg);
    }

    public void removeCompensationDetail(CompensationDetail arg) {
	arg.setCompensationRule(null);
	compensationDetails.remove(arg);
    }

    public void addLaborCostMultiplier(LaborCostMultiplier arg) {
	laborCostMultipliers.add(arg);
    }

    @Override
    public boolean isEffective(DateTime effectiveDate) {
	return (effectiveDateRange == null) ? true : effectiveDateRange
		.isEffective(effectiveDate);
    }

    @Override
    public boolean isEffective(LocalDate effectiveDate) {
	return (effectiveDateRange == null) ? true : effectiveDateRange
		.isEffective(effectiveDate);
    }

    public static class CompensationDetailComparator implements
    Comparator<CompensationDetail> {

	@Override
	public int compare(CompensationDetail o1, CompensationDetail o2) {
	    DateTime d1 = o1.getStartDate();
	    DateTime d2 = o2.getStartDate();
	    return d1.isBefore(d2) ? -1 : (d1.isEqual(d2) ? 0 : 1);
	}

    }

    public static class LaborCostMultiplierComparator implements
    Comparator<LaborCostMultiplier> {

	@Override
	public int compare(LaborCostMultiplier o1, LaborCostMultiplier o2) {
	    DateTime d1 = o1.getStartDate();
	    DateTime d2 = o2.getStartDate();
	    return d1.isBefore(d2) ? -1 : (d1.isEqual(d2) ? 0 : 1);
	}

    }
}
