package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.TicketType;

public class MenuCategoryDAO extends BaseMenuCategoryDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuCategoryDAO() {
	}

	public List<MenuCategory> findAllEnable() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_VISIBLE, Boolean.TRUE));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	public List<MenuCategory> findByName(String name) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_NAME, name));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public MenuCategory findByNameUnique(String name) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_NAME, name));
			return (MenuCategory) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}
	
	
	
	
	public List<MenuCategory> findByCategory(String name) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_TYPE, name));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<MenuCategory> findNonBevegares(TicketType type) {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_VISIBLE, Boolean.TRUE));
			criteria.add(Restrictions.or(Restrictions.isNull(MenuCategory.PROP_BEVERAGE), Restrictions.eq(MenuCategory.PROP_BEVERAGE, Boolean.FALSE)));
			criteria.add(Restrictions.eq(MenuCategory.PROP_PRIORITY, Boolean.FALSE));
			if (type == TicketType.DINE_IN)
				criteria.add(Restrictions.eq(MenuCategory.PROP_TYPE, POSConstants.DINE_IN));
			else
				criteria.add(Restrictions.eq(MenuCategory.PROP_TYPE, POSConstants.HOME_DELIVERY));
			
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<MenuCategory> findBevegares(TicketType type) {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_VISIBLE, Boolean.TRUE));
			criteria.add(Restrictions.eq(MenuCategory.PROP_BEVERAGE, Boolean.TRUE));
			criteria.add(Restrictions.eq(MenuCategory.PROP_PRIORITY, Boolean.FALSE));
			if (type == TicketType.DINE_IN)
			{
				criteria.add(Restrictions.eq(MenuCategory.PROP_TYPE, POSConstants.DINE_IN));
			}
			else
			{	
				criteria.add(Restrictions.eq(MenuCategory.PROP_TYPE, POSConstants.HOME_DELIVERY));
			}
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	public List<MenuCategory> findPriority(TicketType type) {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_VISIBLE, Boolean.TRUE));
			criteria.add(Restrictions.eq(MenuCategory.PROP_PRIORITY, Boolean.TRUE));
			if (type == TicketType.DINE_IN)
			{
				criteria.add(Restrictions.eq(MenuCategory.PROP_TYPE, POSConstants.DINE_IN));
			}
			else
			{	
				criteria.add(Restrictions.eq(MenuCategory.PROP_TYPE, POSConstants.HOME_DELIVERY));
			}
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MenuCategory> findByIdPriceCategory(int priceCategory) throws PosException {
		Session session = null;		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_VISIBLE, Boolean.TRUE));
			criteria.add(Restrictions.eq(MenuCategory.PROP_PRIORITY, Boolean.TRUE));
			criteria.add(Restrictions.eq(MenuCategory.PROP_PRICE_CATEGORY, priceCategory));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PosException("Error occured while finding food items");
		} finally {
			closeSession(session);
		}
	}
	
}