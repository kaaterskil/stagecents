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
package com.stagecents.gl.api.command;

import com.stagecents.gl.domain.AccountType;

public class SegmentValueDTO {

    private String segmentId;
    private String segmentValueId;
    private String value;
    private String description;
    private String minimumValue;
    private String maximumValue;
    private AccountType accountType;

    public SegmentValueDTO(String segmentId, String segmentValueId,
	    String value, String description, String minimumValue,
	    String maximumValue, AccountType accountType) {
	this.segmentId = segmentId;
	this.segmentValueId = segmentValueId;
	this.value = value;
	this.description = description;
	this.minimumValue = minimumValue;
	this.maximumValue = maximumValue;
	this.accountType = accountType;
    }

    public String getSegmentId() {
	return segmentId;
    }

    public String getSegmentValueId() {
	return segmentValueId;
    }

    public String getValue() {
	return value;
    }

    public String getDescription() {
	return description;
    }

    public String getMinimumValue() {
	return minimumValue;
    }

    public String getMaximumValue() {
	return maximumValue;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
