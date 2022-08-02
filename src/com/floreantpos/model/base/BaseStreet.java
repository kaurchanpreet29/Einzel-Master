package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;


/**
 * This is an object that contains data related to the STREET table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="STREET"
 */

public abstract class BaseStreet  implements Comparable, Serializable {

	// constructors
	public BaseStreet () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseStreet (java.lang.Integer autoId) {
		this.setId(autoId);
		initialize();
	}
	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;
	// fields
	private java.lang.String name;
	private java.lang.String plz;
	private java.lang.String bezirk;
	private java.lang.String ort;
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
	public BaseStreet (
		java.lang.Integer autoId,
		java.lang.String name,
		java.lang.String plz,
		java.lang.String bezirk,
		java.lang.String ort) {

		this.setId(autoId);
		this.setName(name);
		this.setPlz(plz);
		this.setBezirk(bezirk);
		this.setOrt(ort);
		initialize();
	}

	protected void initialize () {}


	/**
	 * Return the value associated with the column: NAME
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: NAME
	 * @param phone the NAME value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}
	/**
	 * Return the value associated with the column: PLZ
	 */
	public java.lang.String getPlz () {
		return plz;
	}

	/**
	 * Set the value related to the column: PLZ
	 * @param time the PLZ value
	 */
	public void setPlz (java.lang.String plz) {
		this.plz = plz;
	}
	
	
	/**
	 * Return the value associated with the column: BEZIRK
	 */
	public java.lang.String getBezirk () {
		return bezirk;
	}

	/**
	 * Set the value related to the column: BEZIRK
	 * @param time the BEZIRK value
	 */
	public void setBezirk (java.lang.String bezirk) {
		this.bezirk = bezirk;
	}
	
	/**
	 * Return the value associated with the column: ORT
	 */
	public java.lang.String getOrt () {
		return ort;
	}

	/**
	 * Set the value related to the column: ORT
	 * @param time the ORT value
	 */
	public void setOrt (java.lang.String ort) {
		this.ort = ort;
	}
	
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.CallList)) return false;
		else {
			com.floreantpos.model.CallList callList = (com.floreantpos.model.CallList) obj;
			if (null == this.getId() || null == callList.getAutoId()) return false;
			else return (this.getId().equals(callList.getAutoId()));
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