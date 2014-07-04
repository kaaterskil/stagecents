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
package com.stagecents.fnd.query.repository;

import com.stagecents.common.infrastructure.BaseRepository;
import com.stagecents.fnd.domain.User;
import com.stagecents.fnd.domain.UserId;

public interface UserRepository extends BaseRepository<User, UserId> {

    /**
     * Returns a User object representing the system user whose email address
     * matches the given email address.
     * 
     * @param emailAddress The email address to search.
     * @return The User object representing the system user with the given email
     *         address, or null if no User was found.
     */
    public User findByEmailAddress(String emailAddress);

    /**
     * Returns a User object representing the system user whose username matches
     * the given username.
     * 
     * @param usernam The username to search.e
     * @return The User object representing the system user with the given
     *         username, or null if no User was found.
     */
    public User findByUsername(String username);

    /**
     * Returns true if and only if the given username is unique, false
     * otherwise.
     * 
     * @param username The username to test for uniqueness.
     * @return True if the given username is unique, false otherwise.
     */
    public boolean isUsernameUnique(String username);
}
