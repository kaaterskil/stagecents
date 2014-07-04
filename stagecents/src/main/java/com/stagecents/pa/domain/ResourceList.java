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
import java.util.SortedSet;
import java.util.TreeSet;

import com.stagecents.hr.domain.JobGroup;
import com.stagecents.hr.domain.OrganizationUnit;

/**
 * Represents a set of resources, grouping similar resources that are typically
 * used for specific kinds of project work.
 * 
 * @author Blair Caple
 */
public class ResourceList {

    private String name;
    private String description;
    private OrganizationUnit businessGroup;
    private JobGroup jobGroup;

    // Unidirectional many-to-many association. The join table name is
    // PA_RESOURCE_LIST_MEMBERS.
    private SortedSet<Resource> resources = new TreeSet<Resource>(
	    new ResourceComparator());

    ResourceList() {
    }

    public ResourceList(String name, String description,
	    OrganizationUnit businessGroup, JobGroup jobGroup) {
	this.name = name;
	this.description = description;
	this.businessGroup = businessGroup;
	this.jobGroup = jobGroup;
    }

    public SortedSet<Resource> getResources() {
	return resources;
    }

    public void addResource(Resource arg) {
	resources.add(arg);
    }

    public static class ResourceComparator implements Comparator<Resource> {

	@Override
	public int compare(Resource o1, Resource o2) {
	    return o1.getName().compareTo(o2.getName());
	}

    }
}
