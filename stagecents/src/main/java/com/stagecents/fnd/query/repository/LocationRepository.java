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
package com.stagecents.fnd.query.repository;

import com.stagecents.common.infrastructure.BaseRepository;
import com.stagecents.fnd.domain.BusinessUnitId;
import com.stagecents.fnd.domain.Location;
import com.stagecents.fnd.domain.LocationId;

public interface LocationRepository extends
	BaseRepository<Location, LocationId> {
    /**
     * Returns true if the given location name is unique within the given
     * business unit, false otherwise.
     * 
     * @param locationId The identifier of the location whose name to check for
     *            uniqueness.
     * @param name The name to check for uniqueness.
     * @param businessUnitId The identifier of the business unit within which to
     *            check.
     * @return True if the given name is unique within the given business unit,
     *         false otherwise.
     */
    boolean checkUniqueName(LocationId locationId, String name,
	    BusinessUnitId businessUnitId);

    /**
     * Returns true if business units exist that are associated with the given
     * location, false otherwise.
     * 
     * @param locationId The Location object identifier to check.
     * @return True if business units exist with the given location, false
     *         otherwise.
     */
    boolean checkBusinessUnits(LocationId locationId);

    /**
     * Returns true if positions exist at the given location, false otherwise.
     * 
     * @param locationId The Location object identifier to check.
     * @return True if positions exist at the given location, false otherwise.
     */
    boolean checkPositions(LocationId locationId);
}
