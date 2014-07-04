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

/**
 * Represents the types of resources supported by this application. This
 * includes:
 * <ul>
 * <li>Position</li>
 * <li>Job</li>
 * <li>Organization</li>
 * <li>Expenditure Type</li>
 * <li>Expenditure Category</li>
 * <li>Revenue Category</li>
 * </ul>
 * 
 * @author Blair Caple
 */
public enum ResourceType {
    POSITION("Position"), JOB("Job"), ORGANIZATION("Organization"), EXPENDITURE_TYPE(
	    "Expenditure Type"), EXPENDITURE_CATEGORY("Expenditure Category"), REVENUE_CATEGORY(
	    "Revenue Category");
    private String meaning;

    private ResourceType(String meaning) {
	this.meaning = meaning;
    }

    public String getMeaning() {
	return meaning;
    }
}
