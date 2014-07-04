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
package com.stagecents.gl.api.service;

import java.util.Iterator;

import javax.annotation.Resource;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.stereotype.Component;

import com.stagecents.common.ValidationException;
import com.stagecents.gl.api.command.CreateStructureCommand;
import com.stagecents.gl.api.command.DeleteSegmentValueCommand;
import com.stagecents.gl.api.command.FrozenStructureException;
import com.stagecents.gl.api.command.SegmentDTO;
import com.stagecents.gl.domain.Segment;
import com.stagecents.gl.domain.SegmentValue;
import com.stagecents.gl.domain.Structure;
import com.stagecents.gl.domain.StructureId;
import com.stagecents.gl.query.repository.StructureRepository;

@Component
public class StructureCommandHandler implements StructureService {

    private Repository<Structure> repository;
    private StructureRepository structureRepository;

    @Resource(name = "structureJpaRepository")
    public void setRepository(Repository<Structure> repository) {
	this.repository = repository;
    }

    @Resource(name = "structureQueryRepository")
    public void setStructureRepository(StructureRepository structureRepository) {
	this.structureRepository = structureRepository;
    }

    @CommandHandler
    public void createStructure(CreateStructureCommand command) {
	// Structure validation - see fnd_flex_key_api
	checkUniqueName(command.getStructureId(), command.getStructureDTO()
		.getName());

	// Segment validation
	SegmentDTO[] a = new SegmentDTO[command.getStructureDTO().getSegments()
		.size()];
	SegmentDTO[] segments = command.getStructureDTO().getSegments()
		.toArray(a);
	for (int ix = 0; ix < segments.length; ix++) {
	    // 1. Test duplicate name - see fnd_flex_key_api
	    checkDuplicateSegment(segments, ix);
	}

	Structure structure = new Structure(command.getStructureDTO());
	repository.add(structure);
    }

    @CommandHandler
    public void deleteSegmentValue(DeleteSegmentValueCommand command) {
	Structure structure = repository.load(command.getStructureId());
	if (structure.isFrozen()) {
	    throw new FrozenStructureException();
	}

	Iterator<Segment> iter = structure.getSegments().iterator();
	while (iter.hasNext()) {
	    Segment segment = iter.next();
	    if (segment.getSegmentId().equals(command.getSegmentId())) {
		Iterator<SegmentValue> it = segment.getSegmentValues()
			.iterator();
		while (it.hasNext()) {
		    SegmentValue sv = it.next();
		    if (sv.getSegmentValueId().equals(
			    command.getSegmentValueId())) {
			segment.deleteSegmentValue(sv);
			return;
		    }
		}
	    }
	}
    }

    private void checkUniqueName(StructureId structureId, String name) {
	boolean isUnique = structureRepository.checkDuplicateStructure(
		structureId, name);
	if (!isUnique) {
	    throw new ValidationException("structure name already exists");
	}
    }

    private void checkDuplicateSegment(SegmentDTO[] segments, int index) {
	SegmentDTO segment = segments[index];
	for (int i = 0; i < segments.length; i++) {
	    if ((i != index) && segments[i].getName().equals(segment.getName())) {
		throw new ValidationException("duplicate segment name");
	    }
	}
    }
}
