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
package com.stagecents.fnd.domain;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.stagecents.fnd.api.command.CreateUserCommand;
import com.stagecents.fnd.api.command.UserAuthenticatedEvent;
import com.stagecents.fnd.api.command.UserCreatedEvent;
import com.stagecents.fnd.api.command.UserCredentialsChangedEvent;
import com.stagecents.fnd.api.command.UserDisabledEvent;
import com.stagecents.fnd.api.command.UserEmailChangedEvent;
import com.stagecents.fnd.api.service.DigestUtils;

public class User extends AbstractAnnotatedAggregateRoot<UserId> {

    @AggregateIdentifier
    private UserId userId;
    private int version;

    private String username;
    private String encryptedCredentials;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate passwordDate;
    private String description;
    private String emailAddress;

    User() {
    }

    public User(CreateUserCommand command) {
	String encodedCredentials = DigestUtils.encodeCredentials(command
		.getRawCredentials());
	apply(new UserCreatedEvent(command.getUserId(), command.getUsername(),
		encodedCredentials, command.getStartDate(),
		command.getEndDate(), new LocalDate(),
		command.getDescription(), command.getEmailAddress()));
    }

    @Override
    public UserId getIdentifier() {
	return userId;
    }

    public String getUsername() {
	return username;
    }

    public String getEncryptedCredentials() {
	return encryptedCredentials;
    }

    @EventHandler
    public void handleCreateUser(UserCreatedEvent event) {
	userId = event.getUserId();
	username = event.getUsername();
	encryptedCredentials = event.getEncryptedCredentials();
	startDate = event.getStartDate();
	endDate = event.getEndDate();
	passwordDate = event.getPasswordDate();
	description = event.getDescription();
	emailAddress = event.getEmailAddress();
    }

    @EventHandler
    public void handleChangeCredentials(UserCredentialsChangedEvent event) {
	encryptedCredentials = event.getNewCredentials();
	passwordDate = event.getChangeDate();
    }

    @EventHandler
    public void handleDisableUser(UserDisabledEvent event) {
	endDate = event.getEndDate();
    }

    @EventHandler
    public void handleChangeEmailAddress(UserEmailChangedEvent event) {
	emailAddress = event.getNewEmailAddress();
    }

    public boolean authenticate(String credentials, DateTime loginTime) {
	if (!credentials.equals(encryptedCredentials)) {
	    return false;
	}
	apply(new UserAuthenticatedEvent(userId, username, loginTime));
	return true;
    }

    public void changeEmail(String newEmailAddress) {
	apply(new UserEmailChangedEvent(userId, newEmailAddress));
    }

    public void changeCredentials(String newCredentials, LocalDate changeDate) {
	apply(new UserCredentialsChangedEvent(userId, newCredentials,
		changeDate));
    }

    public void disableUser(LocalDate endDate) {
	apply(new UserDisabledEvent(userId, endDate));
    }
}
