package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicket.KitchenTicketStatus;

public class KitchenTicketDAO extends BaseKitchenTicketDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public KitchenTicketDAO() {
	}

	public List<KitchenTicket> findAllOpen() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(KitchenTicket.PROP_STATUS, KitchenTicketStatus.WAITING.name()));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	 public List<KitchenTicket> findByTicketId(Integer id) {
		    Session session = null;
		
		    try {
		      session = getSession();
		      Criteria criteria = session.createCriteria(getReferenceClass());
		      criteria.add(Restrictions.eq(KitchenTicket.PROP_TICKET_ID, id));
		      return criteria.list();
		    } finally {
		      closeSession(session);
		    }
	    }
	
	 public List<KitchenTicket> deletMasterVoidArt(Integer ticketId){
		 Session session = null;
			
		    try {
		      session = getSession();
		      Criteria criteria = session.createCriteria(getReferenceClass());
		      criteria.add(Restrictions.eq(KitchenTicket.PROP_TICKET_ID, ticketId));
		      criteria.add(Restrictions.eq(KitchenTicket.PROP_SERVER_NAME, "Master"));
		       
		      
		      return criteria.list();
		    } finally {
		      closeSession(session);
		    }
	 }
	 
	 public List<KitchenTicket> findVoided(Integer ticketId) {
		    Session session = null;
		
		    try {
		      session = getSession();
		      Criteria criteria = session.createCriteria(getReferenceClass());
		      criteria.add(Restrictions.eq(KitchenTicket.PROP_TICKET_ID, ticketId));
		      criteria.add(Restrictions.ne(KitchenTicket.PROP_SERVER_NAME, "Master"));
		       
		      
		      return criteria.list();
		    } finally {
		      closeSession(session);
		    }
	    }
	 
	public List<KitchenTicket> findAllVoided(Date startDate, Date endDate) {
		Session session = null;

		try {			
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(KitchenTicket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(KitchenTicket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(KitchenTicket.PROP_VOIDED, Boolean.TRUE));
			List list = criteria.list();

			return list;
		} finally {
			closeSession(session);
		}
	}
	
	public KitchenTicket loadFullTicket(int id) {
		Session session = createNewSession();
		
		KitchenTicket ticket = (KitchenTicket) session.get(getReferenceClass(), id);
		
		if(ticket == null) return null;
		
		Hibernate.initialize(ticket.getTicketItems());

		
		session.close();
		
		return ticket;
	}
	
	
}