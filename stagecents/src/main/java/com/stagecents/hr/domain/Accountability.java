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

public class Accountability {

    static Accountability create(Party parent, Party child,
	    AccountabilityType type) {
	if (!canCreate(parent, child, type)) {
	    throw new IllegalArgumentException("invalid accountability");
	}
	return new Accountability(parent, child, type);
    }

    static boolean canCreate(Party parent, Party child, AccountabilityType type) {
	if (parent.equals(child)) {
	    return false;
	}
	if (parent.ancestorsInclude(child, type)) {
	    return false;
	}
	return true;
    }

    private Party parent;
    private Party child;
    private AccountabilityType type;

    Accountability(Party parent, Party child, AccountabilityType type) {
	this.parent = parent;
	this.child = child;
	this.type = type;
    }

    Party getParent() {
	return parent;
    }

    Party getChild() {
	return child;
    }

    AccountabilityType getType() {
	return type;
    }
}
