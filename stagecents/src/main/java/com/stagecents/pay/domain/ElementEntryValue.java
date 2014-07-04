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
package com.stagecents.pay.domain;

/**
 * Represents the actual values entered for a specific element entry.
 * 
 * @author Blair Caple
 */
public class ElementEntryValue {

    // Bidirectional many-to-one association
    private ElementEntry elementEntry;
    // Unidirectional many-to-one association.
    private InputValue inputValue;
    private float value;

    ElementEntryValue() {
    }

    public ElementEntryValue(ElementEntry elementEntry, InputValue inputValue,
	    float value) {
	this.elementEntry = elementEntry;
	this.inputValue = inputValue;
	this.value = value;
    }

    public ElementEntry getElementEntry() {
	return elementEntry;
    }

    public void setElementEntry(ElementEntry elementEntry) {
	this.elementEntry = elementEntry;
    }

    public InputValue getInputValue() {
	return inputValue;
    }

    public void setInputValue(InputValue inputValue) {
	this.inputValue = inputValue;
    }

    public float getValue() {
	return value;
    }

    public void setValue(float value) {
	this.value = value;
    }
    
    public Class getInputValueClass() {
	return inputValue.getClass();
    }
}
