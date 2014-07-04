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

import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public interface Activity {

    /**
     * Returns the name of the activity.
     * 
     * @return the name of the activity.
     */
    String getName();

    /**
     * Sets the parent activity. If a parent activity was previously set, it is
     * returned.
     * 
     * @param parent The new parent activity.
     * @return The old parent activity, or null if no parent activity was
     *         previously set.
     */
    Activity setParent(Activity parent);

    /**
     * Returns the Project object representing the project to which this
     * activity is assigned.
     * 
     * @return The project that owns this activity.
     */
    Project getProject();

    /**
     * Associates the activity with the given project.
     * 
     * @param project The project to associate.
     */
    void setProject(Project project);

    /**
     * Returns the scheduled starting date and time.
     * 
     * @return The scheduled starting date and time of the activity.
     */
    DateTime getScheduledStart();

    /**
     * Returns the scheduled ending date and time.
     * 
     * @return The scheduled ending date and time.
     */
    DateTime getScheduledEnd();

    /**
     * Returns the scheduled time interval of the activity
     * 
     * @return The scheduled time interval of the activity.
     */
    Interval duration();

    /**
     * Returns the set of resources associated with this activity.
     * 
     * @return The set of resources associated with this activity.
     */
    Set<Resource> getResources();

    /**
     * Returns the set of budgets associated with this activity.
     * 
     * @return The set of budgets associated with this activity.
     */
    Set<Budget> getBudgets();
}
