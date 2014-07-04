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
package com.stagecents.common.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

import com.stagecents.common.AggregateId;

public class AggregateIdType implements UserType {

    public int[] sqlTypes() {
	return new int[] { StringType.INSTANCE.sqlType() };
    }

    public Class returnedClass() {
	return AggregateId.class;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
	if (x == y) {
	    return true;
	}
	if (x != null && y != null && x.equals(y)) {
	    return true;
	}
	return false;
    }

    public int hashCode(Object x) throws HibernateException {
	if (x != null) {
	    return x.hashCode();
	}
	return 0;
    }

    public Object nullSafeGet(ResultSet rs, String[] names,
	    SessionImplementor session, Object owner)
	    throws HibernateException, SQLException {
	String id = (String) StringType.INSTANCE.get(rs, names[0], session);
	return new AggregateId(id);
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index,
	    SessionImplementor session) throws HibernateException, SQLException {
	if (value == null) {
	    StringType.INSTANCE.set(st, null, index, session);
	} else {
	    String id = value.toString();
	    StringType.INSTANCE.set(st, id, index, session);
	}
    }

    public Object deepCopy(Object value) throws HibernateException {
	return value;
    }

    public boolean isMutable() {
	return false;
    }

    public Serializable disassemble(Object value) throws HibernateException {
	return value.toString();
    }

    public Object assemble(Serializable cached, Object owner)
	    throws HibernateException {
	return new AggregateId((String) cached);
    }

    public Object replace(Object original, Object target, Object owner)
	    throws HibernateException {
	return original;
    }

}
