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
package com.stagecents.fnd.api.command;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.joda.time.LocalDate;

import com.stagecents.common.Region;
import com.stagecents.fnd.domain.LocationId;

public class UpdateLocationCommand {

    @TargetAggregateIdentifier
    private final LocationId locationId;

    private final String description;
    private final LocalDate inactiveDate;
    private final String street1;
    private final String street2;
    private final String street3;
    private final String city;
    private final Region region;
    private final String postalCode;

    public UpdateLocationCommand(LocationId locationId, String description,
	    LocalDate inactiveDate, String street1, String street2,
	    String street3, String city, Region region, String postalCode) {
	this.locationId = locationId;
	this.description = description;
	this.inactiveDate = inactiveDate;
	this.street1 = street1;
	this.street2 = street2;
	this.street3 = street3;
	this.city = city;
	this.region = region;
	this.postalCode = postalCode;
    }

    public LocationId getLocationId() {
	return locationId;
    }

    public String getDescription() {
	return description;
    }

    public LocalDate getInactiveDate() {
	return inactiveDate;
    }

    public String getStreet1() {
	return street1;
    }

    public String getStreet2() {
	return street2;
    }

    public String getStreet3() {
	return street3;
    }

    public String getCity() {
	return city;
    }

    public Region getRegion() {
	return region;
    }

    public String getPostalCode() {
	return postalCode;
    }

}
