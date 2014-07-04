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

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Represents the smallest categorized expenditure units charges to projects and
 * tasks.
 * 
 * @author Blair Caple
 */
public class ExpenditureItem {

    private Expenditure expenditure;
    private Activity activity;
    private Project project;
    private LocalDate date;
    private ExpenditureType type;

    private Unit uom;
    private int quantity;
    private float rawCost;
    private float rawCostRate;
    private LaborCostMultiplier laborCostMultiplier;
    private SystemLinkage systemLinkage;

    /**
     * Flag that indicates whether the item has been cost distributed. Upon
     * entry, this flag is set to false. When the item is cost distributed, the
     * flag is set to true.
     */
    private boolean costDistributed = false;

    // Unidirectional one-to-many association. Join table is named
    // PA_EXPENDITURE_COMMENTS
    private List<ExpenditureComment> comments = new ArrayList<ExpenditureItem.ExpenditureComment>();

    ExpenditureItem() {
    }

    public ExpenditureItem(LocalDate date, ExpenditureType type, Unit uom,
	    int quantity, float rawCost, float rawCostRate,
	    LaborCostMultiplier laborCostMultiplier, SystemLinkage systemLinkage) {
	this.date = date;
	this.type = type;
	this.uom = uom;
	this.quantity = quantity;
	this.rawCost = rawCost;
	this.rawCostRate = rawCostRate;
	this.laborCostMultiplier = laborCostMultiplier;
	this.systemLinkage = systemLinkage;
    }

    void setExpenditure(Expenditure expenditure) {
	this.expenditure = expenditure;
    }

    void setActivity(Activity activity) {
	this.activity = activity;
    }

    void setProject(Project project) {
	this.project = project;
    }

    public LocalDate getDate() {
        return date;
    }

    public void addComment(ExpenditureComment arg) {
	comments.add(arg);
    }

    public void removeComment(ExpenditureComment arg) {
	comments.remove(arg);
    }

    /**
     * Represents free text entered for expenditure items to explain or further
     * describe work performed.
     */
    public static class ExpenditureComment {
	private String comment;
	private DateTime commentDate;

	ExpenditureComment() {
	}

	public ExpenditureComment(String comment) {
	    this.comment = comment;
	    this.commentDate = new DateTime();
	}

	public String getComment() {
	    return comment;
	}

	public DateTime getCommentDate() {
	    return commentDate;
	}
    }
}
