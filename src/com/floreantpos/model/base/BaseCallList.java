package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the CALLLIST table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CALLLIST"
 */

public abstract class BaseCallList  implements Comparable, Serializable {

	// constructors
	public BaseCallList () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCallList (java.lang.Integer autoId) {
		this.setAutoId(autoId);
		initialize();
	}
	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer autoId;
	// fields
	private java.lang.String phone;
	private java.lang.String time;
	private java.lang.Integer number;
		/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="AUTO_ID"
     */
	public java.lang.Integer getAutoId () {
		return autoId;
	}

	/**
	 * Set the unique identifier of this class
	 * @param autoId the new ID
	 */
	public void setAutoId (java.lang.Integer autoId) {
		this.autoId = autoId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCallList (
		java.lang.Integer autoId,
		java.lang.String phone,
		java.lang.String time) {

		this.setAutoId(autoId);
		this.setPhone(phone);
		this.setTime(time);
		
		initialize();
	}

	protected void initialize () {}


	/**
	 * Return the value associated with the column: PHONE
	 */
	public java.lang.String getPhone () {
		return phone;
	}

	/**
	 * Set the value related to the column: PHONE
	 * @param phone the PHONE value
	 */
	public void setPhone (java.lang.String phone) {
		this.phone = phone;
	}
	/**
	 * Return the value associated with the column: TIME
	 */
	public java.lang.String getTime () {
		return time;
	}

	/**
	 * Set the value related to the column: NUMBER
	 * @param time the NUMBER value
	 */
	public void setNumber (java.lang.Integer number) {
		this.number = number;
	}
	
	/**
	 * Return the value associated with the column: NUMBER
	 */
	public java.lang.Integer getNumber () {
		return number;
	}

	/**
	 * Set the value related to the column: TIME
	 * @param time the TIME value
	 */
	public void setTime (java.lang.String time) {
		this.time = time;
	}
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.CallList)) return false;
		else {
			com.floreantpos.model.CallList callList = (com.floreantpos.model.CallList) obj;
			if (null == this.getAutoId() || null == callList.getAutoId()) return false;
			else return (this.getAutoId().equals(callList.getAutoId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getAutoId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getAutoId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	public String toString () {
		return super.toString();
	}


}