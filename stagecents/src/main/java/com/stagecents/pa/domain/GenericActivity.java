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
import org.joda.time.Interval;

import com.stagecents.hxt.domain.DetailHours;

/**
 * Represents the base class of user-defined subdivisions of project work.
 * 
 * @author Blair Caple
 */
public class GenericActivity implements Activity {

    /**
     * Represents classifications of tasks. Used as the discriminator to
     * generate subclasses.
     */
    public enum TaskType {
	TASK("Task"), REHEARSAL("Rehearsal"), DRESS_REHEARSAL("Dress Rehearsal"), PERFORMANCE(
		"Performance");
	private String meaning;

	private TaskType(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    public enum Status {
	NOT_STARTED("Not Started"), IN_PROGRESS("In Progress"), COMPLETED(
		"Completed"), ON_HOLD("On Hold"), CANCELLED("Cancelled");
	private String meaning;

	private Status(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    protected String name;
    protected String description;
    protected TaskType taskType;
    protected Status status;
    protected Project project;
    protected Activity parent;
    protected DateTime scheduledStart;
    protected DateTime scheduledEnd;
    protected Venue venue;

    // Bidirectional many-to-many association. The join table is named
    // PA_RESOURCE_ASSIGNMENTS and has no additional columns.
    protected Set<Resource> resources = new HashSet<Resource>();

    // Bidirectional many-to-many association to project budget versions.
    protected SortedSet<Budget> budgets = new TreeSet<Budget>(
	    new BudgetComparator());

    // Bidirectional one-to-many association to timesheet detail.
    protected Set<DetailHours> timesheetDetails = new HashSet<DetailHours>();

    // Bidirectional one-to-many association to sub-tasks.
    protected SortedSet<Activity> children = new TreeSet<Activity>(
	    new ActivityComparator());

    GenericActivity() {
    }

    public GenericActivity(String name, String description, Project project,
	    DateTime scheduledStart, DateTime scheduledEnd, Venue venue) {
	this(name, description, project, scheduledStart, scheduledEnd, venue,
		TaskType.TASK, Status.NOT_STARTED);
    }

    protected GenericActivity(String name, String description, Project project,
	    DateTime scheduledStart, DateTime scheduledEnd, Venue venue,
	    TaskType taskType, Status status) {
	this.name = name;
	this.description = description;
	this.project = project;
	this.scheduledStart = scheduledStart;
	this.scheduledEnd = scheduledEnd;
	this.venue = venue;
	this.taskType = taskType;
	this.status = status;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public Project getProject() {
	return project;
    }

    @Override
    public void setProject(Project project) {
	this.project = project;
    }

    @Override
    public Activity setParent(Activity parent) {
	Activity oldParent = parent;
	this.parent = parent;
	return oldParent;
    }

    @Override
    public DateTime getScheduledStart() {
	return scheduledStart;
    }

    @Override
    public DateTime getScheduledEnd() {
	return scheduledEnd;
    }

    @Override
    public Set<Resource> getResources() {
	return resources;
    }

    @Override
    public Set<Budget> getBudgets() {
	return budgets;
    }

    @Override
    public Interval duration() {
	return new Interval(scheduledStart, scheduledEnd);
    }

    public void addResource(Resource resource) {
	resource.getActivities().add(this);
	resources.add(resource);
    }

    public void removeResource(Resource resource) {
	resource.getActivities().remove(this);
	resources.remove(resource);
    }

    public void addBudget(Budget budget) {
	budget.getActivities().add(this);
	budgets.add(budget);
    }

    public void removeBudget(Budget budget) {
	budget.getActivities().remove(this);
	budgets.remove(budget);
    }

    public void addTimesheetDetail(DetailHours arg) {
	arg.setActivity(this);
	timesheetDetails.add(arg);
    }

    public void removeTimesheetDetail(DetailHours arg) {
	arg.setActivity(null);
	timesheetDetails.remove(arg);
    }

    public void addChild(Activity activity) {
	if (!canAddChildTask(this, activity)) {
	    throw new IllegalArgumentException("invalid child activity");
	}
	activity.setParent(this);
	children.add(activity);
    }

    public void removeChild(Activity activity) {
	activity.setParent(null);
	children.remove(activity);
    }

    List<Activity> children() {
	List<Activity> result = new ArrayList<Activity>();
	Iterator<Activity> iter = children.iterator();
	while (iter.hasNext()) {
	    result.add(iter.next());
	}
	return result;
    }

    List<Activity> parents() {
	List<Activity> result = new ArrayList<Activity>();
	if (parent != null) {
	    result.add(parent);

	    GenericActivity p = (GenericActivity) parent;
	    List<Activity> parents = p.parents();
	    if (!parents.isEmpty()) {
		Iterator<Activity> iter = parents.iterator();
		while (iter.hasNext()) {
		    result.add(iter.next());
		}
	    }
	}
	return result;
    }

    static boolean canAddChildTask(Activity parent, Activity candidate) {
	if (parent.equals(candidate)) {
	    return false;
	}
	if (!parent.getProject().equals(candidate.getProject())) {
	    return false;
	}
	GenericActivity p = (GenericActivity) parent;
	if (p.ancestorsInclude(candidate)) {
	    return false;
	}
	return true;
    }

    boolean ancestorsInclude(Activity activity) {
	Iterator<Activity> iter = parents().iterator();
	while (iter.hasNext()) {
	    Activity ancestor = iter.next();
	    if (ancestor.equals(activity)) {
		return true;
	    }
	}
	return false;
    }

    public static class BudgetComparator implements Comparator<Budget> {

	@Override
	public int compare(Budget o1, Budget o2) {
	    int v1 = o1.getVersionNumber();
	    int v2 = o2.getVersionNumber();
	    return (v1 < v2) ? -1 : ((v1 == v2) ? 0 : 1);
	}
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
