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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.stagecents.common.DateEffective;
import com.stagecents.common.EffectiveDateInterval;
import com.stagecents.hr.domain.Party;
import com.stagecents.hr.domain.Position;
import com.stagecents.hxt.domain.SummaryHours;
import com.stagecents.hxt.domain.Timecard;
import com.stagecents.pa.domain.Activity;

/**
 * Represents the date effective definitions of the units used to build all
 * earnings, deductions and benefits that users can give to positions and
 * employees. Elements are also used to hold non-payment types of information
 * linked to employee assignments, for example, about assets and other equipment
 * issued to employees for their work.
 * 
 * @author Blair Caple
 */
public class ElementType implements DateEffective {

    public enum Category {
	OSP("Day of Week Premium"), OVT("Overtime Earning"), REG(
		"Regular Earning"), SDF("Shift Differential Premium");
	private String meaning;

	private Category(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    /**
     * Enumeration to provide discriminator values for extended classes.
     */
    public enum Type {
	GENERIC("Other Earning", Category.REG), GTL("Group Term Life",
		Category.REG), OVERTIME("Overtime", Category.OVT), PERF_FEE(
		"Performance Fee", Category.REG), PERF_WAGE("Performance Wage",
		Category.REG), REG_SALARY("Regular Salary", Category.REG), REG_WAGE(
		"Regular Wage", Category.REG), SHIFT("Shift", Category.SDF);
	private String meaning;
	private Category category;

	private Type(String meaning, Category category) {
	    this.meaning = meaning;
	    this.category = category;
	}

	public String getMeaning() {
	    return meaning;
	}

	public Category getCategory() {
	    return category;
	}
    }

    public enum Classification {
	BENEFITS("Employer Benefits", 60, "Employer  Liabilities", true), BONUS(
		"Bonus", 25, "Supplemental", true), ER_FED_TAX(
		"Employer Federal Tax", 40, "Employer Tax", true), ER_STATE_TAX(
		"Employer State Tax", 40, "Employer Tax", true), ER_LOCAL_TAX(
		"Employer Local Tax", 40, "Employer Tax", true), GTL(
		"Group Term Life", 30, "Imputed Earnings", true), LABOR_HRS(
		"Labor Hours", 0, "Information", false), OT_EARNINGS(
		"Overtime Earnings", 15, "Earnings", true), OT_HOURS(
		"Overtime Hours", 0, "Information", false), PENSION("Pension",
		25, "Supplemental", true), REG_EARNINGS("Regular Earnings", 15,
		"Earnings", true), REG_HOURS("Regular Hours", 0, "Information",
		false), SHIFT("Shift Pay", 15, "Earnings", true);
	private String name;
	private String type;
	private int priority;
	private boolean costable;

	private Classification(String name, int priority, String category,
		boolean costable) {
	    this.name = name;
	    this.priority = priority;
	    this.type = category;
	    this.costable = costable;
	}

	public String getName() {
	    return name;
	}

	public String getType() {
	    return type;
	}

	public boolean isCostable() {
	    return costable;
	}
    }

    public enum ProcessingType {
	RECURRING, NON_RECURRING
    }

    protected EffectiveDateInterval effectiveDateRange;
    protected Type type;
    protected Classification classification;
    protected ProcessingType processingType;
    protected String name;
    protected String description;
    protected int sequence;

    // Bidirectional one-to-many associations
    protected SortedSet<InputValue> inputValues = new TreeSet<InputValue>(
	    new InputValueComparator());
    protected Set<ElementLink> links = new HashSet<ElementLink>();

    ElementType() {
    }

    public ElementType(String name, String description, Type type,
	    Classification classification, ProcessingType processingType,
	    LocalDate startDate, LocalDate endDate) {
	this.name = name;
	this.description = description;
	this.type = type;
	this.classification = classification;
	this.processingType = processingType;
	this.effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
    }

    public EffectiveDateInterval getEffectiveDateRange() {
	return effectiveDateRange;
    }

    public int getSequence() {
	return sequence;
    }

    public SortedSet<InputValue> getInputValues() {
	return inputValues;
    }

    public List<InputValue> getValues(LocalDate effectiveDate) {
	List<InputValue> result = new ArrayList<InputValue>();
	Iterator<InputValue> iter = inputValues.iterator();
	while (iter.hasNext()) {
	    InputValue each = iter.next();
	    if (each.isEffective(effectiveDate)) {
		result.add(each);
	    }
	}
	return result;
    }

    public void addInputValue(InputValue arg) {
	arg.setElementType(this);
	inputValues.add(arg);
    }

    public void removeInputValue(InputValue arg) {
	arg.setElementType(null);
	inputValues.remove(arg);
    }

    public void linkElement(ElementLink arg) {
	arg.setElementType(this);
	links.add(arg);
    }

    public void unlinkElement(ElementLink arg) {
	arg.setElementType(null);
	links.remove(arg);
    }

    public List<Position> getPositions() {
	List<Position> result = new ArrayList<Position>();
	Iterator<ElementLink> iter = links.iterator();
	while (iter.hasNext()) {
	    Party party = iter.next().getParty();
	    if (party != null && party instanceof Position) {
		result.add((Position) party);
	    }
	}
	return result;
    }

    /**
     * Returns the decimal hours from the given time interval, adjusted by any
     * restricted hours specified by the given position and any regular hours
     * already accumulated in the given timecard.
     * 
     * This method does nothing unless extended in a subclass.
     * 
     * @param interval
     *            The time period of the activity, adjusted to a single day.
     * @param activity
     *            The activity from which to compute regular hours.
     * @param timecard
     *            The timecard from which to accumulate prior regular hours.
     * @param position
     *            The position which participates in the given time interval and
     *            which may define restricted or maximum hours.
     */
    public void processHours(Interval interval, Activity activity,
	    Timecard timecard, Position position) {
	// Noop
    }

    /**
     * Returns the total hours from the given time card for this element type
     * prior to the given start date. THe method will return 0 if no prior hours
     * have been recorded for this element type.
     * 
     * @param timecard
     *            The time card from which to sum prior hours.
     * @param start
     *            The cutoff date.
     * @return The total hours recorded on the given time card for this element
     *         type prior to the given start date, or 0 if no prior hours have
     *         been recorded.
     */
    float getPriorHours(Timecard timecard, LocalDate start) {
	float result = 0F;
	Iterator<SummaryHours> iter = timecard.getSummaryHours().iterator();
	while (iter.hasNext()) {
	    SummaryHours sh = iter.next();
	    if (sh.getDateWorked().isBefore(start)) {
		result += sh.getHours(this);
	    }
	}
	return result;
    }

    MultiplierValue getMultiplier(Interval interval) {
	Iterator<InputValue> iter = inputValues.iterator();
	while (iter.hasNext()) {
	    InputValue iv = iter.next();
	    if (iv instanceof MultiplierValue) {
		MultiplierValue mv = (MultiplierValue) iv;
		if (mv.getEffectiveDateRange() == null
			|| mv.isEffective(interval.getStart())) {
		    return mv;
		}
	    }
	}
	return null;
    }

    MinimumCallValue getMinimumCall(Interval interval) {
	Iterator<InputValue> iter = inputValues.iterator();
	while (iter.hasNext()) {
	    InputValue iv = iter.next();
	    if (iv instanceof MinimumCallValue) {
		MinimumCallValue mcv = (MinimumCallValue) iv;
		if (mcv.getEffectiveDateRange() == null
			|| mcv.isEffective(interval.getStart())) {
		    return mcv;
		}
	    }
	}
	return null;
    }

    void updateTimecard(Timecard timecard, Activity activity,
	    Interval timeWorked) {
	LocalDate dateWorked = timeWorked.getStart().toLocalDate();

	SummaryHours sh = timecard.getSummaryHours(dateWorked);
	if (sh == null) {
	    int seq = timecard.getSummaryHoursSequence(dateWorked);
	    sh = new SummaryHours(timecard, seq, dateWorked);
	}
	sh.updateHours(activity, this, timeWorked);
    }

    @Override
    public boolean isEffective(DateTime effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }

    @Override
    public boolean isEffective(LocalDate effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((classification == null) ? 0 : classification.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result
		+ ((processingType == null) ? 0 : processingType.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof ElementType)) {
	    return false;
	}
	ElementType other = (ElementType) obj;
	if (classification != other.classification) {
	    return false;
	}
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	if (processingType != other.processingType) {
	    return false;
	}
	return true;
    }

    public static class InputValueComparator implements Comparator<InputValue> {

	@Override
	public int compare(InputValue o1, InputValue o2) {
	    int s1 = o1.getSequence();
	    int s2 = o2.getSequence();
	    if (s1 < s2) {
		return -1;
	    } else if (s1 == s2) {
		DateTime d1 = o1.getStartDate();
		DateTime d2 = o2.getStartDate();
		return d1.isBefore(d2) ? -1 : (d1.isEqual(d2) ? 0 : 1);
	    }
	    return 1;
	}
    }
}
