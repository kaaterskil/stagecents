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

import org.joda.time.LocalDate;

import com.stagecents.common.EffectiveDateInterval;

public class Season {

    private String name;
    private EffectiveDateInterval effectiveDateRange;
    private boolean locked;

    // Bidirectional one-to-many association.
    private SortedSet<Project> projects = new TreeSet<Project>(
	    new ProjectComparator());

    Season() {
    }

    public Season(String name, LocalDate startDate, LocalDate endDate) {
	this.name = name;
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
	locked = false;
    }

    public void addProject(Project arg) {
	arg.setSeason(this);
	projects.add(arg);
    }

    public void removeProject(Project arg) {
	arg.setSeason(null);
	projects.remove(arg);
    }

    public static class ProjectComparator implements Comparator<Project> {

	@Override
	public int compare(Project o1, Project o2) {
	    int s1 = o1.getSequence();
	    int s2 = o2.getSequence();
	    return (s1 < s2) ? -1 : ((s1 == s2) ? 0 : 1);
	}

    }
}
