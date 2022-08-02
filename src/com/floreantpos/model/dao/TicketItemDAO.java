package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.util.BusinessDateUtil;

public class TicketItemDAO extends BaseTicketItemDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public TicketItemDAO () {}

	public List<TicketItem> findByTicketId(int ticketid) {
		Session session = null;
		Ticket ticket = TicketDAO.getInstance().get(ticketid);
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(TicketItem.PROP_TICKET, ticket));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<TicketItem> findAllByDates(Date startDate, Date endDate) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.createAlias("ticket", "ticket", Criteria.INNER_JOIN);
			criteria.add(Restrictions.ge("ticket.createDate", BusinessDateUtil.startOfOfficialDay(startDate)));
			criteria.add(Restrictions.le("ticket.createDate", BusinessDateUtil.endOfOfficialDay(endDate)));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}



	public List<TicketItem> findByDate(Date startDate, Date endDate) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.createCriteria("ticket", "ticket");
			criteria.add(Restrictions.eq("ticket." + Ticket.PROP_PAID, Boolean.TRUE));
			criteria.add(Restrictions.eq("ticket." + Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.ge("ticket.createDate", BusinessDateUtil.startOfOfficialDay(startDate)));
			criteria.add(Restrictions.le("ticket.createDate", BusinessDateUtil.endOfOfficialDay(endDate)));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}


	public List<TicketItem> findByDate(Date date) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.createAlias("ticket", "ticket", Criteria.INNER_JOIN);
			criteria.add(Restrictions.ge("ticket.createDate", BusinessDateUtil.startOfOfficialDay(date)));
			criteria.add(Restrictions.le("ticket.createDate", BusinessDateUtil.endOfOfficialDay(date)));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
}