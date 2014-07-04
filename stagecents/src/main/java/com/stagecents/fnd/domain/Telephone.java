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
package com.stagecents.fnd.domain;

public class Telephone {

    private String id;
    private BusinessUnit businessUnit;
    private Telephone.Type type;
    private String value;
    private boolean primary;
    private boolean inactive;

    public static enum Type {
	HOME("Home"), HOME_FAX("Home Fax"), BUSINESS("Business"), BUSINESS_DIRECT(
		"Business Direct"), BUSINESS_FAX("Business Fax"), MOBILE(
		"Mobile"), VACATION("Vacation");
	private String meaning;

	private Type(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    public Telephone() {
    }

    public Telephone(String id, BusinessUnit businessUnit, Type type,
	    String value) {
	this.id = id;
	this.businessUnit = businessUnit;
	this.type = type;
	this.value = value;
    }

    public String getId() {
        return id;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public Telephone.Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }
    
}
