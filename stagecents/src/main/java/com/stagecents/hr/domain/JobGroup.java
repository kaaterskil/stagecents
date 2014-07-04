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
package com.stagecents.hr.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class JobGroup {

    private String name;
    private String desription;
    private JobGroup parent;
    // Bidirectional one-to-many association.
    private Set<JobGroup> children = new HashSet<JobGroup>();
    // Unidirectional one-to-many association.
    private SortedSet<Job> jobs = new TreeSet<Job>(new JobComparator());

    JobGroup() {
    }

    public JobGroup(String name, String desription) {
	this.name = name;
	this.desription = desription;
    }

    void setParent(JobGroup parent) {
	this.parent = parent;
    }

    void addChild(JobGroup arg) {
	arg.setParent(this);
	children.add(arg);
    }

    void removeChild(JobGroup arg) {
	arg.setParent(null);
	children.remove(arg);
    }

    void addJob(Job arg) {
	arg.setJobGroup(this);
	jobs.add(arg);
    }

    void removeJob(Job arg) {
	arg.setJobGroup(null);
	jobs.remove(jobs);
    }

    public static class JobComparator implements Comparator<Job> {

	@Override
	public int compare(Job o1, Job o2) {
	    return o1.getName().compareTo(o2.getName());
	}

    }
}
