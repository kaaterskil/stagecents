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

import com.stagecents.common.Region;

public class Location {

    private String name;
    private String description;
    private String street1;
    private String street2;
    private String street3;
    private String city;
    private Region region;
    private String postalCode;

    Location() {
    }

    public Location(String name, String description, String street1,
	    String street2, String street3, String city, Region region,
	    String postalCode) {
	this.name = name;
	this.description = description;
	this.street1 = street1;
	this.street2 = street2;
	this.street3 = street3;
	this.city = city;
	this.region = region;
	this.postalCode = postalCode;
    }
}
