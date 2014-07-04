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
import org.axonframework.repository.Repository;
import org.springframework.stereotype.Component;

import com.stagecents.common.ValidationException;
import com.stagecents.fnd.api.command.CreateBusinessUnitCommand;
import com.stagecents.fnd.api.command.UpdateBusinessUnitCommand;
import com.stagecents.fnd.domain.BusinessUnit;
import com.stagecents.fnd.domain.BusinessUnitId;
import com.stagecents.fnd.domain.Location;
import com.stagecents.fnd.query.repository.BusinessUnitRepository;

@Component
public class BusinessUnitCommandHandler implements BusinessUnitService {

    private Repository<BusinessUnit> repository;
    private BusinessUnitRepository businessUnitRepository;
    private Repository<Location> locationRepository;

    @Resource(name = "businessUnitJpaRepository")
    public void setRepository(Repository<BusinessUnit> repository) {
	this.repository = repository;
    }

    @Resource(name = "businessUnitQueryRepository")
    public void setBusinessUnitRepository(
	    BusinessUnitRepository businessUnitRepository) {
	this.businessUnitRepository = businessUnitRepository;
    }

    @Resource(name = "locationJpaRepository")
    public void setLocationRepository(Repository<Location> locationRepository) {
	this.locationRepository = locationRepository;
    }

    @CommandHandler
    public void createBusinessUnit(CreateBusinessUnitCommand command) {
	checkName(command.getParentId(), command.getBusinessUnitId(),
		command.getName());

	BusinessUnit parent = null;
	if (command.getParentId() != null) {
	    parent = repository.load(command.getParentId());
	}

	BusinessUnit businessUnit = new BusinessUnit(
		command.getBusinessUnitId(), parent, command.getLocationId(),
		command.getName(), command.getBaseCurrency());
	repository.add(businessUnit);
    }

    @CommandHandler
    public void updateBusinessUnit(UpdateBusinessUnitCommand command) {
	checkName(command.getParentId(), command.getId(), command.getName());

	BusinessUnit businessUnit = repository.load(command.getId());
	BusinessUnit parent = null;
	if (command.getParentId() != null) {
	    // Check for cycles
	    parent = repository.load(command.getParentId());
	    if (parent != null && parent.ancestorsInclude(businessUnit)) {
		throw new ValidationException("invalid parent");
	    }
	}

	businessUnit.update(parent, command.getLocationId(), command.getName(),
		command.getBaseCurrency());
    }

    private void checkName(BusinessUnitId parentId, BusinessUnitId childId,
	    String name) {
	boolean isUnique = businessUnitRepository.uniqueName(parentId, childId,
		name);
	if (!isUnique) {
	    throw new ValidationException("duplicate name");
	}
    }
}
