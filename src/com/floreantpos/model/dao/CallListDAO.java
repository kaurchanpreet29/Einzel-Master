package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.CallList;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.Tax;
import com.floreantpos.model.TicketType;

public class CallListDAO extends BaseCallListDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public CallListDAO () {}
	
	@SuppressWarnings("unchecked")
	public List<MenuItem> findById(boolean includeInvisibleItems,String itemId) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_ITEM_ID, itemId));
					
			if(!includeInvisibleItems) {
				criteria.add(Restrictions.eq(MenuItem.PROP_VISIBLE, Boolean.TRUE));
			}
			
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