package com.floreantpos.model.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCriteria;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.User;
import com.floreantpos.model.VoidTransaction;
import com.floreantpos.model.util.TicketSummary;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.util.BusinessDateUtil;

public class TicketDAO extends BaseTicketDAO {
	private final static TicketDAO instance = new TicketDAO();

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public TicketDAO() {
	}
	
	@Override
	public void saveOrUpdate(Ticket ticket) {
//		ticket.setActiveDate(Calendar.getInstance().getTime());
		
		super.saveOrUpdate(ticket);
	}
	
	@Override
	public void saveOrUpdate(Ticket ticket, Session s) {
//		ticket.setActiveDate(Calendar.getInstance().getTime());
		
		super.saveOrUpdate(ticket, s);
	}
	
	public Ticket loadFullTicket(int id) {
		Session session = createNewSession();
		
		Ticket ticket = (Ticket) session.get(getReferenceClass(), id);
		
	
		if(ticket == null) return null;
		
		Hibernate.initialize(ticket.getTicketItems());
		Hibernate.initialize(ticket.getCouponAndDiscounts());
		Hibernate.initialize(ticket.getTransactions());
		Hibernate.initialize(ticket.getDeletedItems());

		List<TicketItem> ticketItems = ticket.getTicketItems();
		if (ticketItems != null) {
			for (TicketItem ticketItem : ticketItems) {
				if (ticketItem == null) {
					continue;
				}
				List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
				Hibernate.initialize(ticketItemModifierGroups);
				if (ticketItemModifierGroups != null) {
					for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
						Hibernate.initialize(ticketItemModifierGroup.getTicketItemModifiers());
					}
				}
			}
		}
		
		session.close();
		
		return ticket;
	}
	
	public List<Ticket> findAllClosedTicketsDate(Date startDate, Date endDate, User user) {
    Session session = null;

    try {
      session = getSession();
      Criteria criteria = session.createCriteria(getReferenceClass());
      criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
      criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
      criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
      criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
      criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
      
      if(user != null) {
        criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
      }
      List list = criteria.list();
      return list;

    } finally {
      closeSession(session);
    }
  }
	
	public Ticket loadCouponsAndTransactions(int id) {
		Session session = createNewSession();
		
		Ticket ticket = (Ticket) session.get(getReferenceClass(), id);
		
		Hibernate.initialize(ticket.getCouponAndDiscounts());
		Hibernate.initialize(ticket.getTransactions());
		
		session.close();
		
		return ticket;
	}
	
	public List<Gratuity> getServerGratuities(Terminal terminal, String transactionType) {
		Session session = null;
		ArrayList<Gratuity> gratuities = new ArrayList<Gratuity>();

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			criteria.createAlias(Ticket.PROP_GRATUITY, "gratuity");
			criteria.add(Restrictions.eq("gratuity.paid", Boolean.FALSE));

			List list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				gratuities.add(ticket.getGratuity());
			}
			return gratuities;
		} finally {
			closeSession(session);
		}
	}
	public double getPaidGratuityAmount(Terminal terminal) {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass(), "t");
			criteria = criteria.createAlias(Ticket.PROP_GRATUITY, "gratuity");
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			criteria.add(Restrictions.eq("gratuity.paid", Boolean.TRUE));
			criteria.setProjection(Projections.sum("gratuity.amount"));
			
			List list = criteria.list();
			if(list.size() > 0 && list.get(0) instanceof Number) {
				return ((Number) list.get(0)).doubleValue();
			}
			return 0;
		} finally {
			closeSession(session);
		}
	}
	
	/**
	 * This method is used to find tickets list based on criteria
	 * @return List of Tickets
	 */
	public List<Ticket> findTickets(TicketCriteria criteria)
	{
		Session session = null;
		List<Ticket> ticketList = null;
		try {
			session = getSession();
			Criteria cr = session.createCriteria(Ticket.class)
					.setProjection(Projections.projectionList()
							.add(Projections.property("cashPayment"),"cashPayment")
							.add(Projections.property("bewirt"),"bewirt")
							.add(Projections.property("tableNumbers"),"tableNumbers")
							.add(Projections.property("owner"), "owner")
							.add(Projections.property("gutscheinHausbon"),"gutscheinHausbon")
							.add(Projections.property("debitorPayment"),"debitorPayment")
							.add(Projections.property("onlinePayment"),"onlinePayment")
							.add(Projections.property("gutscheinPayment"),"gutscheinPayment")
							.add(Projections.property("ticketType"),"ticketType")
							.add(Projections.property("paid"),"paid")
							.add(Projections.property("assignedDriver"), "assignedDriver")
							.add(Projections.property("status"),"status")
							.add(Projections.property("totalAmount"),"totalAmount")
							.add(Projections.property("tipAmount"),"tipAmount")
							.add(Projections.property("closed"),"closed")
							//.add(Projections.property("createDate"),"createDate")
							.add(Projections.property("activeDate"),"activeDate")
							.add(Projections.property("closingDate"),"closingDate")
							.add(Projections.property("id"),"id"))
					.setResultTransformer(Transformers.aliasToBean(Ticket.class));
			if(criteria.getStartDate() != null)
				cr.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, criteria.getStartDate()));
			if(criteria.getEndDate() != null)
				cr.add(Restrictions.le(Ticket.PROP_CREATE_DATE, criteria.getEndDate()));
			cr.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			cr.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			if(criteria.getUser() != null) {
				cr.add(Restrictions.eq(Ticket.PROP_OWNER, criteria.getUser()));
			}
			ticketList = cr.list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			closeSession(session);
		}
		return ticketList;
	}

	/**
	 * This method is used to find tickets list based on criteria
	 * @return List of Tickets
	 */
	public List<Ticket> findTicketsWithIDOnly(TicketCriteria criteria)
	{
		Session session = null;
		List<Ticket> ticketList = null;
		try {
			session = getSession();
			Criteria cr = session.createCriteria(Ticket.class)
					.setProjection(Projections.projectionList()						     
							.add(Projections.property("id"),"id")
							.add(Projections.property("activeDate"),"activeDate")
							.add(Projections.property("closingDate"),"closingDate"))

					.setResultTransformer(Transformers.aliasToBean(Ticket.class));
			cr.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, criteria.getStartDate()));
			cr.add(Restrictions.le(Ticket.PROP_CREATE_DATE, criteria.getEndDate()));
			if(criteria.isDrawerResseted()==false)
				cr.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, criteria.isDrawerResseted()));
			cr.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			if(criteria.getUser() != null) {
				cr.add(Restrictions.eq(Ticket.PROP_OWNER, criteria.getUser()));
			}

			ticketList = cr.list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			closeSession(session);
		}
		return ticketList;
	}


	public List<Ticket> findTicketsforTable(TicketCriteria criteria)
	{
		Session session = null;
		List<Ticket> ticketList = null;
		try {
			session = getSession();
			Criteria cr = session.createCriteria(Ticket.class)
					.setProjection(Projections.projectionList()						     
							.add(Projections.property("id"),"id")
							.add(Projections.property("totalAmount"),"totalAmount")
							.add(Projections.property("owner"), "owner")
							.add(Projections.property("tables"), "tables")
							)
					.setResultTransformer(Transformers.aliasToBean(Ticket.class));
			cr.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, criteria.getStartDate()));
			cr.add(Restrictions.le(Ticket.PROP_CREATE_DATE, criteria.getEndDate()));
			cr.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			cr.add(Restrictions.eq(Ticket.PROP_CLOSED, criteria.isClose()));
			cr.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			if(criteria.getUser() != null) {
				cr.add(Restrictions.eq(Ticket.PROP_OWNER, criteria.getUser()));
			}

			ticketList = cr.list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			closeSession(session);
		}
		return ticketList;
	}
	
	public void voidTicket(Ticket ticket) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = createNewSession();
			tx = session.beginTransaction();
		
			Terminal terminal = Application.getInstance().getTerminal();
			
			ticket.setVoided(true);
			ticket.setClosed(true);
			ticket.setClosingDate(new Date());
			ticket.setTerminal(terminal);
			
			if(ticket.isPaid()) {
				VoidTransaction transaction = new VoidTransaction();
				transaction.setTicket(ticket);
				transaction.setTerminal(terminal);
				transaction.setTransactionTime(new Date());
				transaction.setTransactionType(TransactionType.DEBIT.name());
				transaction.setPaymentType(PaymentType.CASH.name());
				transaction.setAmount(ticket.getPaidAmount());
				transaction.setTerminal(Application.getInstance().getTerminal());
				transaction.setCaptured(true);
				
				PosTransactionService.adjustTerminalBalance(transaction);
				
				ticket.addTotransactions(transaction);
			}
			
			session.update(ticket);
			session.update(terminal);
			
			session.flush();
			tx.commit();
		} catch (Exception x) {
			try {
				tx.rollback();
			} catch (Exception e) {
			}
			throw x;
		}

		finally {
			closeSession(session);
		}
	}

	public List<Ticket> findAllOpenTickets()
	{
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	public List<Ticket> findAllDeliveryTickets()
	{
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name()));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	public List<Ticket> findDateTickets(Date startDate, Date endDate) {
    Session session = null;
    try {
    	
      session = getSession();
      Criteria criteria = session.createCriteria(getReferenceClass());
      criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
      criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));     
      return criteria.list();
    } finally {
      closeSession(session);
    }
  }
	
	public List<Ticket> findDateTickets_zws(Date from, Date to){
		Session session = null;
	    try {
	      session = getSession();
	      Criteria criteria = session.createCriteria(getReferenceClass());
	      criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, from));
	      criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, to));   
	      criteria.add(Restrictions.isNull(Ticket.PROP_TSE_RECEIPT_TX_ID));
	      criteria.add(Restrictions.eq(Ticket.PROP_CASH_PAYMENT, Boolean.TRUE));	      	     
	      return criteria.list();
	    
	    } finally {
	      closeSession(session);
	    }
	}
	
	public List<Ticket> findNonDateTickets(Date startDate, Date endDate) {
	    Session session = null;
	    try {
	      session = getSession();
	      Criteria criteria = session.createCriteria(getReferenceClass());
	      criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
	      criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
	      criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
	      
	      return criteria.list();
	    } finally {
	      closeSession(session);
	    }
	  }
	
	public List<Ticket> findOpenTickets() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findGdpduTicketsNoSrToday() {
    Session session = null;
    try {
      session = getSession();
      Criteria criteria = session.createCriteria(getReferenceClass());
      criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, BusinessDateUtil.startOfOfficialDay(new Date())));
      criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, BusinessDateUtil.endOfOfficialDay(new Date())));
      criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
      criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
      return criteria.list();
    } finally {
      closeSession(session);
    }
  }
	
	public List<Ticket> findOpenTicketsForUser(User user) {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
//			criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.DINE_IN.name()));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	public List<Ticket> findOpenTickets(Date startDate, Date endDate) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.DINE_IN.name()));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	 public List<Ticket> findGdpduTickets(Date startDate, Date endDate) {
	    Session session = null;
	    try {
	      session = getSession();
	      Criteria criteria = session.createCriteria(getReferenceClass());
	      criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
	      criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
	      criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.TRUE));
	      criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
	      return criteria.list();
	    } finally {
	      closeSession(session);
	    }
	  }
	 
	public List<Ticket> findPreviousTickets(Date date) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.valueOf(false)));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, date));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	public List<Ticket> findOpenHomeTickets() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name()));
			
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findClosedHomeTickets() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			//criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name()));
			criteria.add(Restrictions.or(Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name()), Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name())));
		
			List list = criteria.list();
			return list;
			
		} finally {
			closeSession(session);
		}
	}
	public List<Ticket> findHomeTickets() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.or(Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name()), Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name())));		
			List list = criteria.list();
			return list;
			
		} finally {
			closeSession(session);
		}
	}
	public List<Ticket> findByOwner(Date startDate, Date endDate, boolean closed) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.valueOf(closed)));
			List list = criteria.list();
			return list;
			
		} finally {
			closeSession(session);
		}
	}
	
	
	public List<Ticket> findRefundedTickets(Date startDate, Date endDate) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, false));
			List list = criteria.list();
			return list;			
		} finally {
			closeSession(session);
		}
	}
	
	public List<Ticket> findByOwner(Date startDate, Date endDate, User user) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, false));
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));

			List list = criteria.list();
			return list;
			
		} finally {
			closeSession(session);
		}
	}
	
	public List<Ticket> findBySalseId(int salseId) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_SALES_ID, salseId));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	public List<Ticket> findAllCurrentTickets() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findAllCurrentTicketsDate(Date startDate, Date endDate) {
    Session session = null;

    try {
      session = getSession();
      Criteria criteria = session.createCriteria(getReferenceClass());
      criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
      criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
      criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
      criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
      List list = criteria.list();
      return list;

    } finally {
      closeSession(session);
    }
  }

	public List<Ticket> findAllCurrentTickets(User user) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}	
	
	public List<Ticket> findTktByCustomer(Customer cust) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_KUNDEN_LOYALTY_NO, cust.getLoyaltyNo()));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	
	public List<Ticket> findOpenTktByCustomer(Customer cust) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_KUNDEN_LOYALTY_NO, cust.getLoyaltyNo()));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.FALSE));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	
	
	
	public List<Ticket> findTicketsByDriver(String name) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	public List<Ticket> findPaidTickets() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			//criteria.add(Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name()));
			criteria.add(Restrictions.or(Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name()), Restrictions.eq(Ticket.PROP_TICKET_TYPE, TicketType.HOME_DELIVERY.name())));
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}
	public Ticket findTicketByTableNumber(int tableNumber) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TABLENUMBER, Integer.valueOf(tableNumber)));
			
			List list = criteria.list();
			if(list.size() <= 0) {
				return null;
			}
			
			return (Ticket) list.get(0);
		} finally {
			closeSession(session);
		}
	}
	
	
	
	
	public Ticket findTicketByTicketId(int ticketId) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TICKET_ID, ticketId));
			
			List list = criteria.list();
			if(list.size() <= 0) {
				return null;
			}
			
			return (Ticket) list.get(0);
		} finally {
			closeSession(session);
		}
	}
	
	
//	public boolean hasTicketByTableNumber(int tableNumber) {
//		return findTicketByTableNumber(tableNumber) != null;
//	}

	public TicketSummary getOpenTicketSummary() {
		Session session = null;
		TicketSummary ticketSummary = new TicketSummary();
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count(Ticket.PROP_ID));
			projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
			criteria.setProjection(projectionList);

			List list = criteria.list();
			if (list.size() > 0) {
				Object[] o = (Object[]) list.get(0);
				ticketSummary.setTotalTicket(((Integer) o[0]).intValue());
				ticketSummary.setTotalPrice(o[1] == null ? 0 : ((Double) o[1]).doubleValue());
			}
			return ticketSummary;
		} finally {
			closeSession(session);
		}
	}

	public TicketSummary getClosedTicketSummary(Terminal terminal) {

		Session session = null;
		TicketSummary ticketSummary = new TicketSummary();
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count(Ticket.PROP_ID));
			projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
			criteria.setProjection(projectionList);

			List list = criteria.list();
			if (list.size() > 0) {
				Object[] o = (Object[]) list.get(0);
				ticketSummary.setTotalTicket(((Integer) o[0]).intValue());
				ticketSummary.setTotalPrice(o[1] == null ? 0 : ((Double) o[1]).doubleValue());
			}
			return ticketSummary;
		} finally {
			closeSession(session);
		}
	}

	//	public void saveTransaction(Ticket ticket, com.floreantpos.model.PosTransaction transaction, Terminal terminal, User user) throws Exception {
	//		Session session = null;
	//    	Transaction tx = null;
	//    	try {
	//    		if(transaction instanceof CashTransaction) {
	//    			terminal.setCurrentBalance(terminal.getCurrentBalance() + ticket.getTotalPrice());
	//    		}
	//			
	//			ticket.setVoided(false);
	//			ticket.setPaid(true);
	//			ticket.setClosed(true);
	//			ticket.setDrawerResetted(false);
	//			ticket.setClosingDate(new Date());
	//			ticket.setTerminal(terminal);
	//			
	//			transaction.setTicket(ticket);
	//			transaction.setAmount(ticket.getSubTotal());
	//			transaction.setTaxAmount(ticket.getTotalTax());
	//			transaction.setAppliedDiscount(ticket.getTotalDiscount());
	//			transaction.setTerminal(terminal);
	//			transaction.setUser(user);
	//			transaction.setTransactionTime(new Date());
	//			
	//			session = createNewSession();
	//			tx = session.beginTransaction();
	//			
	//			saveOrUpdate(ticket, session);
	//			saveOrUpdate(transaction, session);
	//			saveOrUpdate(terminal, session);
	//			
	//			tx.commit();
	//			
	//		} catch (Exception e) {
	//			try {
	//				tx.rollback();
	//			}catch (Exception x) {}
	//			
	//			throw e;
	//		} finally {
	//			closeSession(session);
	//		}
	//	}
	public List<Ticket> findTickets(Date startDate, Date endDate, boolean closed) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
            //criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.valueOf(closed)));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	

	public List<Ticket> findTicketsToday(Date startDate, Date endDate) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<Ticket> findMonthTickets(Date startDate, Date endDate) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.TRUE));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<Ticket> findYearTickets(Date startDate, Date endDate) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.TRUE));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	
	public List<Ticket> findTicketsZ(Date startDate, Date endDate) {
		Session session = null;
		try {
			System.out.println("findTicketsZ(Date: ");
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.le(Ticket.PROP_DRAWER_RESETTED, true));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	public List<Ticket> findDateTickets(Date startDate, Date endDate, boolean closed) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findAllTickets(Date startDate, Date endDate) {
    Session session = null;
    try {
      session = getSession();
      Criteria criteria = session.createCriteria(getReferenceClass());
      criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
      criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
      return criteria.list();
    } finally {
      closeSession(session);
    }
  }
	
	public List<Ticket> findTicketsOpen(Date startDate, Date endDate, boolean closed) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.valueOf(closed)));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTabTicketsOpen() {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TAB_PRINT, Boolean.TRUE));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	
	public List<Ticket> findVoidedTickets(Date startDate, Date endDate, boolean closed) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.TRUE));
			
			criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.valueOf(closed)));

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTicketsForLaborHour(Date startDate, Date endDate, int hour, String userType, Terminal terminal) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_ACTIVE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_ACTIVE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_CREATION_HOUR, Integer.valueOf(hour)));
			//criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			//criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));

			if (userType != null) {
				criteria.createAlias(Ticket.PROP_OWNER, "u");
				criteria.add(Restrictions.eq("u.type", userType));
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			}

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<Ticket> findTicketsForShift(Date startDate, Date endDate, Shift shit, String userType, Terminal terminal) {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, startDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, endDate));
			criteria.add(Restrictions.eq(Ticket.PROP_SHIFT, shit));
			criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
//			criteria.add(Restrictions.eq(Ticket.PROP_REFUNDED, Boolean.FALSE));

			if (userType != null) {
				criteria.createAlias(Ticket.PROP_OWNER, "u");
				criteria.add(Restrictions.eq("u.type", userType));
			}
			if (terminal != null) {
				criteria.add(Restrictions.eq(Ticket.PROP_TERMINAL, terminal));
			}

			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public static TicketDAO getInstance() {
		return instance;
	}
}