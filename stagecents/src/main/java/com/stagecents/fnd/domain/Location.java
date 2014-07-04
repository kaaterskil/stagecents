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

import java.util.HashSet;
import java.util.Set;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.joda.time.LocalDate;

import com.stagecents.common.Region;
import com.stagecents.fnd.api.command.CreateLocationCommand;
import com.stagecents.fnd.api.command.DeleteLocationCommand;
import com.stagecents.fnd.api.command.DisableLocationCommand;
import com.stagecents.fnd.api.command.LocationCreatedEvent;
import com.stagecents.fnd.api.command.LocationDeletedEvent;
import com.stagecents.fnd.api.command.LocationDisabledEvent;
import com.stagecents.fnd.api.command.LocationUpdatedEvent;
import com.stagecents.fnd.api.command.UpdateLocationCommand;
import com.stagecents.hr.domain.Position;

public class Location extends AbstractAnnotatedAggregateRoot<LocationId> {

    @AggregateIdentifier
    private LocationId locationId;
    private int version;

    private String name;
    private BusinessUnitId businessUnitId;
    private String description;
    private LocalDate inactiveDate;
    private String street1;
    private String street2;
    private String street3;
    private String city;
    private Region region;
    private String postalCode;
    private Set<BusinessUnitId> businessUnits = new HashSet<BusinessUnitId>();
    private Set<Position> positions = new HashSet<Position>();

    Location() {
    }

    public Location(CreateLocationCommand command) {
	apply(new LocationCreatedEvent(command.getLocationId(),
		command.getName(), command.getDescription(),
		command.getInactiveDate(), command.getStreet1(),
		command.getStreet2(), command.getStreet3(), command.getCity(),
		command.getRegion(), command.getPostalCode()));
    }

    @Override
    public LocationId getIdentifier() {
	return locationId;
    }

    public BusinessUnitId getBusinessUnit() {
	return businessUnitId;
    }

    public LocalDate getInactiveDate() {
	return inactiveDate;
    }

    public Set<BusinessUnitId> getBusinessUnits() {
	return businessUnits;
    }

    public Set<Position> getPositions() {
	return positions;
    }

    @EventHandler
    public void handleCreate(LocationCreatedEvent event) {
	locationId = event.getLocationId();
	name = event.getName();
	description = event.getDescription();
	inactiveDate = event.getInactiveDate();
	street1 = event.getStreet1();
	street2 = event.getStreet2();
	street3 = event.getStreet3();
	city = event.getCity();
	region = event.getRegion();
	postalCode = event.getPostalCode();
    }

    @EventHandler
    public void handleDelete(LocationDeletedEvent event) {
	markDeleted();
    }

    @EventHandler
    public void handleDisable(LocationDisabledEvent event) {
	inactiveDate = event.getInactiveDate();
    }

    @EventHandler
    public void handleUpdate(LocationUpdatedEvent event) {
	description = event.getDescription();
	inactiveDate = event.getInactiveDate();
	street1 = event.getStreet1();
	street2 = event.getStreet2();
	street3 = event.getStreet3();
	city = event.getCity();
	region = event.getRegion();
	postalCode = event.getPostalCode();
    }

    public void delete(DeleteLocationCommand command) {
	apply(new LocationDeletedEvent(locationId));
    }

    @CommandHandler
    public void disable(DisableLocationCommand command) {
	validateInactiveDate(command.getInactiveDate());
	apply(new LocationDisabledEvent(locationId, command.getInactiveDate()));
    }

    @CommandHandler
    public void update(UpdateLocationCommand command) {
	validateInactiveDate(command.getInactiveDate());
	apply(new LocationUpdatedEvent(locationId, command.getDescription(),
		command.getInactiveDate(), command.getStreet1(),
		command.getStreet2(), command.getStreet3(), command.getCity(),
		command.getRegion(), command.getPostalCode()));
    }

    /**
     * Validates that the given inactive date is greater than or equal to the
     * session date. If the inactive date is less than the session date then an
     * IllegalArgumentException will be raised and processing terminated.
     * 
     * @param inactiveDate The inactive date to test
     */
    private void validateInactiveDate(LocalDate inactiveDate) {
	if (inactiveDate != null) {
	    LocalDate today = new LocalDate();
	    if (inactiveDate.isBefore(today)) {
		throw new IllegalArgumentException(
			"inactive date cannot be in the past");
	    }
	}
    }
}
