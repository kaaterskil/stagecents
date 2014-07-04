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
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;

public class Budget {

    public enum BudgetStatus {
	APPROVED, SUBMITTED, DRAFT;
    }

    private String name;
    private String description;
    private Project project;
    private BudgetStatus status;
    private boolean current;

    private float totalHours;
    private float totalRawCost;
    private float totalRevenue;

    private Budget parent;
    private int versionNumber;
    // Bidirectional one-to-many association.
    private SortedSet<Budget> children = new TreeSet<Budget>(
	    new BudgetComparator());
    // Bidirectional many-to-many association.
    private SortedSet<Activity> activities = new TreeSet<Activity>(
	    new ActivityComparator());

    Budget() {
    }

    public Budget(String name, String description, Project project,
	    BudgetStatus status, boolean current) {
	this.name = name;
	this.description = description;
	this.project = project;
	this.status = status;
	this.current = current;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setParent(Budget parent) {
	this.parent = parent;
    }

    public int getVersionNumber() {
	return versionNumber;
    }

    public SortedSet<Activity> getActivities() {
	return activities;
    }

    public void addActivity(Activity arg) {
	arg.getBudgets().add(this);
	activities.add(arg);
    }

    public void removeActivity(Activity arg) {
	arg.getBudgets().remove(this);
	activities.remove(arg);
    }

    public void addChild(Budget child) {
	if (!canAddChild(this, child)) {
	    throw new IllegalArgumentException("invalid child budget");
	}
	child.setParent(this);
	children.add(child);
    }

    public void removeChild(Budget child) {
	child.setParent(null);
	children.remove(child);
    }

    List<Budget> children() {
	List<Budget> result = new ArrayList<Budget>();
	Iterator<Budget> iter = children.iterator();
	while (iter.hasNext()) {
	    result.add(iter.next());
	}
	return result;
    }

    List<Budget> parents() {
	List<Budget> result = new ArrayList<Budget>();
	if (parent != null) {
	    result.add(parent);
	    List<Budget> parents = parent.parents();
	    if (!parents.isEmpty()) {
		Iterator<Budget> iter = parents.iterator();
		result.add(iter.next());
	    }
	}
	return result;
    }

    static boolean canAddChild(Budget parent, Budget child) {
	if (parent.equals(child)) {
	    return false;
	}
	if (parent.ancestorsInclude(child)) {
	    return false;
	}
	return true;
    }

    boolean ancestorsInclude(Budget candidate) {
	Iterator<Budget> iter = parents().iterator();
	while (iter.hasNext()) {
	    Budget parent = iter.next();
	    if (parent.equals(candidate)) {
		return true;
	    }
	}
	return false;
    }

    public static class BudgetComparator implements Comparator<Budget> {

	@Override
	public int compare(Budget o1, Budget o2) {
	    int v1 = o1.versionNumber;
	    int v2 = o2.versionNumber;
	    return (v1 < v2) ? -1 : ((v1 == v2) ? 0 : 1);
	}
    }

    public static class ActivityComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
	    DateTime d1 = o1.getScheduledStart();
	    DateTime d2 = o2.getScheduledStart();
	    if (d1.isBefore(d2)) {
		return -1;
	    } else if (d1.isEqual(d2)) {
		return o1.getName().compareTo(o2.getName());
	    }
	    return 1;
	}

    }
}
