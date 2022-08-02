package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.Salesreportdb;

public class SalesReportDAO extends BaseSalesReportDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public SalesReportDAO () {}
	
	@SuppressWarnings("unchecked")
	public List<Salesreportdb> findById(int id) throws PosException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Salesreportdb.PROP_SALES_ID, id));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Fehler");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	@SuppressWarnings("unchecked")
	public List<Salesreportdb> findByDate(Date date) throws PosException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Salesreportdb.PROP_DATE, date));			
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Fehler");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	
	public List<Salesreportdb> findAllSaleReport(Date startDate, Date endDate) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Salesreportdb.PROP_DATE, startDate));
			criteria.add(Restrictions.le(Salesreportdb.PROP_DATE, endDate));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<Salesreportdb> findDateReports(Date startDate, Date endDate) {
	    Session session = null;
	    try {
	      session = getSession();
	      Criteria criteria = session.createCriteria(getReferenceClass());
	      criteria.add(Restrictions.ge(Salesreportdb.PROP_DATE, startDate));
	      criteria.add(Restrictions.le(Salesreportdb.PROP_DATE, endDate));
	      return criteria.list();
	    } finally {
	      closeSession(session);
	    }
	  }
	
	
	
	public Salesreportdb loadFull(int id) {
    Session session = createNewSession();
    
    Salesreportdb salesReport = (Salesreportdb) session.get(getReferenceClass(), id);
    
    if(salesReport == null) return null;
    
    Hibernate.initialize(salesReport.getMenuUsages());
    session.close();
    
    return salesReport;
  }

}