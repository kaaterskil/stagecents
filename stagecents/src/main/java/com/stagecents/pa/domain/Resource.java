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
package com.stagecents.pa.domain;

import java.util.List;
import java.util.SortedSet;

import org.joda.time.DateTime;

import com.stagecents.pay.domain.ElementType;

public interface Resource<E> {

    /**
     * Returns the underling resource object.
     * 
     * @return The underlying resource.
     */
    E getResource();

    /**
     * Returns the name of the resource.
     * 
     * @return The name of the resource.
     */
    String getName();

    /**
     * Returns the cost rate effective on the given effective date, or null if
     * no cost rate is defined.
     * 
     * @param effectiveDate the date to search for an effective cost rate.
     * @return The CostRate object representing the cost rate in effect on the
     *         specified date.
     */
    CostRate getCostRate(DateTime effectiveDate);

    /**
     * Returns the set of activities associated with this resource.
     * 
     * @return The set of activities associated with this resource.
     */
    SortedSet<Activity> getActivities();

    /**
     * Returns the list of elements associated with this resource.
     * 
     * @return the list of elements associated with this resource.
     */
    List<ElementType> getElements();
}
