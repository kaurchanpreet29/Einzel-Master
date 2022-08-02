package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.Reservation;

public class ReservationDAO extends BaseReservationDAO{
	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ReservationDAO () {}
	
	@SuppressWarnings("unchecked")
	public Reservation findById(int id) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Reservation.PROP_ID, id));
			return (Reservation)criteria.uniqueResult();
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
