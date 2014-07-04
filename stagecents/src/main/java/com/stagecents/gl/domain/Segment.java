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
package com.stagecents.gl.domain;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedEntity;

import com.stagecents.gl.api.command.DeleteSegmentValueCommand;
import com.stagecents.gl.api.command.SegmentValueDeletedEvent;

public class Segment extends AbstractAnnotatedEntity {

    private String segmentId;
    private int version;

    private String name;
    private Structure structure;
    private int sequence;
    private boolean enabled;
    private int length;
    private boolean naturalAccountSegment;
    private boolean costCenterSegment;
    private SortedSet<SegmentValue> segmentValues = new TreeSet<SegmentValue>(
	    new SegmentValueComparator());

    // Lazy loaded vector of segment values.
    private transient Vector<SegmentValue> vector;

    Segment() {
    }

    public Segment(String segmentId, String name, Structure structure,
	    int sequence, boolean enabled, int length,
	    boolean naturalAccountSegment, boolean costCenterSegment) {
	this.segmentId = segmentId;
	this.name = name;
	this.structure = structure;
	this.sequence = sequence;
	this.enabled = enabled;
	this.length = length;
	this.naturalAccountSegment = naturalAccountSegment;
	this.costCenterSegment = costCenterSegment;
    }

    public String getSegmentId() {
	return segmentId;
    }

    public String getName() {
	return name;
    }

    public Structure getStructure() {
	return structure;
    }

    public void setStructure(Structure structure) {
	this.structure = structure;
    }

    public int getSequence() {
	return sequence;
    }

    public int getLength() {
	return length;
    }

    public boolean isNaturalAccountSegment() {
	return naturalAccountSegment;
    }

    public boolean isCostCenterSegment() {
	return costCenterSegment;
    }

    public SortedSet<SegmentValue> getSegmentValues() {
	return segmentValues;
    }

    public void addSegmentValue(SegmentValue segmentValue) {
	segmentValue.setSegment(this);
	segmentValues.add(segmentValue);
    }

    @CommandHandler
    public void deleteSegmentValue(DeleteSegmentValueCommand command) {
	apply(new SegmentValueDeletedEvent(command.getStructureId(),
		command.getSegmentId(), command.getSegmentValueId()));
    }

    public void deleteSegmentValue(SegmentValue segmentValue) {
	segmentValue.setSegment(null);
	segmentValues.remove(segmentValue);
	apply(new SegmentValueDeletedEvent(structure.getIdentifier(),
		segmentId, segmentValue.getSegmentValueId()));
    }

    public Vector<SegmentValue> getSegmentValuesAsVector() {
	if ((vector == null) || (vector.size() != segmentValues.size())) {
	    vector = new Vector<SegmentValue>(segmentValues);
	}
	return vector;
    }

    private static class SegmentValueComparator implements
    Comparator<SegmentValue> {

	public int compare(SegmentValue o1, SegmentValue o2) {
	    int s1 = o1.getSequence();
	    int s2 = o2.getSequence();
	    return (s1 < s2) ? -1 : ((s1 == s2) ? 0 : 1);
	}

    }
}
