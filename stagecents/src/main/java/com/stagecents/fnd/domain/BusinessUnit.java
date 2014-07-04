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

import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import com.stagecents.fnd.api.command.BusinessUnitCreatedEvent;
import com.stagecents.fnd.api.command.BusinessUnitDeletedEvent;
import com.stagecents.fnd.api.command.BusinessUnitUpdatedEvent;
import com.stagecents.fnd.api.command.DeleteBusinessUnitCommand;

/**
 * Represents the definitions that identify business groups and the organization
 * units within a single business group.
 * 
 * @author Blair Caple
 */
public class BusinessUnit extends
AbstractAnnotatedAggregateRoot<BusinessUnitId> {

    @AggregateIdentifier
    private BusinessUnitId businessUnitId;
    private int version;

    private BusinessUnit parent;
    private LocationId locationId;
    private String name;
    private Currency baseCurrency;
    private Set<BusinessUnit> children = new HashSet<BusinessUnit>();

    BusinessUnit() {
    }

    public BusinessUnit(BusinessUnitId businessUnitId, BusinessUnit parent,
	    LocationId locationId, String name, Currency baseCurrency) {
	apply(new BusinessUnitCreatedEvent(businessUnitId, parent, locationId,
		name, baseCurrency));
    }

    @Override
    public BusinessUnitId getIdentifier() {
	return businessUnitId;
    }

    public void setParent(BusinessUnit parent) {
	this.parent = parent;
    }

    public Currency getBaseCurrency() {
	if (baseCurrency == null && parent != null) {
	    return parent.getBaseCurrency();
	}
	return baseCurrency;
    }

    public Set<BusinessUnit> getChildren() {
	return children;
    }

    @EventHandler
    public void handleCreate(BusinessUnitCreatedEvent event) {
	businessUnitId = event.getBusinessUnitId();
	parent = event.getParent();
	locationId = event.getLocationId();
	name = event.getName();
	baseCurrency = event.getBaseCurrency();
    }

    @EventHandler
    public void handleDelete(BusinessUnitDeletedEvent event) {
	this.markDeleted();
    }

    @EventHandler
    public void handleUpdate(BusinessUnitUpdatedEvent event) {
	parent = event.getParent();
	locationId = event.getLocationId();
	name = event.getName();
	baseCurrency = event.getBaseCurrency();
    }

    @CommandHandler
    public void delete(DeleteBusinessUnitCommand command) {
	apply(new BusinessUnitDeletedEvent(businessUnitId));
    }

    public void update(BusinessUnit parent, LocationId locationId, String name,
	    Currency baseCurrency) {
	apply(new BusinessUnitUpdatedEvent(businessUnitId, parent, locationId,
		name, baseCurrency));
    }

    public void addChild(BusinessUnit child) {
	child.setParent(this);
	children.add(child);
    }

    public boolean ancestorsInclude(BusinessUnit sample) {
	if (parent != null) {
	    if (parent.equals(sample)) {
		return true;
	    }
	    if (parent.ancestorsInclude(sample)) {
		return true;
	    }
	}
	return false;
    }
}
