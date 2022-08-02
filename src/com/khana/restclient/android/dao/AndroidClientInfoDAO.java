package com.khana.restclient.android.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.khana.restclient.android.AndroidClientInfo;

/**
 * @author Jyoti Rai
 *
 */


public class AndroidClientInfoDAO extends BaseAndroidClientInfoDAO {
	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public AndroidClientInfoDAO () {}

	public List<AndroidClientInfo> findAll() {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());			
			return criteria.list();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}
	public AndroidClientInfo findByClientId(String clientId) {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(AndroidClientInfo.CLIENT_ID, clientId));			
			return (AndroidClientInfo) criteria.uniqueResult();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}
}