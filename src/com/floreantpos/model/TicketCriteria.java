package com.floreantpos.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author office02
 *
 */
public class TicketCriteria implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 810663167250206383L;
	
	private Date startDate;
	private Date endDate;
	private User user;
	private boolean close;
	private boolean drawerResseted;
	
	/**
	 * 
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	

	public boolean isClose() {
		return close;
	}
	public void setClose(boolean close) {
		this.close = close;
	}
	public boolean isDrawerResseted() {
		return drawerResseted;
	}
	public void setDrawerResseted(boolean drawerResseted) {
		this.drawerResseted = drawerResseted;
	}

}
