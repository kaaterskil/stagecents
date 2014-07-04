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

import com.stagecents.fnd.api.command.AuthenticateUserCommand;
import com.stagecents.fnd.api.command.ChangeUserCredentialsCommand;
import com.stagecents.fnd.api.command.ChangeUserEmailCommand;
import com.stagecents.fnd.api.command.CreateUserCommand;
import com.stagecents.fnd.api.command.DisableUserCommand;

public interface UserService {

    /**
     * Creates a new User from the given data. The given username is tested for
     * uniqueness before continuing.
     * 
     * @param command CreateUserCommand object containing the given data to
     *            create a new user.
     */
    void createUser(CreateUserCommand command);

    /**
     * Authenticates a user's credentials against the stored value.
     * 
     * @param command AuthenticateUserCommand object containing the credentials
     *            to authenticate.
     */
    void authenticateUser(AuthenticateUserCommand command);

    /**
     * Updates a User with a new end date with a value in the past in order to
     * disable it.
     * 
     * @param command DisableUserCommand object containing the effective disable
     *            date.
     */
    void disableUser(DisableUserCommand command);

    /**
     * Updates a User with new credentials.
     * 
     * @param command ChangeUserCredentialsCommand object containing the new
     *            credentials.
     */
    void changeCredentials(ChangeUserCredentialsCommand command);

    /**
     * Updates a User with a new email address.
     * 
     * @param command ChangeUserEmailCommand object containing the new email
     *            address.
     */
    void changeEmail(ChangeUserEmailCommand command);
}
