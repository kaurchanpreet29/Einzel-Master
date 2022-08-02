package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.TicketType;


public class MenuGroupDAO extends BaseMenuGroupDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuGroupDAO () {}

	@SuppressWarnings("unchecked")
	public List<MenuGroup> findEnabledByParent(MenuCategory category) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuGroup.PROP_VISIBLE, Boolean.TRUE));
			criteria.add(Restrictions.eq(MenuGroup.PROP_PARENT, category));
				
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MenuGroup> findByGroupType(String groupType) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuGroup.PROP_VISIBLE, Boolean.TRUE));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<MenuGroup> findByLink(String link) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuGroup.PROP_LINK, link));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	@SuppressWarnings("unchecked")
	public MenuGroup findById(int id) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuGroup.PROP_ID, id));
			MenuGroup grp = (MenuGroup)criteria.uniqueResult();
			return grp;
			
		} finally {
			closeSession(session);
		}
	}
	
	public List<MenuGroup> findByName(String name) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuGroup.PROP_NAME, name));
			return criteria.list();
			
		} finally {
			closeSession(session);
		}
	}
	public List<MenuGroup> findByParent(MenuCategory id) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuGroup.PROP_PARENT, id));
			return criteria.list();
			
		} finally {
			closeSession(session);
		}
	}
	public List<MenuGroup> findByParent(MenuCategory id,String type) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuGroup.PROP_PARENT, id));
			return criteria.list();
			
		} finally {
			closeSession(session);
		}
	}
}