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

import java.util.HashSet;
import java.util.Set;

/**
 * Represents implementation-defined values within a classification category
 * that may be used to classify projects.
 * 
 * @author Blair Caple
 */
public class ClassificationCode {

    private ClassificationCategory classCategory;
    private String name;
    private String description;

    // Bidirectional many-to-many association. THe join table is named
    // PA_PROJECT_CLASSES.
    private Set<Project> projects = new HashSet<Project>();

    ClassificationCode() {
    }

    public ClassificationCode(String name, String description) {
	this.name = name;
	this.description = description;
    }

    public ClassificationCategory getClassCategory() {
	return classCategory;
    }

    public void setClassCategory(ClassificationCategory classCategory) {
	this.classCategory = classCategory;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    public Set<Project> getProjects() {
        return projects;
    }
    
    public void addProject(Project arg) {
	arg.getClassCodes().add(this);
	projects.add(arg);
    }
    
    public void removeProject(Project arg) {
	arg.getClassCodes().remove(this);
	projects.remove(arg);
    }
}
