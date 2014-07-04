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

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.stagecents.common.DateEffective;
import com.stagecents.common.EffectiveDateInterval;
import com.stagecents.hr.domain.Unit;

/**
 * Represents the date effective definitions of the input values associated eith
 * a specific element.
 * 
 * @author Blair Caple
 */
public abstract class InputValue implements DateEffective {

    public enum Type {
	AMOUNT("Amount"), COVERAGE_MULTIPLE("Coverage: Multiplier"), COVERAGE_AMT(
		"Coverage: Amount"), DRESS_CALL("Dress Call Hours"), HOURS(
		"Hours"), IMPUTED_AMT("Imputed Amount"), MINIMUM_CALL(
		"Minimum Call Hours"), MONTHLY_SALARY("Monthly Salary"), MULTIPLE(
		"Multiplier"), OT_MULTIPLE("Overtime Multiplier"), PAY_VALUE_HRS(
		"Value: Hours"), PAY_VALUE_MONEY("Value: Money"), RATE("Rate"), SHIFT(
		"Shift");
	private String meaning;

	private Type(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    protected Type type;
    protected EffectiveDateInterval effectiveDateRange;
    protected ElementType elementType;
    protected int sequence;
    protected String name;
    protected Unit uom;
    protected float defaultValue;
    protected boolean userEnterable;

    InputValue() {
    }

    public InputValue(Type type, ElementType elementType, int sequence,
	    String name, Unit uom, float defaultValue, LocalDate startDate,
	    LocalDate endDate, boolean userEnterable) {
	this.type = type;
	this.elementType = elementType;
	this.sequence = sequence;
	this.name = name;
	this.uom = uom;
	this.defaultValue = defaultValue;
	this.userEnterable = userEnterable;
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
    }

    public void setElementType(ElementType elementType) {
	this.elementType = elementType;
    }

    public int getSequence() {
	return sequence;
    }

    public float getDefaultValue() {
	return defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public EffectiveDateInterval getEffectiveDateRange() {
	return effectiveDateRange;
    }

    public DateTime getStartDate() {
	return effectiveDateRange.getStartDate();
    }

    public float computeOverlappingHours(Interval arg) {
	Interval period = effectiveDateRange.getOverlap(arg);
	return period.toDurationMillis() / 1000 / 60 / 60;
    }

    @Override
    public boolean isEffective(DateTime effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }

    @Override
    public boolean isEffective(LocalDate effectiveDate) {
	return effectiveDateRange.isEffective(effectiveDate);
    }
}
