package com.floreantpos.model.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseMenuItemPriceDAO extends com.floreantpos.model.dao._RootDAO {

	// query name references


	public static MenuItemPriceDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static MenuItemPriceDAO getInstance () {
		if (null == instance) instance = new MenuItemPriceDAO();
		return instance;
	}

	public Class getReferenceClass () {
		return com.floreantpos.model.MenuItemPrice.class;
	}

    public Order getDefaultOrder () {
		return Order.asc("id");
    }

	/**
	 * Cast the object as a com.floreantpos.model.MenuItemPrice
	 */
	public com.floreantpos.model.MenuItemPrice cast (Object object) {
		return (com.floreantpos.model.MenuItemPrice) object;
	}

	public com.floreantpos.model.MenuItemPrice get(java.lang.Integer key)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.MenuItemPrice) get(getReferenceClass(), key);
	}

	public com.floreantpos.model.MenuItemPrice get(java.lang.Integer key, Session s)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.MenuItemPrice) get(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.MenuItemPrice load(java.lang.Integer key)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.MenuItemPrice) load(getReferenceClass(), key);
	}

	public com.floreantpos.model.MenuItemPrice load(java.lang.Integer key, Session s)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.MenuItemPrice) load(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.MenuItemPrice loadInitialize(java.lang.Integer key, Session s) 
			throws org.hibernate.HibernateException { 
		com.floreantpos.model.MenuItemPrice obj = load(key, s); 
		if (!Hibernate.isInitialized(obj)) {
			Hibernate.initialize(obj);
		} 
		return obj; 
	}

/* Generic methods */

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.MenuItemPrice> findAll () {
		return super.findAll();
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.MenuItemPrice> findAll (Order defaultOrder) {
		return super.findAll(defaultOrder);
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
	public java.util.List<com.floreantpos.model.MenuItemPrice> findAll (Session s, Order defaultOrder) {
		return super.findAll(s, defaultOrder);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param menuitemprice a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.MenuItemPrice menuitemprice)
		throws org.hibernate.HibernateException {
		return (java.lang.Integer) super.save(menuitemprice);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param menuitemprice a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.MenuItemPrice menuitemprice, Session s)
		throws org.hibernate.HibernateException {
		return (java.lang.Integer) save((Object) menuitemprice, s);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param menuitemprice a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(com.floreantpos.model.MenuItemPrice menuitemprice)
		throws org.hibernate.HibernateException {
		saveOrUpdate((Object) menuitemprice);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param menuitemprice a transient instance containing new or updated state.
	 * @param s the Session.
	 */
	public void saveOrUpdate(com.floreantpos.model.MenuItemPrice menuitemprice, Session s)
		throws org.hibernate.HibernateException {
		saveOrUpdate((Object) menuitemprice, s);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param menuitemprice a transient instance containing updated state
	 */
	public void update(com.floreantpos.model.MenuItemPrice menuitemprice) 
		throws org.hibernate.HibernateException {
		update((Object) menuitemprice);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param menuitemprice a transient instance containing updated state
	 * @param the Session
	 */
	public void update(com.floreantpos.model.MenuItemPrice menuitemprice, Session s)
		throws org.hibernate.HibernateException {
		update((Object) menuitemprice, s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
	public void delete(java.lang.Integer id)
		throws org.hibernate.HibernateException {
		delete((Object) load(id));
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param id the instance ID to be removed
	 * @param s the Session
	 */
	public void delete(java.lang.Integer id, Session s)
		throws org.hibernate.HibernateException {
		delete((Object) load(id, s), s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param menuitemprice the instance to be removed
	 */
	public void delete(com.floreantpos.model.MenuItemPrice menuitemprice)
		throws org.hibernate.HibernateException {
		delete((Object) menuitemprice);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param menuitemprice the instance to be removed
	 * @param s the Session
	 */
	public void delete(com.floreantpos.model.MenuItemPrice menuitemprice, Session s)
		throws org.hibernate.HibernateException {
		delete((Object) menuitemprice, s);
	}
	
	/**
	 * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
	 * long-running sessions that span many business tasks. This method is, however, useful in certain special circumstances.
	 * For example 
	 * <ul> 
	 * <li>where a database trigger alters the object state upon insert or update</li>
	 * <li>after executing direct SQL (eg. a mass update) in the same session</li>
	 * <li>after inserting a Blob or Clob</li>
	 * </ul>
	 */
	public void refresh (com.floreantpos.model.MenuItemPrice menuitemprice, Session s)
		throws org.hibernate.HibernateException {
		refresh((Object) menuitemprice, s);
	}


}