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
package com.stagecents.common.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;

import com.stagecents.common.infrastructure.BaseRepository;

public class BaseJpaRepository<E, K extends Serializable> implements
	BaseRepository<E, K> {

    private SessionFactory sessionFactory;
    private Class<E> persistentClass;

    public BaseJpaRepository() {
	this.persistentClass = (Class<E>) ((ParameterizedType) getClass()
		.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Autowired(required = true)
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
	return sessionFactory.getCurrentSession();
    }

    public Class<E> getPersistentClass() {
	return persistentClass;
    }

    public E findById(K identifier, boolean lock) {
	E entity;
	if (lock) {
	    entity = (E) getSession().load(getPersistentClass(), identifier,
		    LockOptions.UPGRADE);
	} else {
	    entity = (E) getSession().load(getPersistentClass(), identifier);
	}
	return entity;
    }

    public List<E> findAll() {
	return findByCriteria();
    }

    public List<E> findByExample(E exampleInstance, String... excludeProperty) {
	Criteria crit = getSession().createCriteria(getPersistentClass());
	Example example = Example.create(exampleInstance);
	for(String exclude : excludeProperty) {
	    example.excludeProperty(exclude);
	}
	crit.add(example);
	return crit.list();
    }

    protected List<E> findByCriteria(Criterion... criterion) {
	Criteria crit = getSession().createCriteria(getPersistentClass());
	for (Criterion c : criterion) {
	    crit.add(c);
	}
	return crit.list();
    }

}
