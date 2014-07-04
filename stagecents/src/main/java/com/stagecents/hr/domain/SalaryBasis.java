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

import com.stagecents.pay.domain.InputValue;

/**
 * Represents the definition of a specific measure of time in recording a base
 * salary.
 * 
 * @author Blair Caple
 */
public class SalaryBasis {

    public enum PayBasis {
	ANNUAL("Annual Salary"), HOURLY("Hourly Salary"), MONTHLY(
		"Monthly Salary"), PERIOD("Period Salary");
	private String meaning;

	private PayBasis(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    private String name;
    private PayBasis payBasis;
    private int payAnnualizationFactor;
    private String comments;

    // Unidirectional many-to-one association.
    private PayRate payRate;

    // Unidirectional many-to-one association - holds the value for one
    // InputValue object of an elementLink.
    private InputValue inputValue;

    SalaryBasis() {
    }

    public SalaryBasis(String name, PayBasis payBasis,
	    int payAnnualizationFactor, String comments, PayRate payRate,
	    InputValue inputValue) {
	this.name = name;
	this.payBasis = payBasis;
	this.payAnnualizationFactor = payAnnualizationFactor;
	this.comments = comments;
	this.payRate = payRate;
	this.inputValue = inputValue;
    }
}
