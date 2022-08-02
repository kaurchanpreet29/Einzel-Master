package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.CashBookData;

public class CashbookDataDAO extends BaseCashBookDataDAO{
	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public CashbookDataDAO () {}
	

  public List<CashBookData> findByBeschreibung(String beschreibung) {
    Session session = null;

    try {
      session = getSession();
      Criteria criteria = session.createCriteria(getReferenceClass());
      criteria.add(Restrictions.eq("beschreibung",beschreibung));
      List list = criteria.list();
      return list;
    } finally {
      closeSession(session);
    }
  } 
  
  
}
