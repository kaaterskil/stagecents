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

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Represents the highest unit of work at which revenue, and expense can be
 * maintained, A project can be broken down into one or more Activities.
 * 
 * @author Blair Caple
 */
public class Project {

    public enum Status {
	APPROVED("Approved"), CLOSED("Closed"), SUBMITTED("Submitted"), UNAPPROVED(
		"Unapproved");
	private String meaning;

	private Status(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    private String name;
    private String description;
    private Status status;
    private Season season;
    private int sequence;
    private LocalDate startDate;
    private LocalDate completionDate;
    private LocalDate closeDate;

    // Bidirectional one-to-many association.
    private SortedSet<Budget> budgets = new TreeSet<Budget>(
	    new BudgetComparator());

    // Bidirectional many-to-many association. The join table is named
    // PA_PROJECT_CLASSES.
    private Set<ClassificationCode> classCodes = new HashSet<ClassificationCode>();

    // Unidirectional one-to-many association.
    private SortedSet<ExpenditureItem> expenditureItems = new TreeSet<ExpenditureItem>(
	    new ExpenditureItemComparator());

    // Bidirectional one-to-many association.
    private SortedSet<Activity> activities = new TreeSet<Activity>(
	    new ActivityComparator());

    // Unidirectional many-to-many association. The join table is named
    // PA_PROJECT_PARTIES
    private Set<Resource> resources = new HashSet<Resource>();

    // Unidirectional many-to-many-association. The join table name is
    // PA_RESOURCE_LIST_ASSIGNMENTS.
    private Set<ResourceList> resourceLists = new HashSet<ResourceList>();

    Project() {
    }

    public Project(String name, String description, Status status, Season season,
	    int sequence, LocalDate startDate, LocalDate completionDate) {
	this.name = name;
	this.description = description;
	this.status = status;
	this.season = season;
	this.sequence = sequence;
	this.startDate = startDate;
	this.completionDate = completionDate;
    }

    public void setSeason(Season season) {
	this.season = season;
    }

    public int getSequence() {
	return sequence;
    }

    public Set<ClassificationCode> getClassCodes() {
	return classCodes;
    }

    public void addBudget(Budget arg) {
	arg.setProject(this);
	budgets.add(arg);
    }

    public void removeBudget(Budget arg) {
	arg.setProject(null);
	budgets.remove(arg);
    }

    public void addClassCode(ClassificationCode arg) {
	arg.getProjects().add(this);
	classCodes.add(arg);
    }

    public void removeClassCode(ClassificationCode arg) {
	arg.getProjects().remove(this);
	classCodes.remove(arg);
    }

    public void addExpenditureItem(ExpenditureItem arg) {
	arg.setProject(this);
	expenditureItems.add(arg);
    }

    public void removeExpenditureItem(ExpenditureItem arg) {
	arg.setProject(null);
	expenditureItems.remove(arg);
    }

    public void addActivity(Activity arg) {
	arg.setProject(this);
	activities.add(arg);
    }

    public void removeActivity(Activity arg) {
	arg.setProject(null);
	activities.remove(arg);
    }

    public void addResource(Resource arg) {
	resources.add(arg);
    }

    public void removeResource(Resource arg) {
	resources.remove(arg);
    }

    public void addResourceList(ResourceList arg) {
	resourceLists.add(arg);
    }

    public void removeResourceList(ResourceList arg) {
	resourceLists.remove(arg);
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

    public static class BudgetComparator implements Comparator<Budget> {

	@Override
	public int compare(Budget o1, Budget o2) {
	    int v1 = o1.getVersionNumber();
	    int v2 = o2.getVersionNumber();
	    return (v1 < v2) ? -1 : ((v1 == v2) ? 0 : 1);
	}
    }

    public static class ExpenditureItemComparator implements
	    Comparator<ExpenditureItem> {

	@Override
	public int compare(ExpenditureItem o1, ExpenditureItem o2) {
	    LocalDate d1 = o1.getDate();
	    LocalDate d2 = o2.getDate();
	    return d1.isBefore(d2) ? -1 : (d1.equals(d2) ? 0 : 1);
	}

    }
}
