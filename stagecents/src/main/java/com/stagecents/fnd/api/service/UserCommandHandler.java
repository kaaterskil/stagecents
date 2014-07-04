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
package com.stagecents.fnd.api.service;

import javax.annotation.Resource;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import com.stagecents.fnd.api.command.AuthenticateUserCommand;
import com.stagecents.fnd.api.command.ChangeUserCredentialsCommand;
import com.stagecents.fnd.api.command.ChangeUserEmailCommand;
import com.stagecents.fnd.api.command.CreateUserCommand;
import com.stagecents.fnd.api.command.DisableUserCommand;
import com.stagecents.fnd.api.command.InvalidCredentialsException;
import com.stagecents.fnd.api.command.InvalidUserEndDateException;
import com.stagecents.fnd.api.command.InvalidUsernameException;
import com.stagecents.fnd.api.command.UsernameNotUniqueException;
import com.stagecents.fnd.domain.User;
import com.stagecents.fnd.query.repository.UserRepository;

/**
 * Command handler that can be use used to create and update system users.
 * 
 * @author Blair Caple
 */
@Component
public class UserCommandHandler implements UserService {

    private Repository<User> repository;
    private UserRepository userRepository;

    @Resource(name = "userJpaRepository")
    public void setRepository(Repository<User> repository) {
	this.repository = repository;
    }

    @Resource(name = "userQueryRepository")
    public void setUserRepository(UserRepository userRepository) {
	this.userRepository = userRepository;
    }

    @CommandHandler
    public void createUser(CreateUserCommand command) {
	validateUsername(command.getUsername());
	validateUsernameUnique(command.getUsername());

	User user = new User(command);
	repository.add(user);
    }

    @CommandHandler
    public void authenticateUser(AuthenticateUserCommand command) {
	User user = userRepository.findByUsername(command.getUsername());
	if (user == null) {
	    throw new InvalidUsernameException("invalid username");
	}

	String encryptedCredentials = DigestUtils.encodeCredentials(command
		.getCredentials());
	user.authenticate(encryptedCredentials, command.getLoginTime());
    }

    @CommandHandler
    public void disableUser(DisableUserCommand command) {
	LocalDate today = new LocalDate();
	if (!command.getEndDate().isAfter(today)) {
	    throw new InvalidUserEndDateException(
		    "end date cannot be in the future");
	}

	User user = repository.load(command.getUserId());
	user.disableUser(command.getEndDate());
    }

    @CommandHandler
    public void changeCredentials(ChangeUserCredentialsCommand command) {
	User user = repository.load(command.getUserId());

	String newCredentials = DigestUtils.encodeCredentials(command
		.getNewCredentials());
	if (newCredentials.equals(user.getEncryptedCredentials())) {
	    throw new InvalidCredentialsException(
		    "new password must be different from old password");
	}
	user.changeCredentials(newCredentials, command.getChangeDate());
    }

    @CommandHandler
    public void changeEmail(ChangeUserEmailCommand command) {
	User user = repository.load(command.getUserId());
	user.changeEmail(command.getEmail());
    }

    private void validateUsername(String username) {
	if (username == null || username.trim() == null) {
	    throw new InvalidUsernameException("null user name");

	} else if (!(username.trim().equals(username))) {
	    throw new InvalidUsernameException("invalid space in user name");

	} else if (username.matches("[/\"(*+,;<>\\~:]+")) {
	    throw new InvalidUsernameException("invalid user name");
	}
    }

    private void validateUsernameUnique(String username) {
	User user = userRepository.findByUsername(username);
	if (user != null) {
	    throw new UsernameNotUniqueException(username);
	}
    }

}
