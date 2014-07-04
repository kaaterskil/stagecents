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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import com.stagecents.common.IdentifierFactory;
import com.stagecents.gl.domain.AccountCode;
import com.stagecents.gl.domain.AccountCodeComparator;
import com.stagecents.gl.domain.AccountType;
import com.stagecents.gl.domain.Segment;
import com.stagecents.gl.domain.SegmentValue;
import com.stagecents.gl.domain.Structure;

public class AccountGenerator {
    Structure structure;

    public AccountGenerator(Structure structure) {
	this.structure = structure;
    }

    /**
     * Returns a sorted set of of AccountCode objects representing the cartesian
     * product of all account segment values.
     * 
     * @return A sorted set of AccountCode objects representing the cartesian
     *         product of all account codes.
     * @see stackoverflow.com/questions/9591561/java-cartesian-product-of-
     *      a-list-of-lists
     */
    public SortedSet<AccountCode> generate() {
	int n = structure.getSegments().size();
	int solutions = getNumCombinations();

	SortedSet<AccountCode> res = new TreeSet<AccountCode>(
		new AccountCodeComparator());
	for (int i = 0; i < solutions; i++) {
	    String accountCodeId = IdentifierFactory.getInstance()
		    .generateIdentifier();
	    StringBuilder sb = new StringBuilder();
	    AccountType accountType = null;
	    List<String> segmentValues = new ArrayList<String>(n);

	    int j = 1;
	    Iterator<Segment> iter = structure.getSegments().iterator();
	    while (iter.hasNext()) {
		Segment segment = iter.next();

		// Convert set to list so we can access elements by index
		Vector<SegmentValue> vector = segment
			.getSegmentValuesAsVector();

		// The modulo result is interpreted by the vector as an
		// integer, and therefore is rounded down in cases where
		// the result otherwise would be a floating point number.
		SegmentValue sv = vector.get((i / j) % vector.size());

		sb.append(sv.getDescription());
		if (iter.hasNext()) {
		    sb.append(".");
		}
		if (segment.isNaturalAccountSegment()) {
		    accountType = sv.getAccountType();
		}

		// TODO Implement creation of account codes from a segment value
		// that represents a range of values.
		segmentValues.add(sv.getValue());

		j = (j * vector.size());
	    }

	    String description = sb.toString();
	    String value = concatenateValues(segmentValues);
	    res.add(new AccountCode(accountCodeId, structure, accountType,
		    description, value, segmentValues));
	}
	return res;
    }
    
    public int getNumCombinations() {
	int n = 1;
	Iterator<Segment> iter = structure.getSegments().iterator();
	while (iter.hasNext()) {
	    n *= iter.next().getSegmentValues().size();
	}
	return n;
    }
    
    private String concatenateValues(List<String> values) {
	StringBuilder sb = new StringBuilder();
	Iterator<String> iter = values.iterator();
	while(iter.hasNext()) {
	    sb.append(iter.next());
	    if(iter.hasNext()) {
		sb.append(".");
	    }
	}
	return sb.toString();
    }
}
