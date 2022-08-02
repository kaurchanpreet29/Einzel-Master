package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Tax;
import com.floreantpos.model.Ticket;



public class TaxDAO extends BaseTaxDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public TaxDAO () {}

	public Tax findByName(String name)
	{
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq("name", name));
			return (Tax) criteria.uniqueResult();
			
		} finally {
			closeSession(session);
		}
	}
	
	public Tax findById(int id)
	{
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Tax.PROP_ID, id));
			return (Tax) criteria.uniqueResult();
			
		} finally {
			closeSession(session);
		}
	}
}