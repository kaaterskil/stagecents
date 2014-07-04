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
package com.stagecents.fnd.api.service;

import javax.annotation.Resource;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.repository.Repository;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import com.stagecents.fnd.api.command.CreateLocationCommand;
import com.stagecents.fnd.api.command.DeleteLocationCommand;
import com.stagecents.fnd.domain.BusinessUnitId;
import com.stagecents.fnd.domain.Location;
import com.stagecents.fnd.domain.LocationId;
import com.stagecents.fnd.query.repository.LocationRepository;

@Component
public class LocationCommandHandler implements LocationService {

    private Repository<Location> repository;
    private LocationRepository locationRepository;

    @Resource(name = "locationJpaRepository")
    public void setRepository(Repository<Location> repository) {
	this.repository = repository;
    }

    @Resource(name = "locationQueryRepository")
    public void setLocationRepository(LocationRepository locationRepository) {
	this.locationRepository = locationRepository;
    }

    @CommandHandler
    public void createLocation(CreateLocationCommand command) {
	checkUniqueName(command.getLocationId(), command.getName(),
		command.getBusinessUnitId());
	checkInactiveDate(command.getInactiveDate());

	Location location = new Location(command);
	repository.add(location);
    }

    @CommandHandler
    public void deleteLocation(DeleteLocationCommand command) {
	checkDeleteLocation(command.getLocationId());

	Location location = repository.load(command.getLocationId());
	location.delete(command);
    }

    /**
     * Checks that the given name is unique within the given business unit. If
     * the name is not unique, an IllegalArgumentExcption is raised and
     * processing terminates.
     * 
     * @param locationId The identifier of the location to check.
     * @param name The location name to check for uniqueness.
     * @param businessUnitId The business unit within which to check.
     */
    private void checkUniqueName(LocationId locationId, String name,
	    BusinessUnitId businessUnitId) {
	Assert.notNull(locationRepository, "null repository");
	boolean isUnique = locationRepository.checkUniqueName(locationId, name,
		businessUnitId);
	if (!isUnique) {
	    throw new IllegalArgumentException(
		    "duplicate name within the business unit");
	}

    }

    /**
     * Validates that the inactive date is greater than or equal to the session
     * date. If the inactive date is less than the session date then an
     * IllegalArgumentException will be raised and processing terminated.
     * 
     * @param inactiveDate The date the location becomes inactive.
     * @see Aracle hr_loc_bus
     */
    private void checkInactiveDate(LocalDate inactiveDate) {
	if (inactiveDate != null) {
	    // TODO Replace today with session date
	    LocalDate today = new LocalDate();
	    if (inactiveDate.isBefore(today)) {
		throw new IllegalArgumentException(
			"inactive date cannot be in the past");
	    }
	}
    }

    private void checkDeleteLocation(LocationId locationId) {
	boolean hasNoOrgs = locationRepository.checkBusinessUnits(locationId);
	if (!hasNoOrgs) {
	    throw new IllegalArgumentException(
		    "cannot delete location with associated business units");
	}
	boolean hasNoPositions = locationRepository.checkPositions(locationId);
	if (!hasNoPositions) {
	    throw new IllegalArgumentException(
		    "cannot delete location with associated positions");
	}
    }
}
