package com.floreantpos.model.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

public class BaseTSEReceiptDataDAO  extends com.floreantpos.model.dao._RootDAO {

	// query name references


	public static BaseTSEReceiptDataDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static BaseTSEReceiptDataDAO getInstance () {
		if (null == instance) instance = new BaseTSEReceiptDataDAO();
		return instance;
	}

	public Class getReferenceClass () {
		return com.floreantpos.model.TSEReceiptData.class;
	}

    public Order getDefaultOrder () {
		return Order.asc("name");
    }

	/**
	 * Cast the object as a com.floreantpos.model.TSEReceiptData
	 */
	public com.floreantpos.model.TSEReceiptData cast (Object object) {
		return (com.floreantpos.model.TSEReceiptData) object;
	}

	public com.floreantpos.model.TSEReceiptData get(java.lang.Integer key)
	{
		return (com.floreantpos.model.TSEReceiptData) get(getReferenceClass(), key);
	}

	public com.floreantpos.model.TSEReceiptData get(java.lang.Integer key, Session s)
	{
		return (com.floreantpos.model.TSEReceiptData) get(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.TSEReceiptData load(java.lang.Integer key)
	{
		return (com.floreantpos.model.TSEReceiptData) load(getReferenceClass(), key);
	}

	public com.floreantpos.model.TSEReceiptData load(java.lang.Integer key, Session s)
	{
		return (com.floreantpos.model.TSEReceiptData) load(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.TSEReceiptData loadInitialize(java.lang.Integer key, Session s) 
	{ 
		com.floreantpos.model.TSEReceiptData obj = load(key, s); 
		if (!Hibernate.isInitialized(obj)) {
			Hibernate.initialize(obj);
		} 
		return obj; 
	}

/* Generic methods */

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.TSEReceiptData> findAll () {
		return super.findAll();
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.TSEReceiptData> findAll (Order defaultOrder) {
		return super.findAll(defaultOrder);
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
	public java.util.List<com.floreantpos.model.TSEReceiptData> findAll (Session s, Order defaultOrder) {
		return super.findAll(s, defaultOrder);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param TSEReceiptData a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.TSEReceiptData TSEReceiptData)
	{
		return (java.lang.Integer) super.save(TSEReceiptData);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param TSEReceiptData a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.TSEReceiptData TSEReceiptData, Session s)
	{
		return (java.lang.Integer) save((Object) TSEReceiptData, s);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param TSEReceiptData a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(com.floreantpos.model.TSEReceiptData TSEReceiptData)
	{
		saveOrUpdate((Object) TSEReceiptData);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param TSEReceiptData a transient instance containing new or updated state.
	 * @param s the Session.
	 */
	public void saveOrUpdate(com.floreantpos.model.TSEReceiptData TSEReceiptData, Session s)
	{
		saveOrUpdate((Object) TSEReceiptData, s);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param TSEReceiptData a transient instance containing updated state
	 */
	public void update(com.floreantpos.model.TSEReceiptData TSEReceiptData) 
	{
		update((Object) TSEReceiptData);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param TSEReceiptData a transient instance containing updated state
	 * @param the Session
	 */
	public void update(com.floreantpos.model.TSEReceiptData TSEReceiptData, Session s)
	{
		update((Object) TSEReceiptData, s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
	public void delete(java.lang.Integer id)
	{
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
	{
		delete((Object) load(id, s), s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param TSEReceiptData the instance to be removed
	 */
	public void delete(com.floreantpos.model.TSEReceiptData TSEReceiptData)
	{
		delete((Object) TSEReceiptData);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param TSEReceiptData the instance to be removed
	 * @param s the Session
	 */
	public void delete(com.floreantpos.model.TSEReceiptData TSEReceiptData, Session s)
	{
		delete((Object) TSEReceiptData, s);
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
	public void refresh (com.floreantpos.model.TSEReceiptData TSEReceiptData, Session s)
	{
		refresh((Object) TSEReceiptData, s);
	}


}