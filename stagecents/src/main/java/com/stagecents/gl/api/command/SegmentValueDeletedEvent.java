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
package com.stagecents.gl.api.command;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import com.stagecents.common.AggregateId;

public class SegmentValueDeletedEvent {

    @TargetAggregateIdentifier
    private AggregateId structureId;
    private String segmentId;
    private String segmentValueId;

    public SegmentValueDeletedEvent(AggregateId structureId, String segmentId,
	    String segmentValueId) {
	this.structureId = structureId;
	this.segmentId = segmentId;
	this.segmentValueId = segmentValueId;
    }

    public AggregateId getStructureId() {
	return structureId;
    }

    public String getSegmentId() {
	return segmentId;
    }

    public String getSegmentValueId() {
	return segmentValueId;
    }
}
