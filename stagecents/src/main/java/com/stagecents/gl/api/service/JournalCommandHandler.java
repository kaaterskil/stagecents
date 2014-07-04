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

import javax.annotation.Resource;

import org.axonframework.repository.Repository;
import org.springframework.stereotype.Component;

import com.stagecents.common.ValidationException;
import com.stagecents.gl.api.command.CreateJournalEntryCommand;
import com.stagecents.gl.domain.JournalEntry;
import com.stagecents.gl.query.repository.JournalRepository;

@Component
public class JournalCommandHandler implements JournalService {

    private Repository<JournalEntry> repository;
    private JournalRepository journalRepository;

    @Resource(name = "journalJpaRepository")
    public void setRepository(Repository<JournalEntry> repository) {
	this.repository = repository;
    }

    @Resource(name = "journalQueryRepository")
    public void setJournalRepository(JournalRepository journalRepository) {
	this.journalRepository = journalRepository;
    }

    @Override
    public void createJournalEntry(CreateJournalEntryCommand command) {
	// 1 Make sure the journal has at least two lined
	int numLines = command.getJournalHeaderDTO().getLines().size();
	if(numLines < 2) {
	    throw new ValidationException("journal must have at least two lines");
	}
	
	JournalEntry journalEntry = new JournalEntry(
		command.getJournalHeaderDTO());
	repository.add(journalEntry);
    }

}
