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
package com.stagecents.fnd.query.repository.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.stagecents.common.hibernate.BaseJpaRepository;
import com.stagecents.fnd.domain.BusinessUnitId;
import com.stagecents.fnd.domain.Location;
import com.stagecents.fnd.domain.LocationId;
import com.stagecents.fnd.query.repository.LocationRepository;

public class LocationQueryRepository extends
	BaseJpaRepository<Location, LocationId> implements LocationRepository {

    public boolean checkUniqueName(LocationId locationId, String name,
	    BusinessUnitId businessUnitId) {
	String hql = "from Location loc where loc.name = :name "
		+ "and loc.locationId != :locationId "
		+ "and (loc.businessUnit.businessUnitId != :businessUnitId)";
	Query q = getSession().createQuery(hql);
	q.setString("name", name);
	q.setString("locationId", locationId.getIdentifier());
	q.setString("businessUnitId", businessUnitId.getIdentifier());
	List<Location> result = q.list();
	return result.isEmpty();
    }

    @Override
    public boolean checkBusinessUnits(LocationId locationId) {
	String hql = "from BusinessUnit bu where bu.locationId = :locationId";
	Query q = getSession().createQuery(hql);
	q.setString("locationId", locationId.getIdentifier());
	return q.list().isEmpty();
    }

    @Override
    public boolean checkPositions(LocationId locationId) {
	String hql = "from Position p where p.locationId = :locationId";
	Query q = getSession().createQuery(hql);
	q.setString("locationId", locationId.getIdentifier());
	return q.list().isEmpty();
    }
}
