package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Cashbook;
public class CashbookDAO extends BaseCashbookDAO{
	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public CashbookDAO () {}
	
	public List<Cashbook> findByDate(Date startDate, Date endDate) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Cashbook.PROP_DATE, startDate));
			criteria.add(Restrictions.le(Cashbook.PROP_DATE, endDate));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	public List<Cashbook> findByDate(Date startDate, Date endDate,String beschreibung) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Cashbook.PROP_DATE, startDate));
			criteria.add(Restrictions.le(Cashbook.PROP_DATE, endDate));
			criteria.add(Restrictions.le(Cashbook.PROP_BESCHREIBUNG, beschreibung));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	public List<Cashbook> findByDateAndKonto(Date startDate, Date endDate,Integer konto) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Cashbook.PROP_DATE, startDate));
			criteria.add(Restrictions.le(Cashbook.PROP_DATE, endDate));
			criteria.add(Restrictions.eq(Cashbook.PROP_KONTO, konto));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
}
