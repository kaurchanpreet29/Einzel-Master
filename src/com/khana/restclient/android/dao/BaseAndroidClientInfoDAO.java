package com.khana.restclient.android.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * @author Jyoti Rai
 *
 */

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseAndroidClientInfoDAO extends com.floreantpos.model.dao._RootDAO {

	// query name references


	public static AndroidClientInfoDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static AndroidClientInfoDAO getInstance () {
		if (null == instance) instance = new AndroidClientInfoDAO();
		return instance;
	}

	public Class getReferenceClass () {
		return com.khana.restclient.android.AndroidClientInfo.class;
	}

    public Order getDefaultOrder () {
		return null;
    }

	/**
	 * Cast the object as a com.khana.restclient.android.AndroidClientInfo
	 */
	public com.khana.restclient.android.AndroidClientInfo cast (Object object) {
		return (com.khana.restclient.android.AndroidClientInfo) object;
	}

	public com.khana.restclient.android.AndroidClientInfo get(java.lang.Integer key)
	{
		return (com.khana.restclient.android.AndroidClientInfo) get(getReferenceClass(), key);
	}

	public com.khana.restclient.android.AndroidClientInfo get(java.lang.Integer key, Session s)
	{
		return (com.khana.restclient.android.AndroidClientInfo) get(getReferenceClass(), key, s);
	}

	public com.khana.restclient.android.AndroidClientInfo load(java.lang.Integer key)
	{
		return (com.khana.restclient.android.AndroidClientInfo) load(getReferenceClass(), key);
	}

	public com.khana.restclient.android.AndroidClientInfo load(java.lang.Integer key, Session s)
	{
		return (com.khana.restclient.android.AndroidClientInfo) load(getReferenceClass(), key, s);
	}

	public com.khana.restclient.android.AndroidClientInfo loadInitialize(java.lang.Integer key, Session s) 
	{ 
		com.khana.restclient.android.AndroidClientInfo obj = load(key, s); 
		if (!Hibernate.isInitialized(obj)) {
			Hibernate.initialize(obj);
		} 
		return obj; 
	}

/* Generic methods */

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.khana.restclient.android.AndroidClientInfo> findAll () {
		return super.findAll();
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.khana.restclient.android.AndroidClientInfo> findAll (Order defaultOrder) {
		return super.findAll(defaultOrder);
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
	public java.util.List<com.khana.restclient.android.AndroidClientInfo> findAll (Session s, Order defaultOrder) {
		return super.findAll(s, defaultOrder);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param AndroidClientInfo a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.khana.restclient.android.AndroidClientInfo AndroidClientInfo)
	{
		return (java.lang.Integer) super.save(AndroidClientInfo);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param AndroidClientInfo a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.khana.restclient.android.AndroidClientInfo AndroidClientInfo, Session s)
	{
		return (java.lang.Integer) save((Object) AndroidClientInfo, s);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param AndroidClientInfo a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(com.khana.restclient.android.AndroidClientInfo AndroidClientInfo)
	{
		saveOrUpdate((Object) AndroidClientInfo);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param AndroidClientInfo a transient instance containing new or updated state.
	 * @param s the Session.
	 */
	public void saveOrUpdate(com.khana.restclient.android.AndroidClientInfo AndroidClientInfo, Session s)
	{
		saveOrUpdate((Object) AndroidClientInfo, s);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param AndroidClientInfo a transient instance containing updated state
	 */
	public void update(com.khana.restclient.android.AndroidClientInfo AndroidClientInfo) 
	{
		update((Object) AndroidClientInfo);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param AndroidClientInfo a transient instance containing updated state
	 * @param the Session
	 */
	public void update(com.khana.restclient.android.AndroidClientInfo AndroidClientInfo, Session s)
	{
		update((Object) AndroidClientInfo, s);
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
	 * @param AndroidClientInfo the instance to be removed
	 */
	public void delete(com.khana.restclient.android.AndroidClientInfo AndroidClientInfo)
	{
		delete((Object) AndroidClientInfo);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param AndroidClientInfo the instance to be removed
	 * @param s the Session
	 */
	public void delete(com.khana.restclient.android.AndroidClientInfo AndroidClientInfo, Session s)
	{
		delete((Object) AndroidClientInfo, s);
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
	public void refresh (com.khana.restclient.android.AndroidClientInfo AndroidClientInfo, Session s)
	{
		refresh((Object) AndroidClientInfo, s);
	}


}