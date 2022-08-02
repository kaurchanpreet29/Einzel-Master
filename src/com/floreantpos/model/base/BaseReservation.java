package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the RESERVATION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="RESERVATION"
 */

public abstract class BaseReservation  implements Comparable, Serializable {

	// constructors
	public BaseReservation () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseReservation (java.lang.Integer autoId) {
		this.setId(autoId);
		initialize();
	}
	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;
	// fields
	private java.lang.String name;
	private java.lang.String date;
	private java.lang.String email;
	private java.lang.String worker;
	private java.lang.String message;
	private java.lang.Integer person;
	private java.lang.String telefon;
	private java.lang.String time;
	
	protected java.lang.String tseVendor;
	protected java.lang.String tseId;	
	protected java.lang.Boolean tseLive;
		/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="AUTO_ID"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param autoId the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Constructor for required fields
	 */
	public BaseReservation (
		java.lang.Integer autoId,
		java.lang.String name,
		java.lang.String date,
		java.lang.String email,
		java.lang.String message,
		java.lang.Integer person,
		java.lang.String telefon,
		java.lang.String time) {

		this.setId(autoId);
		this.setName(name);
		this.setDate(date);
		this.setEmail(email);
		this.setMessage(message);
		this.setPerson(person);
		this.setTelefon(telefon);
		this.setTime(time);
		initialize();
	}

	protected void initialize () {}


	/**
	 * Return the value associated with the column: NAME
	 */
	public java.lang.String getName () {
		return name;
	}

	
	public java.lang.String getWorker() {
		return worker;
	}

	public void setWorker(java.lang.String worker) {
		this.worker = worker;
	}

	/**
	 * Set the value related to the column: NAME
	 * @param phone the NAME value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}
	/**
	 * Return the value associated with the column: DATE
	 */
	public java.lang.String getDate () {
		return date;
	}

	/**
	 * Set the value related to the column: DATE
	 * @param time the DATE value
	 */
	public void setDate (java.lang.String date) {
		this.date = date;
	}
	
	
	/**
	 * Return the value associated with the column: EMAIL
	 */
	public java.lang.String getEmail () {
		return email;
	}

	/**
	 * Set the value related to the column: EMAIL
	 * @param time the EMAIL value
	 */
	public void setEmail (java.lang.String email) {
		this.email = email;
	}
	
	/**
	 * Return the value associated with the column: MESSAGE
	 */
	public java.lang.String getMessage () {
		return message;
	}

	/**
	 * Set the value related to the column: MESSAGE
	 * @param time the MESSAGE value
	 */
	public void setMessage (java.lang.String message) {
		this.message = message;
	}
	
	/**
	 * Return the value associated with the column: PERSON
	 */
	public java.lang.Integer getPerson () {
		return person;
	}

	/**
	 * Set the value related to the column: PERSON
	 * @param time the PERSON value
	 */
	public void setPerson (java.lang.Integer person) {
		this.person = person;
	}
	/**
	 * Return the value associated with the column: TELEFON
	 */
	public java.lang.String getTelefon () {
		return telefon;
	}

	/**
	 * Set the value related to the column: TELEFON
	 * @param time the TELEFON value
	 */
	public void setTelefon(java.lang.String telefon) {
		this.telefon = telefon;
	}
	
	/**
	 * Return the value associated with the column: TIME
	 */
	public java.lang.String getTime () {
		return time;
	}

	/**
	 * Set the value related to the column: TIME
	 * @param time the TIME value
	 */
	public void setTime (java.lang.String time) {
		this.time = time;
	}
	
	/*
	 * Tse Data
	 */
	
	public java.lang.String getTseVendor() {
		return tseVendor;
	}

	public void setTseVendor(java.lang.String tseVendor) {
		this.tseVendor = tseVendor;
	}
	public java.lang.String getTseId() {
		return tseId!=null?tseId:"";
	}

	public void setTseId(java.lang.String tseId) {
		this.tseId = tseId;
	}

	
	public java.lang.Boolean isTseLive() {
		return tseLive!=null?tseLive:false;
	}

	public void setTseLive(java.lang.Boolean tseLive) {
		this.tseLive = tseLive;
	}
	
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.CallList)) return false;
		else {
			com.floreantpos.model.Reservation reservation = (com.floreantpos.model.Reservation) obj;
			if (null == this.getId() || null == reservation.getId()) return false;
			else return (this.getId().equals(reservation.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
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