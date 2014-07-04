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

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import com.stagecents.common.EffectiveDateInterval;
import com.stagecents.pay.domain.ElementLink;

/**
 * Represents the definition of a grade. Grades are used to show the level or
 * rank of an employee in an assignment and my be related to a job or position
 * by the definition of valid grades. They are often used to specify element
 * eligibility rules.
 * 
 * @author Blair Caple
 */
public class Grade {

    private String name;
    private int sequence;
    private EffectiveDateInterval effectiveDateRange;
    private String comments;

    private Set<Position> positions = new HashSet<Position>();
    private Set<ElementLink> elements = new HashSet<ElementLink>();

    Grade() {
    }

    public Grade(String name, int sequence, String comments,
	    LocalDate startDate, LocalDate endDate) {
	this.name = name;
	this.sequence = sequence;
	this.comments = comments;
	effectiveDateRange = new EffectiveDateInterval(startDate, endDate);
    }

    public void addPosition(Position arg) {
	arg.setGrade(this);
	positions.add(arg);
    }

    public void removePosition(Position arg) {
	arg.setGrade(null);
	positions.remove(arg);
    }

    public void linkElement(ElementLink arg) {
	arg.setGrade(this);
	elements.add(arg);
    }

    public void unlinkElement(ElementLink arg) {
	arg.setGrade(null);
	elements.remove(arg);
    }
}
