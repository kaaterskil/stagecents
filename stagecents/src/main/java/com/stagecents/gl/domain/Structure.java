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
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import com.stagecents.gl.api.command.ChartOfAccountsGeneratedEvent;
import com.stagecents.gl.api.command.CreateSegmentValueCommand;
import com.stagecents.gl.api.command.DisableStructureCommand;
import com.stagecents.gl.api.command.EnableStructureCommand;
import com.stagecents.gl.api.command.FreezeStructureCommand;
import com.stagecents.gl.api.command.FrozenStructureException;
import com.stagecents.gl.api.command.GenerateChartOfAccountsCommand;
import com.stagecents.gl.api.command.SegmentDTO;
import com.stagecents.gl.api.command.SegmentValueCreatedEvent;
import com.stagecents.gl.api.command.SegmentValueDTO;
import com.stagecents.gl.api.command.StructureCreatedEvent;
import com.stagecents.gl.api.command.StructureDTO;
import com.stagecents.gl.api.command.StructureDisabledEvent;
import com.stagecents.gl.api.command.StructureEnabledEvent;
import com.stagecents.gl.api.command.StructureFrozenEvent;
import com.stagecents.gl.api.command.StructureUnfrozenEvent;
import com.stagecents.gl.api.command.UnfreezeStructureCommand;
import com.stagecents.gl.api.service.AccountGenerator;

/**
 * Represents structural information about the general account segment
 * structure.
 * 
 * @author Blair Caple
 */
public class Structure extends AbstractAnnotatedAggregateRoot<StructureId> {

    @AggregateIdentifier
    private StructureId structureId;
    private int version;

    private String name;
    private boolean enabled;
    private boolean frozen;
    private SortedSet<Segment> segments = new TreeSet<Segment>(
	    new SegmentComparator());
    private SortedSet<AccountCode> chartOfAccounts = new TreeSet<AccountCode>(
	    new AccountCodeComparator());

    Structure() {
    }

    public Structure(StructureDTO data) {
	apply(new StructureCreatedEvent(data.getStructureId(), data));
    }

    @Override
    public StructureId getIdentifier() {
	return structureId;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public boolean isFrozen() {
	return frozen;
    }

    public SortedSet<Segment> getSegments() {
	return segments;
    }

    public SortedSet<AccountCode> getChartOfAccounts() {
        return chartOfAccounts;
    }

    public void addSegment(Segment segment) {
	if (frozen) {
	    throw new FrozenStructureException();
	}
	segment.setStructure(this);
	segments.add(segment);
    }

    @EventHandler
    public void handleCreateStructure(StructureCreatedEvent event) {
	structureId = event.getStructureId();
	name = event.getStructureDTO().getName();
	enabled = event.getStructureDTO().isEnabled();
	frozen = event.getStructureDTO().isFrozen();

	Iterator<SegmentDTO> iter = event.getStructureDTO().getSegments()
		.iterator();
	while (iter.hasNext()) {
	    SegmentDTO data = iter.next();
	    Segment segment = new Segment(data.getSegmentId(), data.getName(),
		    this, data.getSequence(), data.isEnabled(),
		    data.getLength(), data.isNaturalAccountSegment(),
		    data.isCostCenterSegment());
	    addSegment(segment);
	}
    }

    @EventHandler
    public void handleDisableStructure(StructureDisabledEvent event) {
	enabled = false;
    }

    @EventHandler
    public void handleEnableStructure(StructureEnabledEvent event) {
	enabled = true;
    }

    @EventHandler
    public void handleFreezeStructure(StructureFrozenEvent event) {
	frozen = true;
    }

    @EventHandler
    public void handleUnfreezeStructure(StructureUnfrozenEvent event) {
	frozen = false;
    }

    @EventHandler
    public void handleCreateSegmentValue(SegmentValueCreatedEvent event) {
	if (isFrozen()) {
	    throw new FrozenStructureException();
	}

	Segment segment = null;
	Iterator<Segment> iter = segments.iterator();
	while (iter.hasNext()) {
	    segment = iter.next();
	    if (segment.getSegmentId().equals(
		    event.getSegmentValueDTO().getSegmentId())) {
		break;
	    }
	}
	if (segment != null) {
	    SegmentValueDTO sv = event.getSegmentValueDTO();
	    // Validation
	    if (sv.getValue() != null) {
		// 1. Validate value length
		if (sv.getValue().length() > segment.getLength()) {
		    throw new IllegalArgumentException(
			    "value length exceeds allowable length");
		}
	    } else if (sv.getMinimumValue() != null
		    && sv.getMaximumValue() != null) {
		// 1. Validate minimumValue and maximumValue lengths
		if (sv.getMinimumValue().length() > segment.getLength()
			|| sv.getMaximumValue().length() > segment.getLength()) {
		    throw new IllegalArgumentException(
			    "value length exceeds allowable length");
		}
		// 2. Validate maxmimumValue > minimumValue
		if (sv.getMaximumValue().compareTo(sv.getMinimumValue()) < 1) {
		    throw new IllegalArgumentException(
			    "maximum value <= minimum value");
		}

	    } else if (sv.getMinimumValue() != null
		    || sv.getMaximumValue() != null) {
		throw new IllegalArgumentException(
			"invalid minimum value/maximum value combination");
	    }
	    if (sv.getAccountType() != null
		    && !(segment.isNaturalAccountSegment())) {
		throw new IllegalArgumentException("invalid account type");
	    }

	    // Create SegmentValue object
	    SegmentValue segmentValue = new SegmentValue(
		    sv.getSegmentValueId(), segment, segment.getSequence(),
		    sv.getValue(), sv.getDescription(), sv.getMinimumValue(),
		    sv.getMaximumValue(), sv.getAccountType());
	    segment.getSegmentValues().add(segmentValue);
	}
    }

    @EventHandler
    public void handleGenerateChartOfAccounts() {
	if (segments.isEmpty()) {
	    throw new IllegalArgumentException(
		    "Cannot generate COA with no defined segments");
	}

	boolean hasValues = false;
	for (Segment s : segments) {
	    if (!(s.getSegmentValues().isEmpty())) {
		hasValues = true;
		break;
	    }
	}
	if (!hasValues) {
	    throw new IllegalArgumentException(
		    "cannot generate COA with no defined values");
	}

	AccountGenerator generator = new AccountGenerator(this);
	if (chartOfAccounts.size() == generator.getNumCombinations()) {
	    throw new IllegalArgumentException(
		    "chart of accounts already exists");
	}

	SortedSet<AccountCode> coa = generator.generate();
	chartOfAccounts.addAll(coa);
    }

    @CommandHandler
    public void disable(DisableStructureCommand command) {
	apply(new StructureDisabledEvent(structureId));
    }

    @CommandHandler
    public void enable(EnableStructureCommand command) {
	apply(new StructureEnabledEvent(structureId));
    }

    @CommandHandler
    public void freeze(FreezeStructureCommand command) {
	apply(new StructureFrozenEvent(structureId));
    }

    @CommandHandler
    public void unfreeze(UnfreezeStructureCommand command) {
	apply(new StructureUnfrozenEvent(structureId));
    }

    @CommandHandler
    public void createSegmentValue(CreateSegmentValueCommand command) {
	apply(new SegmentValueCreatedEvent(structureId,
		command.getSegmentValueDTO()));
    }

    @CommandHandler
    public void generateChartOfAccounts(GenerateChartOfAccountsCommand command) {
	apply(new ChartOfAccountsGeneratedEvent(structureId));
    }

    private static class SegmentComparator implements Comparator<Segment> {

	public int compare(Segment o1, Segment o2) {
	    int seq1 = o1.getSequence();
	    int seq2 = o2.getSequence();
	    return (seq1 < seq2) ? -1 : ((seq1 == seq2) ? 0 : 1);
	}
    }
}
