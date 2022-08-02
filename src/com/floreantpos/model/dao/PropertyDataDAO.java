package com.floreantpos.model.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Gratuity;
import com.floreantpos.model.PropertyData;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;

public class PropertyDataDAO extends BasePropertyDataDAO{
	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PropertyDataDAO () {}
	
	public List<PropertyData> getProperty(String name) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq("propertytext", name));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
}
