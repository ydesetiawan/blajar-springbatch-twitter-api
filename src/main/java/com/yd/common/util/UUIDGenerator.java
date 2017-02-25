package com.yd.common.util;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * @author edys
 * @version 3.0.0, Feb 11, 2014
 * @since 3.0.0
 */
public class UUIDGenerator extends org.hibernate.id.UUIDGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		Serializable id = session.getEntityPersister(null, object)
				.getClassMetadata().getIdentifier(object, session);
		return id != null ? id : super.generate(session, object);
	}

}
