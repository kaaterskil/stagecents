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

/**
 * Represents a venue location. A Venue may be nested to an unlimited degree can
 * contain date effective costs in frequencies of per night, week or month.
 * 
 * @author Blair Caple
 */
public class Venue {

    public enum VenueRateFrequency {
	PER_NIGHT("Per Night"), PER_WEEK("Per Week"), PER_MONTH("Per Month");
	private String meaning;

	private VenueRateFrequency(String meaning) {
	    this.meaning = meaning;
	}

	public String getMeaning() {
	    return meaning;
	}
    }

    private String name;
    private String description;
    private Venue parent;
    private VenueRateFrequency rateType;
    private boolean lodging;

    private Set<Venue> children = new HashSet<Venue>();
    private SortedSet<VenueRate> rates = new TreeSet<VenueRate>(
	    new VenueRateComparator());

    public void setParent(Venue parent) {
	this.parent = parent;
    }

    public void addChild(Venue child) {
	child.setParent(this);
	children.add(child);
    }

    public void removeChild(Venue child) {
	child.setParent(null);
	children.remove(child);
    }

    public void addRate(VenueRate rate) {
	rate.setVenue(this);
	rates.add(rate);
    }

    public void removeRate(VenueRate rate) {
	rate.setVenue(null);
	rates.remove(rate);
    }

    public static class VenueRateComparator implements Comparator<VenueRate> {

	@Override
	public int compare(VenueRate o1, VenueRate o2) {
	    DateTime d1 = o1.getStartDate();
	    DateTime d2 = o2.getStartDate();
	    return d1.isBefore(d2) ? -1 : (d1.equals(d2) ? 0 : 1);
	}

    }
}
