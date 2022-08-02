package com.floreantpos.model.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.TSEReceiptData;

public class TSEReceiptDataDAO  extends BaseTSEReceiptDataDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public TSEReceiptDataDAO () {}
	
	public TSEReceiptData findById(Integer id)
	{
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq("id", id));
			return (TSEReceiptData) criteria.uniqueResult();
			
		} finally {
			closeSession(session);
		}
	}
}