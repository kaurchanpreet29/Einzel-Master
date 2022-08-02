package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.Tax;
import com.floreantpos.model.TicketType;

public class MenuItemDAO extends BaseMenuItemDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuItemDAO () {}
	
	public MenuItem initialize(MenuItem menuItem) {
		if(menuItem.getId() == null) return menuItem;
		
		Session session = null;
		
		try {
			session = createNewSession();
			menuItem = (MenuItem) session.merge(menuItem);
			
			Hibernate.initialize(menuItem.getMenuItemModiferGroups());
			
			List<MenuItemModifierGroup> menuItemModiferGroups = menuItem.getMenuItemModiferGroups();
			if (menuItemModiferGroups != null) {
				for (MenuItemModifierGroup menuItemModifierGroup : menuItemModiferGroups) {
					Hibernate.initialize(menuItemModifierGroup.getModifierGroup().getModifiers());
				}
			}
			
			Hibernate.initialize(menuItem.getShifts());
			 Hibernate.initialize(menuItem.getMenuitemprice());
			
			return menuItem;
		} finally {
			closeSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	public List<MenuItem> findByParent(MenuGroup group, boolean includeInvisibleItems) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_PARENT, group));
						
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
	@SuppressWarnings("unchecked")
	public List<MenuItem> findBarcodeItems() throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.isNotNull(MenuItem.PROP_BARCODE));
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
		
	@SuppressWarnings("unchecked")
	public List<MenuItem> findByIdPriceCategory(String itemId, int priceCategory) throws PosException {
		Session session = null;		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_ITEM_ID, itemId));
			criteria.add(Restrictions.eq(MenuItem.PROP_PRICE_CATEGORY, priceCategory));
			criteria.add(Restrictions.eq(MenuItem.PROP_VISIBLE, Boolean.TRUE));				
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
	public List<MenuItem> findById(String itemId) throws PosException {
		Session session = null;		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_ITEM_ID, itemId));
			criteria.add(Restrictions.eq(MenuItem.PROP_VISIBLE, Boolean.TRUE));				
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
	public List<MenuItem> findByIdType(boolean includeInvisibleItems,String itemId,String type) throws PosException {
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
	
	
	public MenuItem loadMenuItem(int id) {
        Session session = createNewSession();

        MenuItem menuItem = (MenuItem) session.get(getReferenceClass(), id);

        if (menuItem == null)
            return null;
       
        Hibernate.initialize(menuItem.getMenuitemprice());
        Hibernate.initialize(menuItem.getMenuItemModiferGroups());
        Hibernate.initialize(menuItem.getShifts());
        
        session.close();
        return menuItem;
    }
	
	
	
	public List<MenuItem> findByIdTypeNamePrice(String itemId,String type,Double price,String name) throws PosException {
		Session session = null;		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_ITEM_ID, itemId));
			criteria.add(Restrictions.eq(MenuItem.PROP_NAME, name));
			criteria.add(Restrictions.eq(MenuItem.PROP_PRICE, price));
			criteria.add(Restrictions.eq(MenuItem.PROP_VISIBLE, Boolean.TRUE));
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
	
	public List<MenuItem> findByBarcodePrice(String barcode, int priceCategory) throws PosException {
		Session session = null;		
		System.out.println("findByBarcodePrice_"+barcode + ", priceCategory:"+ priceCategory);
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_BARCODE, barcode));
			criteria.add(Restrictions.eq(MenuItem.PROP_PRICE_CATEGORY, priceCategory));			 
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
	
	public List<MenuItem> findByBarcode1Price(String barcode, int priceCategory) throws PosException {
		Session session = null;		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_BARCODE1, barcode));
			criteria.add(Restrictions.eq(MenuItem.PROP_PRICE_CATEGORY, priceCategory));
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
	
	public List<MenuItem> findByBarcode2Price(String barcode, int priceCategory) throws PosException {
		Session session = null;		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_BARCODE2, barcode));
			criteria.add(Restrictions.eq(MenuItem.PROP_PRICE_CATEGORY, priceCategory));
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
	
	public List<MenuItem> findByBarcode(String barcode) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_BARCODE, barcode));
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
	public List<MenuItem> findByBarcode1(String barcode) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_BARCODE1, barcode));
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
	
	public List<MenuItem> findByBarcode2(String barcode) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_BARCODE2, barcode));
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
	/*@SuppressWarnings("unchecked")
	public List<MenuItem> findByType() throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_TPYE, Boolean.TRUE));
		
			M
			}
			else
			{
				criteria.add(Restrictions.eq(MenuItem.PROP_TPYE, Boolean.FALSE));
			}
			
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
	*/
	//added
	public List<MenuItem> findAllItemsSorted() {
        Session session = null;
        session = getSession();
        Criteria criteria = session.createCriteria(getReferenceClass());
        criteria.addOrder(new org.hibernate.criterion.Order(MenuItem.PROP_ITEM_ID, true) {
            @Override
            public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
                return "cast(itemId as int)";
            }
        });
        return criteria.list();
    }
	//till here
	public List<MenuItemModifierGroup> findModifierGroups(MenuItem item) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_ID, item.getId()));
			MenuItem newItem = (MenuItem) criteria.uniqueResult();
			Hibernate.initialize(newItem.getMenuItemModiferGroups());

			return newItem.getMenuItemModiferGroups();
		} catch (Exception e) {
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}