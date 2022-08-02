package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.PosException;
import com.floreantpos.model.Gutschein;

public class GutscheinDAO extends BaseGutscheinDAO {

	public GutscheinDAO () {}

	public List<Gutschein> findOpenGutscheins()
	{
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq("closed", false));
			criteria.add(Restrictions.ge("expiryDate", DateUtils.startOfDay(new Date())));
			return criteria.list();

		} finally {
			closeSession(session);
		}
	}

	public Gutschein findByBarcode(String barcode) throws PosException {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq("barcode", barcode));
			return (Gutschein) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}


	@SuppressWarnings("unchecked")
	public List<Gutschein> findByDates(Date fromDate, Date toDate, boolean closed) throws PosException {
		Session session = null;	    
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq("closed", closed));
			criteria.add(Restrictions.ge(Gutschein.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Gutschein.PROP_CREATE_DATE,toDate));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Gutschein> findByDates(Date fromDate, Date toDate) throws PosException {
		Session session = null;	    
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Gutschein.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Gutschein.PROP_CREATE_DATE,toDate));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}


	public List<Gutschein> findRechnugId(String rechnugsId) throws PosException {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Gutschein.PROP_BILL_NR, rechnugsId));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}