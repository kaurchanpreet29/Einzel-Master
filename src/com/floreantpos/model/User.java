package com.floreantpos.model;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.LogFactory;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseUser;
import com.floreantpos.model.dao.UserDAO;

@XmlRootElement(name="user")
public class User extends BaseUser {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public User () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public User (java.lang.Integer autoId) {
		super(autoId);
	}

	/**
	 * Constructor for required fields
	 */
	public User (
		java.lang.Integer autoId,
		java.lang.String password) {

		super (
			autoId,
			password);
	}

	/*[CONSTRUCTOR MARKER END]*/

	public final static String USER_TYPE_MANAGER = "MANAGER";
	public final static String USER_TYPE_CASHIER = "CASHIER";
	public final static String USER_TYPE_SERVER = "SERVER";
	
	public boolean hasPermission(UserPermission permission) {
		return getType().hasPermission(permission);
	}

	
	public void doClockIn(Terminal terminal, Shift shift, Calendar currentTime) {
		setClockedIn(true);
		setCurrentShift(shift);
		setCurrentTerminal(terminal);
		setLastClockInTime(currentTime.getTime());
		
		LogFactory.getLog(Application.class).info("terminal id befor saving clockIn=" + terminal.getId());
		
		AttendenceHistory attendenceHistory = new AttendenceHistory();
		attendenceHistory.setClockInTime(currentTime.getTime());
		attendenceHistory.setClockInHour(Short.valueOf((short) currentTime.get(Calendar.HOUR)));
		attendenceHistory.setUser(this);
		attendenceHistory.setTerminal(terminal);
		attendenceHistory.setShift(shift);
		
		UserDAO.getInstance().saveClockIn(this, attendenceHistory, shift, currentTime);
	}
	
	public void doClockOut(AttendenceHistory attendenceHistory, Shift shift, Calendar currentTime) {
		setClockedIn(false);
		setCurrentShift(null);
		setCurrentTerminal(null);
		setLastClockInTime(null);
		
		attendenceHistory.setClockedOut(true);
		attendenceHistory.setClockOutTime(currentTime.getTime());
		attendenceHistory.setClockOutHour(Short.valueOf((short) currentTime.get(Calendar.HOUR)));
		
		UserDAO.getInstance().saveClockOut(this, attendenceHistory, shift, currentTime);
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
	
	public boolean isManager() {
		return hasPermission(UserPermission.PERFORM_MANAGER_TASK);
	}
	
	public boolean isSuperUser() {
		return getFirstName().equals("Super-User");
	}
	
	public boolean isAdministrator() {
		return hasPermission(UserPermission.PERFORM_ADMINISTRATIVE_TASK);
	}
}