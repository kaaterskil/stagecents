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
package com.stagecents.gl.query;

import java.util.ArrayList;
import java.util.List;

public class SegmentEntry {

    private String identifier;
    private String name;
    private int sequence;
    private boolean enabled;
    private int length;
    private boolean naturalAccountSegment;
    private boolean costCenterSegment;
    private List<SegmentValueEntry> segmentValues = new ArrayList<SegmentValueEntry>();

    public String getIdentifier() {
	return identifier;
    }

    public void setIdentifier(String identifier) {
	this.identifier = identifier;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getSequence() {
	return sequence;
    }

    public void setSequence(int sequence) {
	this.sequence = sequence;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    public int getLength() {
	return length;
    }

    public void setLength(int length) {
	this.length = length;
    }

    public boolean isNaturalAccountSegment() {
	return naturalAccountSegment;
    }

    public void setNaturalAccountSegment(boolean naturalAccountSegment) {
	this.naturalAccountSegment = naturalAccountSegment;
    }

    public boolean isCostCenterSegment() {
	return costCenterSegment;
    }

    public void setCostCenterSegment(boolean costCenterSegment) {
	this.costCenterSegment = costCenterSegment;
    }

    public List<SegmentValueEntry> getSegmentValues() {
	return segmentValues;
    }

    public void setSegmentValues(List<SegmentValueEntry> segmentValues) {
	this.segmentValues = segmentValues;
    }
}
