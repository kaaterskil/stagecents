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
package com.stagecents.fnd.query.repository.hibernate;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.stagecents.common.hibernate.BaseJpaRepository;
import com.stagecents.fnd.domain.User;
import com.stagecents.fnd.domain.UserId;
import com.stagecents.fnd.query.repository.UserRepository;

@Repository
public class UserQueryRepository extends BaseJpaRepository<User, UserId>
	implements UserRepository {

    public User findByEmailAddress(String emailAddress) {
	String hql = "from User u where u.emailAddress = :emailAddress";
	Query q = getSession().createQuery(hql);
	q.setString("emailAddress", emailAddress);
	try {
	    return (User) q.uniqueResult();
	} catch (NonUniqueResultException e) {
	    return null;
	}
    }

    public User findByUsername(String username) {
	String hql = "from User u where u.username = :username";
	Query q = getSession().createQuery(hql);
	q.setString("username", username);
	try {
	    return (User) q.uniqueResult();
	} catch (NonUniqueResultException e) {
	    return null;
	}
    }

    public boolean isUsernameUnique(String username) {
	User user = findByUsername(username);
	return (user == null) ? true : false;
    }

}
