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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.stagecents.common.DateEffective;
import com.stagecents.common.EffectiveDateInterval;
import com.stagecents.pay.domain.ElementType;

public abstract class AbstractResource<E> implements Resource<E>, DateEffective {

    protected ResourceType resourceType;
    private String name;
    private String description;
    private EffectiveDateInterval effectiveDateRange;

    // Unidirectional many-to-many association for non-labor resources.
    private Set<CostRate> costRates = new HashSet<CostRate>();

    // Bidirectional many-to-many association. The join table is named
    // PA_RESOURCE_ASSIGNMENTS, the primary key of which must reference the
    // project, activity and resource.
    private SortedSet<Activity> activities = new TreeSet<Activity>(
	    new ActivityComparator());

    AbstractResource() {
    }

    protected AbstractResource(String name, String description,
	    LocalDate startDate, LocalDate endDate, ResourceType type) {
	this(name, description, type);
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
    }

    protected AbstractResource(String name, String description,
	    ResourceType type) {
	this.name = name;
	this.description = description;
	this.resourceType = type;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public SortedSet<Activity> getActivities() {
	return activities;
    }

    public void addActivity(Activity activity) {
	activity.getResources().add(this);
	activities.add(activity);
    }

    @Override
    public CostRate getCostRate(DateTime effectiveDate) {
	Iterator<CostRate> iter = costRates.iterator();
	while (iter.hasNext()) {
	    CostRate cr = iter.next();
	    if (cr.isEffective(effectiveDate)) {
		return cr;
	    }
	}
	return null;
    }

    @Override
    public List<ElementType> getElements() {
	return new ArrayList<ElementType>();
    }

    @Override
    public boolean isEffective(DateTime effectiveDate) {
	return (effectiveDateRange == null) ? true : effectiveDateRange
		.isEffective(effectiveDate);
    }

    @Override
    public boolean isEffective(LocalDate effectiveDate) {
	return (effectiveDateRange == null) ? true : effectiveDateRange
		.isEffective(effectiveDate);
    }

    public static class ActivityComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
	    DateTime s1 = o1.getScheduledStart();
	    DateTime s2 = o2.getScheduledStart();
	    return s1.isBefore(s2) ? -1 : (s1.isEqual(s2) ? 0 : 1);
	}

    }
}
