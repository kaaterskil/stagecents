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
package com.stagecents.fnd.api.command;

import org.joda.time.LocalDate;

import com.stagecents.fnd.domain.UserId;

public class UserCreatedEvent {

    private final UserId userId;
    private final String username;
    private final String encryptedCredentials;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate passwordDate;
    private final String description;
    private final String emailAddress;

    public UserCreatedEvent(UserId userId, String username,
	    String encryptedCredentials, LocalDate startDate,
	    LocalDate endDate, LocalDate passwordDate, String description,
	    String emailAddress) {
	this.userId = userId;
	this.username = username;
	this.encryptedCredentials = encryptedCredentials;
	this.startDate = startDate;
	this.endDate = endDate;
	this.passwordDate = passwordDate;
	this.description = description;
	this.emailAddress = emailAddress;
    }

    public UserId getUserId() {
	return userId;
    }

    public String getUsername() {
	return username;
    }

    public String getEncryptedCredentials() {
	return encryptedCredentials;
    }

    public LocalDate getStartDate() {
	return startDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public LocalDate getPasswordDate() {
	return passwordDate;
    }

    public String getDescription() {
	return description;
    }

    public String getEmailAddress() {
	return emailAddress;
    }
}
